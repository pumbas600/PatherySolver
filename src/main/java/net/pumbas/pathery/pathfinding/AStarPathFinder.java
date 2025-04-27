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
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.WallCombination;

public class AStarPathFinder implements PathFinder {

  @Override
  public List<Position> findPath(
      PatheryMap map,
      WallCombination walls,
      Set<Position> startPositions,
      Set<Position> endPositions
  ) throws NoPathException {
    Set<Position> closedSet = new HashSet<>();
    Queue<Node> queue = new PriorityQueue<>();

    for (Position startPosition : startPositions) {
      if (endPositions.contains(startPosition)) {
        // This won't happen due to the way the PatheryMap is constructed, but is included for
        // completeness.
        return List.of(startPosition);
      }

      int heuristicCost = this.calculateHeuristicCost(startPosition, endPositions);
      queue.add(new Node(startPosition, heuristicCost, 0));
      closedSet.add(startPosition);
    }

    while (!queue.isEmpty()) {
      Node currentNode = queue.poll();
      int minPathCost = currentNode.getMinPathCost() + 1;

      for (Position neighbour : map.getNeighbours(currentNode.getPosition(), walls)) {
        if (closedSet.contains(neighbour)) {
          continue;
        }

        int heuristicCost = this.calculateHeuristicCost(neighbour, endPositions);
        Node neighbourNode = new Node(neighbour, currentNode, heuristicCost, minPathCost);

        if (endPositions.contains(neighbour)) {
          return this.buildPath(neighbourNode);
        } else {
          queue.add(neighbourNode);
          closedSet.add(neighbour);
        }
      }
    }

    throw new NoPathException(
        "There is no valid path between the start positions (%s) and the end positions (%s)"
            .formatted(startPositions, endPositions));
  }

  private List<Position> buildPath(Node endNode) {
    Node currentNode = endNode;
    List<Position> path = new ArrayList<>();

    while (currentNode != null) {
      path.add(currentNode.getPosition());
      currentNode = currentNode.getParent();
    }

    Collections.reverse(path);
    return path;
  }

  private int calculateHeuristicCost(Position from, Set<Position> endPositions) {
    int minHeuristicCost = Integer.MAX_VALUE;

    for (Position endPosition : endPositions) {
      int heuristicCost = this.calculateHeuristicCost(from, endPosition);
      if (heuristicCost < minHeuristicCost) {
        minHeuristicCost = heuristicCost;
      }
    }

    return minHeuristicCost;
  }

  private int calculateHeuristicCost(Position from, Position to) {
    int xDiff = Math.abs(from.getX() - to.getX());
    int yDiff = Math.abs(from.getY() - to.getY());
    int heuristicCost = xDiff + yDiff;
    if (xDiff != 0 && yDiff != 0) {
      heuristicCost--; // This is to account for the corner of the direct path being counted twice
    }

    return heuristicCost;
  }

  @Getter
  @AllArgsConstructor
  private static class Node implements Comparable<Node> {

    private final Position position;
    private final Node parent;
    private final int heuristicCost;
    private final int minPathCost;

    public Node(Position position, int heuristicCost, int minPathCost) {
      this(position, null, heuristicCost, minPathCost);
    }

    public int getCost() {
      return this.heuristicCost + this.minPathCost;
    }

    @Override
    public int compareTo(Node o) {
      return Integer.compare(this.getCost(), o.getCost());
    }
  }
}
