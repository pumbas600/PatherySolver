package net.pumbas.patherysolver.pathfinding;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import net.pumbas.patherysolver.models.PatheryMap;
import net.pumbas.patherysolver.models.Position;
import net.pumbas.patherysolver.models.TileType;

@RequiredArgsConstructor
public class DijkstraPathFinder implements PathFinder {

  private final PatheryMap map;

  @Override
  public int getPathLength(
      Set<Position> walls,
      Set<Position> startPositions,
      Set<Position> endPositions
  ) {
    Set<Position> visited = new HashSet<>();
    Queue<PathNode> queue = new PriorityQueue<>();

    for (Position startPosition : startPositions) {
      queue.add(new PathNode(startPosition, 0));
      visited.add(startPosition);
    }

    while (!queue.isEmpty()) {
      PathNode pathNode = queue.poll();
      Position position = pathNode.getPosition();
      int pathLength = pathNode.getPathLength();

      if (endPositions.contains(position)) {
        return pathLength;
      }

      for (Position neighbour : pathNode.getNeighbours()) {
        if (!this.map.isWithinBounds(neighbour)) {
          continue;
        }

        TileType tileType = this.map.getTile(neighbour);
        if (tileType.isBlocked() || walls.contains(neighbour) || visited.contains(neighbour)) {
          continue;
        }

        queue.add(new PathNode(neighbour, pathLength + 1));
        visited.add(neighbour);
      }
    }

    return PathFinder.NO_PATH;
  }

}
