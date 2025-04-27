package net.pumbas.pathery.solvers;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.exceptions.NoSolutionException;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.SetWallCombination;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.models.WallCombination;
import net.pumbas.pathery.pathfinding.PathFinder;

@RequiredArgsConstructor
public class ParallelOptimalSolver implements Solver {

  @Getter
  private int currentLongestPathLength;
  @Getter
  private WallCombination currentBestWallCombination;
  private AtomicLong prunedCount;
  private AtomicLong exploredCount;

  private final PathFinder pathFinder;
  private final PatheryMap map;

  @Override
  public OptimalSolution findOptimalSolution() {
    ExecutorService executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() - 1);

    this.currentBestWallCombination = null;
    this.prunedCount = new AtomicLong();
    this.exploredCount = new AtomicLong();
    this.currentLongestPathLength = Integer.MIN_VALUE;

    int totalPositions = map.getWidth() * map.getHeight();
    Queue<WallCombination> wallCombinations = new LinkedList<>();
    wallCombinations.add(SetWallCombination.EMPTY);

    while (!wallCombinations.isEmpty()) {
      WallCombination walls = wallCombinations.poll();
      for (int positionIndex = 0; positionIndex < totalPositions; positionIndex++) {
        int x = positionIndex % map.getWidth();
        int y = positionIndex / map.getWidth();

        if (map.getTile(x, y) != TileType.OPEN) {
          continue;
        }

        Position position = Position.of(x, y);
        WallCombination newWalls = walls.add(position);

        executorService.submit(() -> this.exploreWallCombination(map, newWalls));

        if (newWalls.getWallCount() < map.getMaxWalls()) {
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

    if (this.currentBestWallCombination == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.currentBestWallCombination.getWalls());
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
      WallCombination walls
  ) {
    try {
      this.exploredCount.incrementAndGet();
      int pathLength = this.pathFinder.findCompletePath(map, walls).size();
      this.updateBestPath(pathLength, walls);
    } catch (NoPathException e) {
      this.prunedCount.incrementAndGet();
    }
  }


  private synchronized void updateBestPath(int pathLength, WallCombination walls) {
    if (pathLength > this.currentLongestPathLength) {
      this.currentLongestPathLength = pathLength;
      this.currentBestWallCombination = walls;
    }
  }

}
