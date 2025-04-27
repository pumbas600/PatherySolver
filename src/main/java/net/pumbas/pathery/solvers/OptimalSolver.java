package net.pumbas.pathery.solvers;

import java.util.HashSet;
import java.util.Set;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.PositionHashSet;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.models.PositionSet;

public class OptimalSolver extends AbstractSolver {

  public OptimalSolver(final PatheryMap map) {
    super(map);
  }

  @Override
  public OptimalSolution findOptimalSolution() {
    this.resetMetrics();

    Set<PositionSet> wallCombinations = new HashSet<>();
    wallCombinations.add(PositionHashSet.EMPTY);

    int totalPositions = map.getWidth() * map.getHeight();

    for (int positionIndex = 0; positionIndex < totalPositions; positionIndex++) {
      int x = positionIndex % map.getWidth();
      int y = positionIndex / map.getWidth();

      if (map.getTile(x, y) != TileType.OPEN) {
        continue;
      }

      Position position = Position.of(x, y);
      this.exploreWallCombinations(map, wallCombinations, position);
    }

    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.currentBestWallCombination);
  }

  private void exploreWallCombinations(
      PatheryMap map,
      Set<PositionSet> wallCombinations,
      Position position
  ) {
    Set<PositionSet> newWallCombinations = new HashSet<>();

    for (PositionSet walls : wallCombinations) {
      PositionSet newWallCombination = walls.add(position);

      try {
        this.exploredCount++;
        int pathLength = this.pathFinder.findCompletePath(map, newWallCombination).size();
        if (pathLength > this.currentLongestPathLength) {
          this.currentLongestPathLength = pathLength;
          this.currentBestWallCombination = newWallCombination;
        }
      } catch (NoPathException e) {
        this.prunedCount++;
        continue;
      }

      if (newWallCombination.getCount() < map.getMaxWalls()) {
        // Add it to the new set so that we don't start iterating over it in this for loop.
        newWallCombinations.add(newWallCombination);
      }
    }

    wallCombinations.addAll(newWallCombinations);
  }
}
