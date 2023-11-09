package net.pumbas.patherysolver.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Position {

  private final int x;
  private final int y;

  public boolean equals(final int x, final int y) {
    return this.x == x && this.y == y;
  }

}
