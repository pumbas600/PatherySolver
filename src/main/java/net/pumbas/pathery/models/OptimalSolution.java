package net.pumbas.pathery.models;

import java.util.Set;

import lombok.Value;

@Value
public class OptimalSolution {

  private final int maxMoveCount;
  private final Set<Position> walls;

  public static OptimalSolution fromLongestPath(final int longestPathLength, final Set<Position> walls) {
    /* The number of moves is the path length -1. */
    return new OptimalSolution(longestPathLength - 1, walls);
  }
}
