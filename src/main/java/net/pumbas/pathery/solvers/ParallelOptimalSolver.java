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
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.PositionHashSet;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.models.PositionSet;
import net.pumbas.pathery.pathfinding.PathFinder;

@RequiredArgsConstructor
public class ParallelOptimalSolver implements Solver {

  @Getter
  private int currentLongestPathLength;
  @Getter
  private PositionSet currentBestWallCombination;
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
    Queue<PositionSet> wallCombinations = new LinkedList<>();
    wallCombinations.add(PositionHashSet.EMPTY);

    while (!wallCombinations.isEmpty()) {
      PositionSet walls = wallCombinations.poll();
      for (int positionIndex = 0; positionIndex < totalPositions; positionIndex++) {
        int x = positionIndex % map.getWidth();
        int y = positionIndex / map.getWidth();

        if (map.getTile(x, y) != TileType.OPEN) {
          continue;
        }

        Position position = Position.of(x, y);
        PositionSet newWalls = walls.add(position);

        executorService.submit(() -> this.exploreWallCombination(map, newWalls));

        if (newWalls.getCount() < map.getMaxWalls()) {
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

    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.currentBestWallCombination);
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
      PositionSet walls
  ) {
    try {
      this.exploredCount.incrementAndGet();
      int pathLength = this.pathFinder.findCompletePath(map, walls).size();
      this.updateBestPath(pathLength, walls);
    } catch (NoPathException e) {
      this.prunedCount.incrementAndGet();
    }
  }


  private synchronized void updateBestPath(int pathLength, PositionSet walls) {
    if (pathLength > this.currentLongestPathLength) {
      this.currentLongestPathLength = pathLength;
      this.currentBestWallCombination = walls;
    }
  }

}
