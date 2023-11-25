package net.pumbas.pathery.solvers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
  private long prunedCount;
  @Getter
  private long exploredCount;
  @Getter
  private int currentLongestPathLength;
  private Set<Position> bestWalls;

  @Override
  public OptimalSolution findOptimalSolution(PatheryMap map) {
    ExecutorService executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() - 1);
    PathFinder pathFinder = PathFinderFactory.getPathFinder(map);

    this.currentLongestPathLength = Integer.MIN_VALUE;
    this.bestWalls = null;

    Set<Set<Position>> wallCombinations = new HashSet<>();
    wallCombinations.add(new HashSet<>());

    int totalPositions = map.getWidth() * map.getHeight();

    for (int positionIndex = 0; positionIndex < totalPositions; positionIndex++) {
      int x = positionIndex % map.getWidth();
      int y = positionIndex / map.getWidth();

      if (map.getTile(x, y) != TileType.OPEN) {
        continue;
      }

      Position position = new Position(x, y);
      executorService.submit(
          () -> this.exploreWallCombinations(map, wallCombinations, pathFinder, position));
    }

    if (this.bestWalls == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return new OptimalSolution(this.currentLongestPathLength, this.bestWalls);
  }

  private void exploreWallCombinations(
      PatheryMap map,
      Set<Set<Position>> wallCombinations,
      PathFinder pathFinder,
      Position position
  ) {
    Set<Set<Position>> newWallCombinations = new HashSet<>();

    for (Set<Position> walls : wallCombinations) {
      Set<Position> newWalls = new HashSet<>(walls);
      newWalls.add(position);

      try {
        this.exploredCount++;
        int pathLength = pathFinder.findCompletePath(map, newWalls).size();
        this.updateBestPath(pathLength, newWalls);
      } catch (NoPathException e) {
        this.prunedCount++;
        continue;
      }

      if (newWalls.size() < map.getMaxWalls()) {
        // Add it to the new set so that we don't start iterating over it in this for loop.
        newWallCombinations.add(newWalls);
      }
    }

    wallCombinations.addAll(newWallCombinations);
  }


  private synchronized void updateBestPath(int pathLength, Set<Position> walls) {
    if (pathLength > this.currentLongestPathLength) {
      this.currentLongestPathLength = pathLength;
      this.bestWalls = walls;
    }
  }

}
