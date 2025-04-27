package net.pumbas.pathery.solvers;

import java.util.List;

import net.pumbas.pathery.models.PatheryMap;

/**
 * An interface for a search tree based solver. Implementing this interface allows the solver to be
 * parallelised.
 * 
 * @param <TTree> The data structure used to represent the search tree.
 */
public interface TreeSolver<TTree extends List<?>> extends Solver {

  TTree getInitialTree(final PatheryMap map);

  /**
   * Expands the tree once by exploring the next node in the tree. 
   * {@code tree}.
   *  
   * @param tree The {@link TTree} to expand
   * @param map The {@link PatheryMap} being solved
   */
  void expandTree(final TTree tree, final PatheryMap map);
}
