package net.pumbas.pathery.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;

@RequiredArgsConstructor
public class DijkstraPathFinder implements PathFinder {

  private final PatheryMap map;

  @Override
  public List<Position> findPath(
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

      if (endPositions.contains(position)) {
        return this.buildPath(pathNode);
      }

      for (Position neighbour : pathNode.getNeighbours()) {
        if (!this.map.isWithinBounds(neighbour)) {
          continue;
        }

        TileType tileType = this.map.getTile(neighbour);
        if (tileType.isBlocked() || walls.contains(neighbour) || visited.contains(neighbour)) {
          continue;
        }

        queue.add(new PathNode(neighbour, pathNode.getPathLength() + 1, pathNode));
        visited.add(neighbour);
      }
    }

    return Collections.emptyList();
  }

  private List<Position> buildPath(PathNode endNode) {
    PathNode currentNode = endNode;
    List<Position> path = new ArrayList<>();

    while (currentNode != null) {
      path.add(currentNode.getPosition());
      currentNode = currentNode.getPrevNode();
    }

    Collections.reverse(path);
    return path;
  }

  @Getter
  @AllArgsConstructor
  @RequiredArgsConstructor
  private static class PathNode implements Comparable<PathNode> {

    private final Position position;
    private final int pathLength;
    private PathNode prevNode;

    @Override
    public int compareTo(PathNode pathNode) {
      return Integer.compare(this.pathLength, pathNode.pathLength);
    }

    public List<Position> getNeighbours() {
      return List.of(
          this.position.add(Position.UP),
          this.position.add(Position.RIGHT),
          this.position.add(Position.DOWN),
          this.position.add(Position.LEFT)
      );
    }

  }

}
