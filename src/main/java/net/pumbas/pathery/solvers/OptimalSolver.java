package net.pumbas.pathery.solvers;

import java.util.Iterator;
import lombok.RequiredArgsConstructor;

import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;

public class OptimalSolver implements Solver {
  
  /**
   * An {@link Iterator} which loops through every possible wall placement position in
   * the given {@link PatheryMap}. This will only return positions for {@link TileType#OPEN}
   * tiles.
   */
  @RequiredArgsConstructor
  public static class WallPositionIterator implements Iterator<Position> {

    private final PatheryMap map;

    private int currentPosition = 0;

    private int getXPosition() {
      return this.currentPosition % this.map.getWidth();
    }

    private int getYPosition() {
      return this.currentPosition / this.map.getHeight();
    }

    private isCurrentPositionValidWallLocation() {
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
      return new Position(this.getXPosition(), this.getYPosition());
    }

  }

  @Override
  public OptimalSolution findOptimalSolution(PatheryMap map) {
    return null;
  }
}
