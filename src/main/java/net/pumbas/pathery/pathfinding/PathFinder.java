package net.pumbas.pathery.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.PositionSet;

public interface PathFinder {

  /**
   * Finds a complete shortest path from one of the start tiles to one of the end tiles by going
   * through each of the checkpoints in order. This is achieved by finding a path from a start tile
   * to the first checkpoint, and then from that checkpoint to the next until eventually, it finds a
   * path from the latest checkpoint to a finish tile. The combination of all these paths forms the
   * complete path.
   *
   * @param map   The {@link PatheryMap} to perform the pathfinding on
   * @param walls The {@link Position}s of the walls
   * @return The shortest complete path from a start tile to an end tile
   * @throws NoPathException If there is no valid path
   * @see #findPath(PatheryMap, Set, Set, Set)
   */
  default List<Position> findCompletePath(PatheryMap map, PositionSet walls)
      throws NoPathException {
    List<Position> completePath = new ArrayList<>();
    Set<Position> startPositions = map.getStartTiles();

    List<Set<Position>> destinations = map.getCheckpoints()
        .stream()
        .map(Set::of)
        .collect(Collectors.toList());
    destinations.add(map.getFinishTiles());

    for (Set<Position> endPositions : destinations) {
      List<Position> path = this.findPath(map, walls, startPositions, endPositions);

      if (!completePath.isEmpty()) {
        // The first element is the last element of the previous path. We remove it to
        // prevent duplicate positions in the resulting path
        path.remove(0);
      }

      completePath.addAll(path);
      startPositions = endPositions;
    }

    return completePath;
  }

  /**
   * Finds the shortest path between any start position and an end position for the given
   * {@link Set} of walls. If there is no valid path then an {@link NoPathException} will be
   * thrown.
   * <p>
   * When there are multiple paths of the same length, the path should prioritise the following
   * order so that it matches the <a href="https://www.pathery.com/faq">Pathery pathfinding
   * algorithm</a>: UP, RIGHT, DOWN, LEFT.
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
      PositionSet walls,
      Set<Position> startPositions,
      Set<Position> endPositions
  ) throws NoPathException;

}
