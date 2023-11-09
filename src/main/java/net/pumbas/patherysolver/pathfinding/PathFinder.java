package net.pumbas.patherysolver.pathfinding;

import java.util.List;
import java.util.Set;
import net.pumbas.patherysolver.models.Position;

public interface PathFinder {

  /**
   * Finds the shortest path between a starting tile and a finish tile for the given {@link Set} of
   * walls . If there is no valid path then an empty list will be returned.
   *
   * @param walls          The {@link Position}s of the walls
   * @param startPositions The {@link Position}s a path can start from
   * @param endPositions   The {@link Position}s a path can end
   * @return The shortest path found, or an empty list if there is no valid path
   */
  List<Position> findPath(Set<Position> walls, Set<Position> startPositions,
      Set<Position> endPositions);

}
