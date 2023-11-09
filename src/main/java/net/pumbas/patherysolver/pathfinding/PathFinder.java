package net.pumbas.patherysolver.pathfinding;

import java.util.List;
import lombok.Getter;
import net.pumbas.patherysolver.models.Position;
import net.pumbas.patherysolver.models.Solution;

public interface PathFinder {

  /**
   * Finds the shortest path between a starting tile and a finish tile for the given
   * {@link Solution} and returns its length. If there is no valid path then {@code -1} is
   * returned.
   *
   * @param solution       The {@link Solution} defining the walls on the map
   * @param startPositions The {@link Position}s a path can start from
   * @param endPositions   The {@link Position}s a path can end
   * @return The length of the shortest path found, or {@code -1} if there is no valid path
   */
  int getPathLength(Solution solution, List<Position> startPositions, List<Position> endPositions);

  @Getter
  class PathNode extends Position implements Comparable<PathNode> {

    private int pathLength;

    public PathNode(int x, int y, int pathLength) {
      super(x, y);
      this.pathLength = pathLength;
    }

    @Override
    public int compareTo(PathNode pathNode) {
      return Integer.compare(this.pathLength, pathNode.pathLength);
    }

  }

}
