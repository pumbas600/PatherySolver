package net.pumbas.pathery.pathfinding;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;

public class AStarPathFinder implements PathFinder {

  @Override
  public List<Position> findPath(
      PatheryMap map,
      Set<Position> walls,
      Set<Position> startPositions,
      Set<Position> endPositions
  ) throws NoPathException {
    throw new NoPathException("To be implemented");
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
    private final int heuristicCost;
    private final int minPathCost;

    public int getCost() {
      return this.heuristicCost + this.minPathCost;
    }

    @Override
    public int compareTo(Node o) {
      return Integer.compare(this.getCost(), o.getCost());
    }
  }
}
