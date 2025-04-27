package net.pumbas.pathery.solvers;

import lombok.Getter;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.PositionSet;
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
}
