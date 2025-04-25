package net.pumbas.pathery.models;

import java.util.Set;

import lombok.Value;

@Value
public class OptimalSolution {

  private final int maxPathLength;
  private final Set<Position> walls;
}
