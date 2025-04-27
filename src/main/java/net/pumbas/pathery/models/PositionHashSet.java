package net.pumbas.pathery.models;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PositionHashSet implements PositionSet {

  public static PositionHashSet EMPTY = new PositionHashSet();

  private final Set<Position> positions;
  
  private PositionHashSet() {
    this.positions = Collections.emptySet();
  }

  @Override
  public int getCount() {
    return this.positions.size();
  }

  @Override
  public PositionSet add(final Position position) {
    final HashSet<Position> newPositions = new HashSet<>(this.positions);
    newPositions.add(position);

    return new PositionHashSet(Collections.unmodifiableSet(newPositions));
  }

  @Override
  public PositionSet addAll(final Collection<Position> positions) {
    final HashSet<Position> newPositions = new HashSet<>(this.positions);
    newPositions.addAll(newPositions);

    return new PositionHashSet(Collections.unmodifiableSet(newPositions));
  }

  @Override
  public boolean contains(final Position position) {
    return this.positions.contains(position);
  }

  @Override
  public Set<Position> toSet() {
    return this.positions;
  }

  @Override
  public int hashCode() {
    return this.positions.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }

    final PositionHashSet other = (PositionHashSet) obj;
    return this.positions.equals(other.positions);
  }
  
  @Override
  public String toString() {
    return this.positions.toString();
  }
  
}
