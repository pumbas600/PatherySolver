package net.pumbas.pathery.pathfinding;

import java.util.List;
import java.util.Set;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;

public interface PathFinder {

  /**
   * Finds the shortest path between any start position and an end position for the given
   * {@link Set} of walls. If there is no valid path then an {@link NoPathException} will be
   * thrown.
   *
   * @param map            The {@link PatheryMap} to perform pathfinding on
   * @param walls          The {@link Position}s of the walls
   * @param startPositions The {@link Position}s a path can start from
   * @param endPositions   The {@link Position}s a path can end
   * @return The shortest path found
   * @throws NoPathException if there is no valid path
   */
  List<Position> findPath(
      PatheryMap map,
      Set<Position> walls,
      Set<Position> startPositions,
      Set<Position> endPositions
  ) throws NoPathException;

}
