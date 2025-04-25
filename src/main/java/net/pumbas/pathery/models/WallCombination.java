package net.pumbas.pathery.models;

import java.util.Set;

public interface WallCombination {
  WallCombination add(final Position position);

  Set<Position> getWalls();

  int getWallCount();
}
