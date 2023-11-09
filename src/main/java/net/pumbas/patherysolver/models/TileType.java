package net.pumbas.patherysolver.models;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TileType {
  OPEN("o", "Open"),
  START("s", "Start"),
  FINISH("f", "Finish"),
  CHECKPOINT("c", "Checkpoint", true),
  ROCK("r", "Rock"),
  TELEPORT_IN("t", "Teleport In"),
  TELEPORT_OUT("u", "Teleport Out"),
  UNBUILDABLE("p", "Unbuildable"),
  DIRECTIONAL_FORCE("z", "Directional Force"),
  SINGLE_PATH_ROCK("x", "Single-Path-Rock");

  private static final Map<String, TileType> codeMap = new HashMap<>();

  static {
    for (TileType tile : TileType.values()) {
      codeMap.put(tile.getCode(), tile);
    }
  }

  private final String code;
  private final String name;

  /**
   * Whether this tile has a meaningful value associated with it. A meaningful value is one that
   * affects the game in some way. For example, rock has different values to change the style of it
   * in the website, but these are purely visual and don't affect the game, so it is not
   * meaningful.
   * <p>
   * While some tiles may have a value that is meaningful, this will be false if that value is
   * supported by the solver.
   */
  private final boolean isValued;

  TileType(String code, String name) {
    this(code, name, false);
  }

  public static TileType fromCode(String code) {
    return codeMap.get(code);
  }
}
