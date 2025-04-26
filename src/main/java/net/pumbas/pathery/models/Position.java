package net.pumbas.pathery.models;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Position {

  private static final Map<String, Position> CACHE = new HashMap<>();

  public static Position UP = Position.of(0, -1);
  public static Position DOWN = Position.of(0, 1);
  public static Position LEFT = Position.of(-1, 0);
  public static Position RIGHT = Position.of(1, 0);

  private final int x;
  private final int y;

  public static Position of(final int x, final int y) {
    final String key = x + "," + y;
    Position position = CACHE.get(key);
    
    if (position == null) {
      position = new Position(x, y);
      CACHE.put(key, position);
    }

    return position;
  }

  public Position add(Position position) {
    return Position.of(this.x + position.x, this.y + position.y);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", this.x, this.y);
  }

}
