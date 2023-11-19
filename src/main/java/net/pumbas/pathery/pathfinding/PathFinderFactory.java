package net.pumbas.pathery.pathfinding;

import net.pumbas.pathery.models.PatheryMap;

public class PathFinderFactory {

  public static PathFinder getPathFinder(PatheryMap map) {
    // TODO: If the map contains teleporters, use BFSPathFinder instead
    return new AStarPathFinder();
  }
}
