package net.pumbas.pathery.models;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TileType {
  WALL('w', "Wall", true),
  OPEN('o', "Open"),
  START('s', "Start"),
  FINISH('f', "Finish"),
  CHECKPOINT('c', "Checkpoint"),
  ROCK('r', "Rock", true),
  TELEPORT_IN('t', "Teleport In"),
  TELEPORT_OUT('u', "Teleport Out"),
  UNBUILDABLE('p', "Unbuildable"),
  DIRECTIONAL_FORCE('z', "Directional Force"),
  SINGLE_PATH_ROCK('x', "Single-Path-Rock", true);

  private static final Map<Character, TileType> codeMap = new HashMap<>();

  static {
    for (TileType tile : TileType.values()) {
      codeMap.put(tile.getCode(), tile);
    }
  }

  private final char code;
  private final String name;
  private final boolean isBlocked;

  TileType(char code, String name) {
    this(code, name, false);
  }

  public static TileType fromCode(char code) {
    return codeMap.get(code);
  }

  /**
   * Decodes the given coded map into a {@link TileType} matrix. A coded map is one where each
   * character in the string is a {@link TileType#code} and each element represents a row of the
   * map.
   * <p>
   * For example:
   * <pre>{@code
   *   String[] codedMap = new String[] {
   *     "rooor",
   *     "rorof",
   *     "sooor"
   *   }
   * }</pre>
   *
   * @param codedMap The coded map to decode
   * @return The {@link TileType} matrix represented by the coded map
   */
  public static TileType[][] decode(String[] codedMap) {
    int width = codedMap[0].length();
    int height = codedMap.length;
    TileType[][] map = new TileType[width][height];

    for (int y = 0; y < height; y++) {
      char[] codes = codedMap[y].toCharArray();

      for (int x = 0; x < width; x++) {
        char tileCode = codes[x];
        map[x][y] = TileType.fromCode(tileCode);
      }
    }

    return map;
  }
}
