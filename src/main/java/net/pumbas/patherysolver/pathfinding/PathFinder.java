package net.pumbas.patherysolver.pathfinding;

import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pumbas.patherysolver.models.Position;

public interface PathFinder {

  int NO_PATH = -1;

  /**
   * Finds the shortest path between a starting tile and a finish tile for the given {@link Set} of
   * walls and returns its length. If there is no valid path then {@link #NO_PATH} is returned.
   *
   * @param walls          The {@link Position}s of the walls
   * @param startPositions The {@link Position}s a path can start from
   * @param endPositions   The {@link Position}s a path can end
   * @return The length of the shortest path found, or {@link #NO_PATH} if there is no valid path
   */
  int getPathLength(Set<Position> walls, Set<Position> startPositions, Set<Position> endPositions);

  @Getter
  @RequiredArgsConstructor
  class PathNode implements Comparable<PathNode> {

    private final Position position;
    private final int pathLength;

    @Override
    public int compareTo(PathNode pathNode) {
      return Integer.compare(this.pathLength, pathNode.pathLength);
    }

    public List<Position> getNeighbours() {
      return List.of(
          this.position.add(Position.UP),
          this.position.add(Position.RIGHT),
          this.position.add(Position.DOWN),
          this.position.add(Position.LEFT)
      );
    }

  }

}
