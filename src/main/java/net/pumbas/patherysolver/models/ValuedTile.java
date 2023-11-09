package net.pumbas.patherysolver.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValuedTile {

  private final TileType tileType;
  private final int value;

  /**
   * @param tileType The tile type to compare to
   * @return {@code true} if this tile is of the given {@link TileType}, {@code false} otherwise
   */
  public boolean is(final TileType tileType) {
    return this.tileType == tileType;
  }

}
