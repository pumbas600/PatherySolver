package net.pumbas.pathery.models;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OptimalSolution {
  private final int minPathLength;
  private final Set<Position> walls;
}
