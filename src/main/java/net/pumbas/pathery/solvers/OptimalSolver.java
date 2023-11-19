package net.pumbas.pathery.solvers;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.exceptions.NoSolutionException;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.pathfinding.PathFinder;
import net.pumbas.pathery.pathfinding.PathFinderFactory;

public class OptimalSolver implements Solver {

  @Getter
  private long prunedCount;
  @Getter
  private long exploredCount;

  @Override
  public OptimalSolution findOptimalSolution(PatheryMap map) {
    PathFinder pathFinder = PathFinderFactory.getPathFinder(map);
    Set<Position> bestWalls = null;
    int maximumPathLength = Integer.MIN_VALUE;

    Set<Set<Position>> wallCombinations = new HashSet<>();
    Set<Set<Position>> temporaryNewWallCombinations = new HashSet<>();
    wallCombinations.add(new HashSet<>());

    int totalPositions = map.getWidth() * map.getHeight();

    for (int positionIndex = 0; positionIndex < totalPositions; positionIndex++) {
      int x = positionIndex % map.getWidth();
      int y = positionIndex / map.getWidth();

      if (map.getTile(x, y) != TileType.OPEN) {
        continue;
      }

      Position position = new Position(x, y);

      for (Set<Position> walls : wallCombinations) {
        Set<Position> newWalls = new HashSet<>(walls);
        newWalls.add(position);

        try {
          this.exploredCount++;
          int pathLength = pathFinder.findCompletePath(map, newWalls).size();
          if (pathLength > maximumPathLength) {
            maximumPathLength = pathLength;
            bestWalls = newWalls;
          }
        } catch (NoPathException e) {
          this.prunedCount++;
          continue;
        }

        if (newWalls.size() < map.getMaxWalls()) {
          // Add it to the temporary set so that we don't start iterating over it in this for loop.
          temporaryNewWallCombinations.add(newWalls);
        }
      }

      // Now that we've finished iterating over wallPositionCombinations, we can add the new subsets
      wallCombinations.addAll(temporaryNewWallCombinations);
      temporaryNewWallCombinations.clear();
    }

    if (bestWalls == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return new OptimalSolution(maximumPathLength, bestWalls);
  }
}
