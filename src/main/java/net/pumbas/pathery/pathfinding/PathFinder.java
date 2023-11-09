package net.pumbas.pathery.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;

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
  default List<Position> findCompletePath(PatheryMap map, Set<Position> walls)
      throws NoPathException {
    List<Position> completePath = new ArrayList<>();
    Set<Position> startPositions = map.getStartTiles();

    for (Position checkpoint : map.getCheckpoints()) {
      Set<Position> endPositions = Set.of(checkpoint);
      completePath.addAll(this.findPath(map, walls, startPositions, endPositions));
      startPositions = endPositions;
    }

    completePath.addAll(this.findPath(map, walls, startPositions, map.getFinishTiles()));
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
      Set<Position> walls,
      Set<Position> startPositions,
      Set<Position> endPositions
  ) throws NoPathException;

}
