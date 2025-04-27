package net.pumbas.pathery.solvers;

import java.util.List;
import java.util.Stack;

import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PositionBitSet;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.PositionSet;

/**
 * An evolution of {@link net.pumbas.pathery.solvers.EfficientSolver} that avoids creating duplicate
 * wall combinations. A DFS search tree is used to explore the possible combinations as the tree
 * is very wide, but not very deep. At any point in time the search tree should be approximately
 * (walls * average path length) in size.
 * 
 * Credit for this algorithm largely goes to Donut the 1st (https://github.com/Donut-the-1st).
 */
public class DuplicateFreeSolver extends AbstractSolver implements TreeSolver<Stack<DuplicateFreeSolver.SearchTreeNode>> {

  public DuplicateFreeSolver(final PatheryMap map) {
    super(map);
  }

  public record SearchTreeNode(PositionSet wallCombination, PositionSet bannedPositions) {
  }

  @Override
  public OptimalSolution findOptimalSolution() {
    this.resetMetrics();
    final Stack<SearchTreeNode> stack = this.getInitialTree();

    while (!stack.isEmpty()) {
      this.expandTree(stack);
    }

    System.out.println("%d nodes explored. %d nodes pruned".formatted(this.exploredCount, this.prunedCount));
    
    return OptimalSolution.fromLongestPath(this.currentLongestPathLength, this.currentBestWallCombination);
  }

  @Override
  public Stack<SearchTreeNode> getInitialTree() {
    final Stack<SearchTreeNode> stack = new Stack<>();
    final PositionBitSet emptySet = PositionBitSet.empty(this.map);

    stack.push(new SearchTreeNode(emptySet, emptySet));
    return stack;
  }

  @Override
  public boolean expandTree(final Stack<SearchTreeNode> tree) {
    final SearchTreeNode node = tree.pop();
    boolean isBetterSolution = false;
    this.exploredCount++;

    try {
      final List<Position> path = this.pathFinder.findCompletePath(this.map, node.wallCombination());
      final int pathLength = path.size();

      if (pathLength > this.currentLongestPathLength) {
        isBetterSolution = true;
        this.currentLongestPathLength = pathLength;
        this.currentBestWallCombination = node.wallCombination();
      }

      if (node.wallCombination().getCount() < this.map.getMaxWalls()) {
        final List<Position> unbannedPositions = this.findUnbannedPositions(path, node.bannedPositions());

        /* 
         * Add positions to the stack in reverse order so we expand the tree from the start of the 
         * path, rather than the end. 
         */
        for (int index = unbannedPositions.size() - 1; index >= 0; index--) {
          final Position unbannedPosition = unbannedPositions.get(index);
          final PositionSet newWallCombination = node.wallCombination().add(unbannedPosition);

          /* 
           * We don't want to explore positions that will be explored by sibling nodes first.
           */
          final PositionSet newBannedPositions = node.bannedPositions().addAll(unbannedPositions.subList(0, index));
          tree.push(new SearchTreeNode(newWallCombination, newBannedPositions));
        }
      }
    } catch (NoPathException e) {
      this.prunedCount++;
    }

    return isBetterSolution;
  }
  
}
