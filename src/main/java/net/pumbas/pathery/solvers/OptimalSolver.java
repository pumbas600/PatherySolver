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

@Getter
public class OptimalSolver implements Solver {

  private long prunedCount;
  private long exploredCount;
  private int currentMinimumPathLength;
  private Set<Position> bestWalls;

  @Override
  public OptimalSolution findOptimalSolution(PatheryMap map) {
    PathFinder pathFinder = PathFinderFactory.getPathFinder(map);
    this.bestWalls = null;
    this.currentMinimumPathLength = Integer.MIN_VALUE;

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
      this.exploreWallCombinations(map, wallCombinations, pathFinder, position);
    }

    if (this.bestWalls == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return new OptimalSolution(this.currentMinimumPathLength, this.bestWalls);
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
        if (pathLength > this.currentMinimumPathLength) {
          this.currentMinimumPathLength = pathLength;
          this.bestWalls = newWalls;
        }
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
}
