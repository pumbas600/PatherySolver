package net.pumbas.pathery.pathfinding;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;

public class BFSPathFinder implements PathFinder {

  @Override
  public List<Position> findPath(
      PatheryMap map,
      Set<Position> walls,
      Set<Position> startPositions,
      Set<Position> endPositions
  ) throws NoPathException {
    Map<Position, Position> parents = new HashMap<>();
    Queue<Position> queue = new ArrayDeque<>();

    for (Position startPosition : startPositions) {
      if (endPositions.contains(startPosition)) {
        // This won't happen due to the way the PatheryMap is constructed, but is included for
        // completeness. Note that an ArrayList is specifically returned so that its mutable.
        return new ArrayList<>(List.of(startPosition));
      }

      queue.add(startPosition);
      parents.put(startPosition, null);
    }

    while (!queue.isEmpty()) {
      Position currentPosition = queue.poll();

      for (Position neighbour : map.getNeighbours(currentPosition, walls)) {
        if (parents.containsKey(neighbour)) {
          continue;
        }

        parents.put(neighbour, currentPosition);
        if (endPositions.contains(neighbour)) {
          return this.buildPath(neighbour, parents);
        } else {
          queue.add(neighbour);
        }
      }
    }

    throw new NoPathException(String.format(
        "There is no valid path between the start positions (%s) and the end positions (%s)",
        startPositions, endPositions));
  }


  private List<Position> buildPath(Position endPosition, Map<Position, Position> parents) {
    List<Position> path = new ArrayList<>();
    Position currentPosition = endPosition;
    Position parentPosition;

    do {
      parentPosition = parents.get(currentPosition);
      path.add(currentPosition);
      currentPosition = parentPosition;
    } while (parentPosition != null);

    Collections.reverse(path);
    return path;
  }

}
