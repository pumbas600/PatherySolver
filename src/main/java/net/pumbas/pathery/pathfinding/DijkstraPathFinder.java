package net.pumbas.pathery.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;

public class DijkstraPathFinder implements PathFinder {

  private static final int NOT_SEARCHED = 0;
  private static final int START = 1;

  @Override
  public List<Position> findPath(
      PatheryMap map,
      Set<Position> walls,
      Set<Position> startPositions,
      Set<Position> endPositions
  ) throws NoPathException {

    // This map is going to use 0 for not searched and 1 for the starting positions.
    int[][] costMap = new int[map.getWidth()][map.getHeight()];
    Queue<PathNode> queue = new PriorityQueue<>();

    for (Position startPosition : startPositions) {
      queue.add(new PathNode(startPosition, 1));
      costMap[startPosition.getX()][startPosition.getY()] = START;
    }

    while (!queue.isEmpty()) {
      PathNode pathNode = queue.poll();
      Position position = pathNode.getPosition();

      if (endPositions.contains(position)) {
        return this.buildPath(map, walls, startPositions, position, costMap);
      }

      for (Position neighbour : map.getNeighbours(position, walls)) {
        if (costMap[neighbour.getX()][neighbour.getY()] != NOT_SEARCHED) {
          continue;
        }

        int cost = pathNode.getPathLength() + 1;
        queue.add(new PathNode(neighbour, cost));
        costMap[neighbour.getX()][neighbour.getY()] = cost;
      }
    }

    throw new NoPathException(String.format(
        "There is no valid path between the start positions (%s) and the end positions (%s)",
        startPositions, endPositions));
  }

  private List<Position> buildPath(
      PatheryMap map,
      Set<Position> walls,
      Set<Position> startPositions,
      Position endPosition,
      int[][] costMap
  ) {
    List<Position> path = new ArrayList<>();
    Position currentPosition = this.findPathStartPosition(
        map, walls, startPositions, endPosition, costMap);

    while (true) {
      int currentCost = costMap[currentPosition.getX()][currentPosition.getY()];
      path.add(currentPosition);

      if (currentPosition.equals(endPosition)) {
        break;
      }

      currentPosition = this.findNeighbourWithCost(
          map, walls, currentPosition, costMap, currentCost + 1);
    }

    return path;
  }

  private Position findPathStartPosition(
      PatheryMap map,
      Set<Position> walls,
      Set<Position> startPositions,
      Position endPosition,
      int[][] costMap
  ) {
    if (startPositions.size() == 1) {
      return startPositions.iterator().next();
    }

    // Work backwards until we find which of the start positions was the optimal starting point
    Position currentPosition = endPosition;
    while (true) {
      final int currentCost = costMap[currentPosition.getX()][currentPosition.getY()];
      if (currentCost == START) {
        return currentPosition;
      }

      currentPosition = this.findNeighbourWithCost(
          map, walls, currentPosition, costMap, currentCost - 1);
    }
  }

  private Position findNeighbourWithCost(
      PatheryMap map,
      Set<Position> walls,
      Position position,
      int[][] costMap,
      int cost
  ) {
    return map.getNeighbours(position, walls)
        .stream()
        .filter(neighbour -> costMap[neighbour.getX()][neighbour.getY()] == cost)
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                String.format("There is no neighbour of %s with the cost of %s", position, cost)));
  }

  @Getter
  @RequiredArgsConstructor
  private static class PathNode implements Comparable<PathNode> {

    private final Position position;
    private final int pathLength;

    @Override
    public int compareTo(PathNode pathNode) {
      return Integer.compare(this.pathLength, pathNode.pathLength);
    }

  }

}
