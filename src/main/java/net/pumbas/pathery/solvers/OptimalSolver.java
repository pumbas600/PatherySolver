package net.pumbas.pathery.solvers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;

public class OptimalSolver implements Solver {

  @Override
  public OptimalSolution findOptimalSolution(PatheryMap map) {
    // TODO: Handle case when getMaxWalls == 0

    this.getAllWallPositionCombinations(map).forEach(System.out::println);

    int currentWallIndex = 0;

    return null;
  }

  protected Set<Set<Position>> getAllWallPositionCombinations(PatheryMap map) {
    Set<Set<Position>> wallPositionCombinations = new HashSet<>();
    Set<Set<Position>> temporaryNewWallPositionCombinations = new HashSet<>();
    wallPositionCombinations.add(new HashSet<>());

    int totalPositions = map.getWidth() * map.getHeight();

    for (int positionIndex = 0; positionIndex < totalPositions; positionIndex++) {
      int x = positionIndex % map.getWidth();
      int y = positionIndex / map.getWidth();

      if (map.getTile(x, y) != TileType.OPEN) {
        continue;
      }

      Position position = new Position(x, y);

      for (Set<Position> wallPositions : wallPositionCombinations) {
        if (wallPositions.size() == map.getMaxWalls()) {
          continue;
        }

        Set<Position> newWallPositions = new HashSet<>(wallPositions);
        newWallPositions.add(position);

        // Add it to the temporary set so that we don't start iterating over it in this for loop.
        temporaryNewWallPositionCombinations.add(newWallPositions);
      }

      // Now that we've finished iterating over wallPositionCombinations, we can add the new subsets
      wallPositionCombinations.addAll(temporaryNewWallPositionCombinations);
      temporaryNewWallPositionCombinations.clear();
    }

    return wallPositionCombinations.stream()
        .filter(wallPosition -> wallPosition.size() == map.getMaxWalls())
        .collect(Collectors.toSet());
  }

  /**
   * An {@link Iterator} which loops through every possible wall placement position in the given
   * {@link PatheryMap}. This will only return positions for {@link TileType#OPEN} tiles.
   */
  @RequiredArgsConstructor
  public static class WallPositionIterator implements Iterator<Position> {

    private final PatheryMap map;

    private int currentPosition = 0;

    private int getXPosition() {
      return this.currentPosition % this.map.getWidth();
    }

    private int getYPosition() {
      return this.currentPosition / this.map.getWidth();
    }

    private boolean isCurrentPositionValidWallLocation() {
      TileType tileType = this.map.getTile(this.getXPosition(), this.getYPosition());
      return tileType == TileType.OPEN;
    }

    private void findNextWallPosition() {
      while (this.map.isWithinBounds(this.getXPosition(), this.getYPosition())
          && this.isCurrentPositionValidWallLocation()) {
        this.currentPosition++;
      }
    }

    @Override
    public boolean hasNext() {
      this.findNextWallPosition();
      return this.map.isWithinBounds(this.getXPosition(), this.getYPosition());
    }

    @Override
    public Position next() {
      Position position = new Position(this.getXPosition(), this.getYPosition());
      this.currentPosition++;
      return position;
    }

    public void reset() {
      this.currentPosition = 0;
    }

  }
}
