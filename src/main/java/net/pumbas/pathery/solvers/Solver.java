package net.pumbas.pathery.solvers;

import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.WallCombination;

public interface Solver {

  /**
   * For a given {@link PatheryMap}, find the optimal placement of walls such that the minimum path
   * from a start tile to a finish tile via the checkpoints is as long as possible.
   *
   * @param map The {@link PatheryMap} to find the solution for
   * @return The {@link OptimalSolution} for this map
   */
  OptimalSolution findOptimalSolution(PatheryMap map);

  /**
   * Returns the number of wall combinations that have been pruned from the search space. A wall
   * combination is pruned if it does not have a valid path from a start tile to a finish tile via
   * the checkpoints.
   *
   * @return The number of wall combinations that have been pruned from the search space
   */
  long getPrunedCount();

  /**
   * Returns the number of wall combinations that have been explored. A wall combination is explored
   * if the shortest path from a start tile to a finish tile via the checkpoints is calculated.
   *
   * @return The number of wall combinations that have been explored
   */
  long getExploredCount();

  /**
   * Returns the current longest path length that has been found so far from all the wall
   * combinations that have been explored so far.
   *
   * @return The current longest path length that has been found
   */
  int getCurrentLongestPathLength();

  /**
   * Return the current wall combination that has been found to produce the longest path so far from
   * all the wall combinations that have been explored.
   *  
   * @return The current wall combination that has been found
   */
  WallCombination getCurrentBestWallCombination();
}
