package net.pumbas.pathery.models;

import java.util.BitSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BitSetWallCombination implements WallCombination {

  private final BitSet walls;
  private final int width;

  public static BitSetWallCombination empty(final PatheryMap map) {
    return new BitSetWallCombination(new BitSet(map.getWidth() * map.getHeight()), map.getWidth());
  }

  private int positionToIndex(Position position) {
    return position.getX() + position.getY() * this.width;
  }

  private Position indexToPosition(int index) {
    return new Position(index % this.width, index / this.width);
  }

  @Override
  public WallCombination add(Position position) {
    final BitSet newBitSet = (BitSet) this.walls.clone();

    newBitSet.set(this.positionToIndex(position));
    return new BitSetWallCombination(newBitSet, this.width);
  }

  @Override
  public Set<Position> getWalls() {
    return this.walls.stream()
      .mapToObj(this::indexToPosition)
      .collect(Collectors.toSet());
  }

  @Override
  public int getWallCount() {
    return this.walls.cardinality();
  }

  @Override
  public int hashCode() {
    return this.walls.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }

    final BitSetWallCombination other = (BitSetWallCombination) obj;
    return this.walls.equals(other.walls) && this.width == other.width;
  }

  @Override
  public String toString() {
    return this.getWalls().toString();
  }
}
