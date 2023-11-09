package net.pumbas.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValuedTile {

  private final TileType tileType;
  private final int value;

}
