package net.pumbas.patherysolver.models;

import lombok.Getter;

public class PatheryMap {

  @Getter
  private final int width;
  @Getter
  private final int height;

  private final TileType[][] map;

  public PatheryMap(final int width, final int height) {
    this.width = width;
    this.height = height;
    this.map = new TileType[width][height];
  }

  /**
   * Checks if the given position is within the bounds of the map.
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @return {@code true} if the position is within the bounds of the map, {@code false} otherwise
   */
  public boolean isWithinBounds(final int x, final int y) {
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
  public void setTile(final int x, final int y, final TileType tileType) {
    this.validateWithinBounds(x, y);
    this.map[x][y] = tileType;
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
  public TileType getTile(final int x, final int y) {
    this.validateWithinBounds(x, y);
    return this.map[x][y];
  }

  private void validateWithinBounds(final int x, final int y) {
    if (!this.isWithinBounds(x, y)) {
      throw new IllegalArgumentException(String.format(
          "The given position (%d, %d) is not within the bounds of the map", x, y));
    }
  }

}
