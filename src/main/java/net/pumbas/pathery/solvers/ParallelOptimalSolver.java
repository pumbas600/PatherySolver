package net.pumbas.pathery.solvers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Getter;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.exceptions.NoSolutionException;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.pathfinding.PathFinder;
import net.pumbas.pathery.pathfinding.PathFinderFactory;

public class ParallelOptimalSolver implements Solver {

  @Getter
  private int currentLongestPathLength;
  private AtomicLong prunedCount;
  private AtomicLong exploredCount;
  private Set<Position> bestWalls;

  @Override
  public OptimalSolution findOptimalSolution(PatheryMap map) {
    ExecutorService executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() - 1);
    PathFinder pathFinder = PathFinderFactory.getPathFinder(map);

    this.bestWalls = null;
    this.prunedCount = new AtomicLong();
    this.exploredCount = new AtomicLong();
    this.currentLongestPathLength = Integer.MIN_VALUE;

    int totalPositions = map.getWidth() * map.getHeight();
    Queue<Set<Position>> wallCombinations = new LinkedList<>();
    wallCombinations.add(new HashSet<>());

    while (!wallCombinations.isEmpty()) {
      Set<Position> walls = wallCombinations.poll();
      for (int positionIndex = 0; positionIndex < totalPositions; positionIndex++) {
        int x = positionIndex % map.getWidth();
        int y = positionIndex / map.getWidth();

        if (map.getTile(x, y) != TileType.OPEN) {
          continue;
        }

        Position position = new Position(x, y);
        Set<Position> newWalls = new HashSet<>(walls);
        newWalls.add(position);

        executorService.submit(
            () -> this.exploreWallCombination(map, pathFinder, newWalls));

        if (newWalls.size() < map.getMaxWalls()) {
          wallCombinations.add(newWalls);
        }
      }
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (this.bestWalls == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.bestWalls);
  }

  @Override
  public long getPrunedCount() {
    return this.prunedCount.get();
  }

  @Override
  public long getExploredCount() {
    return this.exploredCount.get();
  }

  private void exploreWallCombination(
      PatheryMap map,
      PathFinder pathFinder,
      Set<Position> walls
  ) {
    try {
      this.exploredCount.incrementAndGet();
      int pathLength = pathFinder.findCompletePath(map, walls).size();
      this.updateBestPath(pathLength, walls);
    } catch (NoPathException e) {
      this.prunedCount.incrementAndGet();
    }
  }


  private synchronized void updateBestPath(int pathLength, Set<Position> walls) {
    if (pathLength > this.currentLongestPathLength) {
      this.currentLongestPathLength = pathLength;
      this.bestWalls = walls;
    }
  }

}
