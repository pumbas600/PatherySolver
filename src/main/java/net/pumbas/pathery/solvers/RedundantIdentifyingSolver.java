package net.pumbas.pathery.solvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PositionBitSet;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.PositionSet;

/**
 * An evolution of {@link net.pumbas.pathery.solvers.DuplicateFreeSolver}, however, it removes walls
 * that block paths that are now already blocked by another wall. This does increase the 
 * search space as it create a duplicate every time, meaning it takes longer to prove it's the most
 * optimal solution but it also causes it to converge on a better solution faster. 
 * 
 * Credit for this algorithm largely goes to Donut the 1st (https://github.com/Donut-the-1st).
 */
public class RedundantIdentifyingSolver extends AbstractSolver implements TreeSolver<Stack<RedundantIdentifyingSolver.SearchTreeNode>> {

  public RedundantIdentifyingSolver(final PatheryMap map) {
    super(map);
  }

  public record BlockedPath(PositionSet path, Position blockedAt) {
  }

  public record SearchTreeNode(
    PositionSet wallCombination, PositionSet bannedPositions, List<BlockedPath> blockedPaths) {
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

    stack.push(new SearchTreeNode(emptySet, emptySet, Collections.emptyList()));
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

      final int currentWallCount = node.wallCombination().getCount();
      if (currentWallCount < this.map.getMaxWalls()) {
        final List<Position> unbannedPositions = this.findUnbannedPositions(path, node.bannedPositions());
        final PositionSet pathPositionSet = PositionBitSet.of(this.map, path);

        /* 
         * Add positions to the stack in reverse order so we expand the tree from the start of the 
         * path, rather than the end. 
         */
        for (int index = unbannedPositions.size() - 1; index >= 0; index--) {
          final Position unbannedPosition = unbannedPositions.get(index);

          final List<BlockedPath> newBlockedPaths = new ArrayList<>(currentWallCount + 1);
          newBlockedPaths.add(new BlockedPath(pathPositionSet, unbannedPosition));

          PositionSet newWallCombination = node.wallCombination().add(unbannedPosition);

          for (final BlockedPath blockedPath : node.blockedPaths()) {
            /* Dont include existing walls that have become redundant. */
            if (blockedPath.path().contains(unbannedPosition)) {
              newWallCombination = newWallCombination.remove(blockedPath.blockedAt);
            } else {
              newBlockedPaths.add(blockedPath);
            }
          }

          /* 
           * We don't want to explore positions that will be explored by sibling nodes first.
           */
          final PositionSet newBannedPositions = node.bannedPositions().addAll(unbannedPositions.subList(0, index));
          tree.push(new SearchTreeNode(newWallCombination, newBannedPositions, newBlockedPaths));
        }
      }
    } catch (NoPathException e) {
      this.prunedCount++;
    }

    return isBetterSolution;
  }
  
}
