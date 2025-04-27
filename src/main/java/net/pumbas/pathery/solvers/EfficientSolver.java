package net.pumbas.pathery.solvers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PositionBitSet;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.models.PositionSet;

/**
 * This solver attempts two key things:
 * 
 * <ol>
 *   <li>When considering new places to put a wall, it can only ever be on a tile used in the 
 *       current path as otherwise the path length wont change.</li>
 *   <li>All tested wall combinations should be stored to prevent duplicates being found.</li>
 * </ol>
 */
public class EfficientSolver extends AbstractSolver {

  public EfficientSolver(final PatheryMap map) {
    super(map);
  }

  @Override
  public OptimalSolution findOptimalSolution() {
    this.resetMetrics();

    final List<PositionSet> openWallCombinations = new ArrayList<>();
    final Set<PositionSet> newWallCombinations = new HashSet<>();
    
    openWallCombinations.add(PositionBitSet.empty(map));

    while (!openWallCombinations.isEmpty()) {
      for (final PositionSet wallCombination : openWallCombinations) {
        try {
          this.exploredCount++;
          final List<Position> path = pathFinder.findCompletePath(map, wallCombination);
          final int pathLength = path.size();

          if (pathLength > this.currentLongestPathLength) {
            this.currentLongestPathLength = pathLength;
            this.currentBestWallCombination = wallCombination;
          }

          if (wallCombination.getCount() < map.getMaxWalls()) {
            for (final Position position : path) {
              // There are some special tiles that can be included in the path, but never be walls.
              if (map.getTile(position) != TileType.OPEN) {
                continue;
              }

              final PositionSet newWallCombination = wallCombination.add(position);
              if (!newWallCombinations.add(newWallCombination)) {
                this.prunedCount++;
              }
            }
          }

        } catch (NoPathException e) {
          this.prunedCount++;
          continue;
        }
      }

      System.out.println(
          "%d new combinations found. %d combinations pruned. Current longest path: %d"
              .formatted(newWallCombinations.size(), this.prunedCount, currentLongestPathLength));
      openWallCombinations.clear();
      openWallCombinations.addAll(newWallCombinations);

      // All the wall combinations in the open set are the same size, which means we can clear them
      // because we will never need to check if we've explored a combination with fewer walls.
      newWallCombinations.clear();
    }

    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.currentBestWallCombination);
  }
}
