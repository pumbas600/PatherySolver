package net.pumbas.pathery.solvers;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.exceptions.NoSolutionException;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.SetWallCombination;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.models.WallCombination;
import net.pumbas.pathery.pathfinding.PathFinder;
import net.pumbas.pathery.pathfinding.PathFinderFactory;

public class OptimalSolver implements Solver {

  @Getter
  private long prunedCount;
  @Getter
  private long exploredCount;
  @Getter
  private int currentLongestPathLength;
  private WallCombination bestWalls;

  @Override
  public OptimalSolution findOptimalSolution(PatheryMap map) {
    PathFinder pathFinder = PathFinderFactory.getPathFinder(map);
    this.bestWalls = null;
    this.prunedCount = 0;
    this.exploredCount = 0;
    this.currentLongestPathLength = Integer.MIN_VALUE;

    Set<WallCombination> wallCombinations = new HashSet<>();
    wallCombinations.add(SetWallCombination.EMPTY);

    int totalPositions = map.getWidth() * map.getHeight();

    for (int positionIndex = 0; positionIndex < totalPositions; positionIndex++) {
      int x = positionIndex % map.getWidth();
      int y = positionIndex / map.getWidth();

      if (map.getTile(x, y) != TileType.OPEN) {
        continue;
      }

      Position position = Position.of(x, y);
      this.exploreWallCombinations(map, wallCombinations, pathFinder, position);
    }

    if (this.bestWalls == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.bestWalls.getWalls());
  }

  private void exploreWallCombinations(
      PatheryMap map,
      Set<WallCombination> wallCombinations,
      PathFinder pathFinder,
      Position position
  ) {
    Set<WallCombination> newWallCombinations = new HashSet<>();

    for (WallCombination walls : wallCombinations) {
      WallCombination newWallCombination = walls.add(position);

      try {
        this.exploredCount++;
        int pathLength = pathFinder.findCompletePath(map, newWallCombination).size();
        if (pathLength > this.currentLongestPathLength) {
          this.currentLongestPathLength = pathLength;
          this.bestWalls = newWallCombination;
        }
      } catch (NoPathException e) {
        this.prunedCount++;
        continue;
      }

      if (newWallCombination.getWallCount() < map.getMaxWalls()) {
        // Add it to the new set so that we don't start iterating over it in this for loop.
        newWallCombinations.add(newWallCombination);
      }
    }

    wallCombinations.addAll(newWallCombinations);
  }
}
