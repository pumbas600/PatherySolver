package net.pumbas.pathery.solvers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.exceptions.NoSolutionException;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.models.WallCombination;
import net.pumbas.pathery.pathfinding.PathFinder;
import net.pumbas.pathery.pathfinding.PathFinderFactory;

/**
 * This solver attempts two key things:
 * 
 * <ol>
 *   <li>When consider new places to put a wall, it can only ever be on a tile used in the current 
 *       path as otherwise the path length wont change.</li>
 *   <li>All tested wall combinations should be stored to prevent duplicates being found.</li>
 * </ol>
 */
public class EfficientSolver implements Solver {

  @Getter
  private long prunedCount;
  @Getter
  private long exploredCount;
  @Getter
  private int currentLongestPathLength;
  private WallCombination bestWallCombination;

  @Override
  public OptimalSolution findOptimalSolution(final PatheryMap map) {
    final PathFinder pathFinder = PathFinderFactory.getPathFinder(map);
    this.bestWallCombination = null;
    this.prunedCount = 0;
    this.exploredCount = 0;
    this.currentLongestPathLength = Integer.MIN_VALUE;

    final Set<String> exploredWallCombinationIds = new HashSet<>();
    final List<WallCombination> openWallCombinations = new ArrayList<>();
    final List<WallCombination> newWallCombinations = new ArrayList<>();
    
    openWallCombinations.add(WallCombination.EMPTY);

    while (!openWallCombinations.isEmpty()) {
      for (final WallCombination wallCombination : openWallCombinations) {
        try {
          this.exploredCount++;
          final List<Position> path = pathFinder.findCompletePath(map, wallCombination.getWalls());
          final int pathLength = path.size();

          if (pathLength > this.currentLongestPathLength) {
            this.currentLongestPathLength = pathLength;
            this.bestWallCombination = wallCombination;
          }

          if (wallCombination.getWallCount() < map.getMaxWalls()) {
            for (final Position position : path) {
              // There are some special tiles that can be included in the path, but never be walls.
              if (map.getTile(position) != TileType.OPEN) {
                continue;
              }

              final WallCombination newWallCombination = wallCombination.add(position);

              if (!exploredWallCombinationIds.contains(newWallCombination.getUniqueId())) {
                exploredWallCombinationIds.add(newWallCombination.getUniqueId());
                newWallCombinations.add(newWallCombination);
              } else {
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
        "%d new combinations found. %d unique walls explored. Current longest path: %d"
          .formatted(newWallCombinations.size(), exploredWallCombinationIds.size(), currentLongestPathLength)
      );
      openWallCombinations.clear();
      openWallCombinations.addAll(newWallCombinations);
      newWallCombinations.clear();

      // All the wall combinations in the open set are the same size, which means we can clear the
      // explored set because we will never need to check if we've explored a combination with fewer
      // walls.
      exploredWallCombinationIds.clear();
    }
    
    if (this.bestWallCombination == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return new OptimalSolution(this.currentLongestPathLength, this.bestWallCombination.getWalls());
  }
}
