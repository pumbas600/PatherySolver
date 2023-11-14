package net.pumbas.pathery.solvers;

import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;

public interface Solver {
  
  /**
   * For a given {@link PatheryMap}, find the optimal placement of walls such that the minimum path
   * from a start tile to a finish tile via the checkpoints is as long as possible.
   *
   * @param map The {@link PatheryMap} to find the solution for
   * @return The {@link OptimalSolution} for this map
   */
  OptimalSolution findOptimalSolution(PatheryMap map);
}
