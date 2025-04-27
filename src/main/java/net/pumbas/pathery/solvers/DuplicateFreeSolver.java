package net.pumbas.pathery.solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PositionBitSet;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
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
  public void expandTree(final Stack<SearchTreeNode> tree) {
    final SearchTreeNode node = tree.pop();
    this.exploredCount++;

    try {
      final List<Position> path = this.pathFinder.findCompletePath(this.map, node.wallCombination());
      final int pathLength = path.size();

      if (pathLength > this.currentLongestPathLength) {
        this.currentLongestPathLength = pathLength;
        this.currentBestWallCombination = node.wallCombination();
      }

      if (node.wallCombination().getCount() < this.map.getMaxWalls()) {
        final List<Position> unbannedPositions = this.findUnbannedPositions(path, node.bannedPositions());
        PositionSet newBannedPositions = node.bannedPositions();

        for (final Position unbannedPosition : unbannedPositions) {
          final PositionSet newWallCombination = node.wallCombination().add(unbannedPosition);
          tree.push(new SearchTreeNode(newWallCombination, newBannedPositions));

          /* 
           * We don't want sibling nodes to try and explore this position as it will create 
           * duplicates.
           */
          newBannedPositions = newBannedPositions.add(unbannedPosition);
        }
      }
    } catch (NoPathException e) {
      this.prunedCount++;
    }
  }

  private List<Position> findUnbannedPositions(
      final List<Position> path, final PositionSet bannedPositions) {
    final List<Position> unbannedPositions = new ArrayList<>(path.size());
    for (Position position : path) {
      if (this.map.getTile(position) == TileType.OPEN && !bannedPositions.contains(position)) {
        unbannedPositions.add(position);
      }
    }

    return unbannedPositions;
  }
  
}
