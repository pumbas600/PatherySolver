package net.pumbas.pathery.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

public class PatheryMap {

  /* The ordering of these is important and is defined by the pathery rules. */
  private static final Position[] NEIGHBOURS = new Position[] {
      Position.UP, Position.RIGHT, Position.DOWN, Position.LEFT
  };

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
  private final List<Position> checkpoints;

  private final TileType[][] map;


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
          this.startTiles.add(Position.of(x, y));
        } else if (tileType == TileType.FINISH) {
          this.finishTiles.add(Position.of(x, y));
        }
      }
    }
  }

  /**
   * Checks if the given x and y coordinates are within the bounds of the map.
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @return {@code true} if the coordinates are within the bounds of the map, {@code false}
   * otherwise
   */
  public boolean isWithinBounds(int x, int y) {
    return x >= 0 && x < this.width
        && y >= 0 && y < this.height;
  }

  /**
   * Checks if the given position is within the bounds of the map.
   *
   * @param position The position to check
   * @return {@code true} if the position is within the bounds of the map, {@code false} otherwise
   */
  public boolean isWithinBounds(Position position) {
    return this.isWithinBounds(position.getX(), position.getY());
  }

  private void validateWithinBounds(int x, int y) {
    if (!this.isWithinBounds(x, y)) {
      throw new IllegalArgumentException(String.format(
          "The given position %s is not within the bounds of the map", Position.of(x, y)));
    }
  }

  /**
   * Gets a list of the adjacent {@link Position}s for the given position which are within the
   * bounds of the map and are not blocked by a wall or a blocking {@link TileType}.
   *
   * @param position The position to get the neighbours for
   * @param walls    The walls on the map
   * @return A list of the adjacent {@link Position}s for the given position
   */
  public List<Position> getNeighbours(Position position, PositionSet walls) {
    final List<Position> neighbours = new ArrayList<>(NEIGHBOURS.length);
    for (Position neighbour : NEIGHBOURS) {
      final Position neighbourPosition = position.add(neighbour);

      if (this.isWithinBounds(neighbourPosition) && !this.getTile(neighbourPosition).isBlocked()
          && !walls.contains(neighbourPosition)) {
        neighbours.add(neighbourPosition);
      }
    }

    return neighbours;
  }

  /**
   * Sets the {@link TileType} at the given position.
   *
   * @param position The position of the tile
   * @param tileType The {@link TileType} to set
   * @throws IllegalArgumentException if the position is outside the bounds of the map
   */
  public void setTile(Position position, TileType tileType) {
    this.validateWithinBounds(position.getX(), position.getY());
    this.map[position.getX()][position.getY()] = tileType;

    if (tileType == TileType.START) {
      this.startTiles.add(position);
    } else if (tileType == TileType.FINISH) {
      this.finishTiles.add(position);
    }
  }

  /**
   * Gets the {@link TileType} at the given coordinates. If there is no tile at the given position
   * then {@code null} is returned.
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @return The {@link TileType} at the given coordinates or {@code null} if there is no tile there
   * @throws IllegalArgumentException if the coordinates is outside the bounds of the map
   */
  public TileType getTile(int x, int y) {
    this.validateWithinBounds(x, y);
    return this.map[x][y];
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
    return this.getTile(position.getX(), position.getY());
  }

}
