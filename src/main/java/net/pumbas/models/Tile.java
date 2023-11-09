package net.pumbas.models;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tile {
  OPEN("o", "Open"),
  START("s", "Start"),
  FINISH("f", "Finish"),
  CHECKPOINT("c", "Checkpoint"),
  ROCK("r", "Rock"),
  TELEPORT_IN("t", "Teleport In"),
  TELEPORT_OUT("u", "Teleport Out"),
  UNBUILDABLE("p", "Unbuildable"),
  DIRECTIONAL_FORCE("z", "Directional Force"),
  SINGLE_PATH_ROCK("x", "Single-Path-Rock");

  private static final Map<String, Tile> codeMap = new HashMap<>();

  static {
    for (final Tile tile : Tile.values()) {
      codeMap.put(tile.getCode(), tile);
    }
  }

  private final String code;
  private final String name;

  public static Tile fromCode(final String code) {
    return codeMap.get(code);
  }
}
