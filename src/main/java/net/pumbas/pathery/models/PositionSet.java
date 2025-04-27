package net.pumbas.pathery.models;

import java.util.Collection;
import java.util.Set;

public interface PositionSet {
  
  PositionSet add(final Position position);

  PositionSet addAll(final Collection<Position> positions);

  PositionSet remove(final Position position);

  boolean contains(final Position position);

  Set<Position> toSet();

  int getCount();
}
