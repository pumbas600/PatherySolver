package net.pumbas.pathery.solvers;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.PositionSet;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.pathfinding.PathFinder;
import net.pumbas.pathery.pathfinding.PathFinderFactory;

/**
 * A common implementation for single-threaded solvers which provides fields for the metrics required
 * by the {@link Solver} interface.
 */
public abstract class AbstractSolver implements Solver {

  @Getter
  protected long prunedCount;
  @Getter
  protected long exploredCount;
  @Getter
  protected int currentLongestPathLength;
  @Getter
  protected PositionSet currentBestWallCombination;

  protected final PathFinder pathFinder;
  protected final PatheryMap map;

  public AbstractSolver(final PatheryMap map) {
    this.pathFinder = PathFinderFactory.getPathFinder(map);
    this.map = map;
  }

  protected void resetMetrics() {
    this.currentBestWallCombination = null;
    this.prunedCount = 0;
    this.exploredCount = 0;
    this.currentLongestPathLength = Integer.MIN_VALUE;
  }

  protected List<Position> findUnbannedPositions(
      final List<Position> path, final PositionSet bannedPositions) {
    final List<Position> unbannedPositions = new ArrayList<>(path.size());
    for (Position position : path) {
      if (this.map.getTile(position) == TileType.OPEN && !bannedPositions.contains(position)) {
        unbannedPositions.add(position);
      }
    }

    return unbannedPositions;
  }
}
