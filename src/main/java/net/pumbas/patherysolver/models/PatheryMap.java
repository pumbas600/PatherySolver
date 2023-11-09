package net.pumbas.patherysolver.models;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public class PatheryMap {

  @Getter
  private final int width;
  @Getter
  private final int height;
  @Getter
  private final int maxWalls;
  @Getter
  private final Set<Position> startTiles = new HashSet<>();
  @Getter
  private final Set<Position> finishTiles = new HashSet<>();
  @Getter
  private final Set<Position> checkpoints;

  private final TileType[][] map;

  public PatheryMap(int width, int height, int maxWalls, Set<Position> checkpoints) {
    this.width = width;
    this.height = height;
    this.maxWalls = maxWalls;
    this.map = new TileType[width][height];
    this.checkpoints = checkpoints;
  }

  public PatheryMap(TileType[][] map, int maxWalls, Set<Position> checkpoints) {
    this.width = map.length;
    this.height = this.width == 0 ? 0 : map[0].length;
    this.maxWalls = maxWalls;
    this.map = map;
    this.checkpoints = checkpoints;
    this.findStartAndFinishTiles();
  }

  private void findStartAndFinishTiles() {
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        TileType tileType = this.map[x][y];

        if (tileType == TileType.START) {
          this.startTiles.add(new Position(x, y));
        } else if (tileType == TileType.FINISH) {
          this.finishTiles.add(new Position(x, y));
        }
      }
    }
  }

  /**
   * Checks if the given position is within the bounds of the map.
   *
   * @param position The position to check
   * @return {@code true} if the position is within the bounds of the map, {@code false} otherwise
   */
  public boolean isWithinBounds(Position position) {
    return position.getX() >= 0 && position.getX() < this.width
        && position.getY() >= 0 && position.getY() < this.height;
  }

  /**
   * Sets the {@link TileType} at the given position.
   *
   * @param position The position of the tile
   * @param tileType The {@link TileType} to set
   * @throws IllegalArgumentException if the position is outside the bounds of the map
   */
  public void setTile(Position position, TileType tileType) {
    this.validateWithinBounds(position);
    this.map[position.getX()][position.getY()] = tileType;

    if (tileType == TileType.START) {
      this.startTiles.add(position);
    } else if (tileType == TileType.FINISH) {
      this.finishTiles.add(position);
    }
  }

  /**
   * Gets the {@link TileType} at the given position. If there is no tile at the given position then
   * {@code null} is returned.
   *
   * @param position The position of the tile
   * @return The {@link TileType} at the given position or {@code null} if there is no tile there
   * @throws IllegalArgumentException if the position is outside the bounds of the map
   */
  public TileType getTile(Position position) {
    this.validateWithinBounds(position);
    return this.map[position.getX()][position.getY()];
  }

  private void validateWithinBounds(Position position) {
    if (!this.isWithinBounds(position)) {
      throw new IllegalArgumentException(String.format(
          "The given position %s is not within the bounds of the map", position));
    }
  }

}
