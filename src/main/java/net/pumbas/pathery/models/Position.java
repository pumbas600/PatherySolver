package net.pumbas.pathery.models;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Position {

  public static Position UP = new Position(0, -1);
  public static Position DOWN = new Position(0, 1);
  public static Position LEFT = new Position(-1, 0);
  public static Position RIGHT = new Position(1, 0);

  private final int x;
  private final int y;

  public boolean equals(int x, int y) {
    return this.x == x && this.y == y;
  }

  public Position add(Position position) {
    return new Position(this.x + position.x, this.y + position.y);
  }

  public List<Position> getNeighbours() {
    return List.of(
        this.add(Position.UP),
        this.add(Position.RIGHT),
        this.add(Position.DOWN),
        this.add(Position.LEFT)
    );
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", this.x, this.y);
  }

}
