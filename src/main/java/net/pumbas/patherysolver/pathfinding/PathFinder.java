package net.pumbas.patherysolver.pathfinding;

import net.pumbas.patherysolver.models.Position;
import net.pumbas.patherysolver.models.Solution;

public interface PathFinder {

  /**
   * Finds the shortest path between the two positions and returns its length. If there is no valid
   * path then {@code -1} is returned.
   *
   * @param solution The {@link Solution} defining the walls on the map
   * @param from     The starting {@link Position}
   * @param to       The ending {@link Position}
   * @return The length of the path between the two given positions, or {@code -1} if there is no
   * valid path between them
   */
  int getPathLength(Solution solution, Position from, Position to);

}
