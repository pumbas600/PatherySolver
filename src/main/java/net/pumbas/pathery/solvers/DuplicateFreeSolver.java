package net.pumbas.pathery.solvers;

import java.util.List;
import java.util.Stack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.exceptions.NoSolutionException;
import net.pumbas.pathery.models.BitSetWallCombination;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.models.WallCombination;
import net.pumbas.pathery.pathfinding.PathFinder;

/**
 * An evolution of {@link net.pumbas.pathery.solvers.EfficientSolver} that avoids creating duplicate
 * wall combinations. A DFS search tree is used to explore the possible combinations as the tree
 * is very wide, but not very deep. At any point in time the search tree should be approximately
 * (walls * average path length) in size.
 * 
 * Credit for this algorithm largely goes to Donut the 1st (https://github.com/Donut-the-1st).
 */
@RequiredArgsConstructor
public class DuplicateFreeSolver implements TreeSolver<Stack<DuplicateFreeSolver.SearchTreeNode>> {

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
  
  private final PathFinder pathFinder;
  private final PatheryMap map;

  @Override
  public OptimalSolution findOptimalSolution() {
    this.currentBestWallCombination = null;
    this.prunedCount = 0;
    this.exploredCount = 0;
    this.currentLongestPathLength = Integer.MIN_VALUE;

    final Stack<SearchTreeNode> stack = this.getInitialTree(map);

    while (!stack.isEmpty()) {
      this.expandTree(stack, map);
    }

    
    System.out.println("%d nodes explored. %d nodes pruned".formatted(this.exploredCount, this.prunedCount));
    
    if (this.currentBestWallCombination == null) {
      throw new NoSolutionException(
          "There is no valid solution for this map using all %d walls".formatted(
              map.getMaxWalls()));
    }

    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.currentBestWallCombination.getWalls());
  }

  @Override
  public Stack<SearchTreeNode> getInitialTree(final PatheryMap map) {
    final Stack<SearchTreeNode> stack = new Stack<>();
    stack.push(new SearchTreeNode(BitSetWallCombination.empty(map), 0));
    return stack;
  }

  @Override
  public void expandTree(final Stack<SearchTreeNode> tree, final PatheryMap map) {
    final SearchTreeNode node = tree.pop();
    this.exploredCount++;

    try {
      final List<Position> path = this.pathFinder.findCompletePath(map, node.wallCombination());
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
          tree.push(new SearchTreeNode(newWallCombination, index));
        }
      }
    } catch (NoPathException e) {
      this.prunedCount++;
    }
  }
  
}
