package net.pumbas.pathery.models;

import java.util.BitSet;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionBitSet implements PositionSet {

  private final BitSet positions;
  private final int width;

  public static PositionBitSet empty(final PatheryMap map) {
    return new PositionBitSet(new BitSet(map.getWidth() * map.getHeight()), map.getWidth());
  }

  private int positionToIndex(final Position position) {
    return position.getX() + position.getY() * this.width;
  }

  private Position indexToPosition(final int index) {
    return Position.of(index % this.width, index / this.width);
  }

  @Override
  public PositionSet add(final Position position) {
    final BitSet newBitSet = (BitSet) this.positions.clone();

    newBitSet.set(this.positionToIndex(position));
    return new PositionBitSet(newBitSet, this.width);
  }

  @Override
  public PositionSet addAll(final Collection<Position> positions) {
    final BitSet newBitSet = (BitSet) this.positions.clone();

    for (final Position position : positions) {
      newBitSet.set(this.positionToIndex(position));
    }

    return new PositionBitSet(newBitSet, this.width);
  }

  @Override
  public boolean contains(final Position position) {
    return this.positions.get(this.positionToIndex(position));
  }

  @Override
  public Set<Position> toSet() {
    return this.positions.stream()
      .mapToObj(this::indexToPosition)
      .collect(Collectors.toSet());
  }

  @Override
  public int getCount() {
    return this.positions.cardinality();
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

    final PositionBitSet other = (PositionBitSet) obj;
    return this.positions.equals(other.positions) && this.width == other.width;
  }

  @Override
  public String toString() {
    return this.toSet().toString();
  }
}
