package net.pumbas.pathery.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SetWallCombination implements WallCombination{

  public static SetWallCombination EMPTY = new SetWallCombination();

  @Getter
  private final Set<Position> walls;
  
  private SetWallCombination() {
    this.walls = Collections.emptySet();
  }

  @Override
  public int getWallCount() {
    return this.walls.size();
  }

  @Override
  public WallCombination add(final Position position, final PatheryMap map) {
    final HashSet<Position> newWalls = new HashSet<>(this.walls);
    newWalls.add(position);

    return new SetWallCombination(Collections.unmodifiableSet(newWalls));
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

    final SetWallCombination other = (SetWallCombination) obj;
    return this.walls.equals(other.walls);
  }
  
  @Override
  public String toString() {
    return this.walls.toString();
  }
  
}
