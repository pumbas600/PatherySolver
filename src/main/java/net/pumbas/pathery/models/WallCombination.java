package net.pumbas.pathery.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WallCombination {

  public static WallCombination EMPTY = new WallCombination();

  @Getter
  private final String uniqueId;

  @Getter
  private final Set<Position> walls;
  
  private WallCombination() {
    this.walls = Collections.emptySet();
    this.uniqueId = "";
  }

  public int getWallCount() {
    return this.walls.size();
  }

  public WallCombination add(Position position) {
    final HashSet<Position> newWalls = new HashSet<>(this.walls);
    newWalls.add(position);

    final String newUniqueId = newWalls.toString();

    return new WallCombination(newUniqueId, newWalls);
  }


  
}
