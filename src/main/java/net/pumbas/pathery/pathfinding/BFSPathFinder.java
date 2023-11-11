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
      queue.add(startPosition);
      parents.put(startPosition, null);
    }

    while (!queue.isEmpty()) {
      Position currentPosition = queue.poll();

      if (endPositions.contains(currentPosition)) {
        return this.buildPath(currentPosition, parents);
      }

      for (Position neighbour : currentPosition.getNeighbours()) {
        if (!map.isWithinBounds(neighbour)) {
          continue;
        }

        if (map.getTile(neighbour).isBlocked()
            || parents.containsKey(neighbour)
            || walls.contains(neighbour)) {
          continue;
        }

        parents.put(neighbour, currentPosition);

        if (endPositions.contains(currentPosition)) {
          return this.buildPath(currentPosition, parents);
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
