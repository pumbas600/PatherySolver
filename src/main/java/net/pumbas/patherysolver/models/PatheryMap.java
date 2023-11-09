package net.pumbas.patherysolver.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class PatheryMap {

  @Getter
  private final int width;
  @Getter
  private final int height;
  @Getter
  private final int maxWalls;
  @Getter
  private final List<Position> startTiles = new ArrayList<>();
  @Getter
  private final List<Position> finishTiles = new ArrayList<>();

  private final TileType[][] map;
  private final List<Position> checkpoints;

  public PatheryMap(int width, int height, int maxWalls, List<Position> checkpoints) {
    this.width = width;
    this.height = height;
    this.maxWalls = maxWalls;
    this.map = new TileType[width][height];
    this.checkpoints = checkpoints;
  }

  public PatheryMap(TileType[][] map, int maxWalls, List<Position> checkpoints) {
    this.width = map.length;
    this.height = this.width == 0 ? 0 : map[0].length;
    this.maxWalls = maxWalls;
    this.map = map;
    this.checkpoints = checkpoints;
    this.findStartAndFinishTiles();
  }

  public PatheryMap(String[] codedMap, int maxWalls, List<Position> checkpoints) {
    this(TileType.decode(codedMap), maxWalls, checkpoints);
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
   * @param x The x coordinate
   * @param y The y coordinate
   * @return {@code true} if the position is within the bounds of the map, {@code false} otherwise
   */
  public boolean isWithinBounds(int x, int y) {
    return x >= 0 && x < this.width && y >= 0 && y < this.height;
  }

  /**
   * Sets the {@link TileType} at the given position.
   *
   * @param x        The x coordinate of the tile
   * @param y        The y coordinate of the tile
   * @param tileType The {@link TileType} to set
   * @throws IllegalArgumentException if the position is outside the bounds of the map
   */
  public void setTile(int x, int y, TileType tileType) {
    this.validateWithinBounds(x, y);
    this.map[x][y] = tileType;

    if (tileType == TileType.START) {
      this.startTiles.add(new Position(x, y));
    } else if (tileType == TileType.FINISH) {
      this.finishTiles.add(new Position(x, y));
    }
  }

  /**
   * Gets the {@link TileType} at the given position. If there is no tile at the given position then
   * {@code null} is returned.
   *
   * @param x The x coordinate of the tile
   * @param y The y coordinate of the tile
   * @return The {@link TileType} at the given position or {@code null} if there is no tile there
   * @throws IllegalArgumentException if the position is outside the bounds of the map
   */
  public TileType getTile(int x, int y) {
    this.validateWithinBounds(x, y);
    return this.map[x][y];
  }

  private void validateWithinBounds(int x, int y) {
    if (!this.isWithinBounds(x, y)) {
      throw new IllegalArgumentException(String.format(
          "The given position (%d, %d) is not within the bounds of the map", x, y));
    }
  }

}
