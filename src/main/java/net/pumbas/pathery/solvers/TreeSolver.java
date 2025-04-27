package net.pumbas.pathery.solvers;

import java.util.List;

/**
 * An interface for a search tree based solver. Implementing this interface allows the solver to be
 * parallelised.
 * 
 * @param <TTree> The data structure used to represent the search tree.
 */
public interface TreeSolver<TTree extends List<?>> extends Solver {

  /**
   * @return The initial tree to expand upon.
   */
  TTree getInitialTree();

  /**
   * Expands the tree once by exploring the next node in the tree.
   *  
   * @param tree The {@link TTree} to expand
   * @return {@code true} if a better solution was found, {@code false} otherwise.
   */
  boolean expandTree(final TTree tree);
}
