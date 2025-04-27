package net.pumbas.pathery.solvers;

import java.util.List;
import java.util.Stack;

import lombok.Getter;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.exceptions.NoSolutionException;
import net.pumbas.pathery.models.BitSetWallCombination;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.models.WallCombination;
import net.pumbas.pathery.pathfinding.PathFinder;
import net.pumbas.pathery.pathfinding.PathFinderFactory;

/**
 * An evolution of {@link net.pumbas.pathery.solvers.EfficientSolver} that avoids creating duplicate
 * wall combinations. A DFS search tree is used to explore the possible combinations as the tree
 * is very wide, but not very deep. At any point in time the search tree should be approximately
 * (walls * average path length) in size.
 * 
 * Credit for this algorithm largely goes to Donut the 1st (https://github.com/Donut-the-1st).
 */
public class DuplicateFreeSolver implements Solver {

  public record SearchTreeNode(WallCombination wallCombination, int startIndex) {
  } 

  @Getter
  private long prunedCount;
  @Getter
  private long exploredCount;
  @Getter
  private int currentLongestPathLength;
  @Getter
  private WallCombination currentBestWallCombination;

  @Override
  public OptimalSolution findOptimalSolution(PatheryMap map) {
    final PathFinder pathFinder = PathFinderFactory.getPathFinder(map);
    this.currentBestWallCombination = null;
    this.prunedCount = 0;
    this.exploredCount = 0;
    this.currentLongestPathLength = Integer.MIN_VALUE;

    final Stack<SearchTreeNode> stack = new Stack<>();
    stack.push(new SearchTreeNode(BitSetWallCombination.empty(map), 0));

    while (!stack.isEmpty()) {
      final SearchTreeNode node = stack.pop();
      this.exploredCount++;

      try {
        final List<Position> path = pathFinder.findCompletePath(map, node.wallCombination());
        final int pathLength = path.size();

        if (pathLength > this.currentLongestPathLength) {
          this.currentLongestPathLength = pathLength;
          this.currentBestWallCombination = node.wallCombination();
        }

        if (node.wallCombination().getWallCount() < map.getMaxWalls()) {
          /* 
           * We only consider nodes from the start index as we know the rest have been explored.
           */
          for (int index = node.startIndex(); index < path.size(); index++) {
            final Position position = path.get(index);
            if (map.getTile(position) != TileType.OPEN) {
              continue;
            }

            final WallCombination newWallCombination = node.wallCombination().add(position);
            stack.push(new SearchTreeNode(newWallCombination, index));
          }
        }
      } catch (NoPathException e) {
        this.prunedCount++;
      }
    }

    
    System.out.println("%d nodes explored. %d nodes pruned".formatted(this.exploredCount, this.prunedCount));
    
    if (this.currentBestWallCombination == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.currentBestWallCombination.getWalls());
  }
  
}
