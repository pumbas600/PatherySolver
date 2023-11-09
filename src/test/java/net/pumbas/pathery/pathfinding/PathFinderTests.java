package net.pumbas.pathery.pathfinding;

import java.util.Collections;
import java.util.List;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PathFinderTests {

  private static List<PathFinder> getPathFinders() {
    return List.of(new DijkstraPathFinder());
  }

  @ParameterizedTest
  @MethodSource("getPathFinders")
  public void testWithNoWallsOrCheckpointsAndOneStartAndFinish(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rooor",
        "rooof",
        "sooor",
    };

    PatheryMap map = new PatheryMap(TileType.decode(codedMap), 0, Collections.emptyList());
    List<Position> path = pathFinder.findCompletePath(map, Collections.emptySet());

    List<Position> expectedPath = List.of(
        new Position(0, 2), new Position(1, 2), new Position(1, 1),
        new Position(2, 1), new Position(3, 1), new Position(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPathFinders")
  public void testPriorityWithNoWallsOrCheckpointsAndOneStartAndFinish(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rooor",
        "sorof",
        "rooor",
    };

    PatheryMap map = new PatheryMap(TileType.decode(codedMap), 0, Collections.emptyList());
    List<Position> path = pathFinder.findCompletePath(map, Collections.emptySet());

    List<Position> expectedPath = List.of(
        new Position(0, 1), new Position(1, 1), new Position(1, 0), new Position(2, 0),
        new Position(3, 0), new Position(3, 1), new Position(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPathFinders")
  public void testWithNoWallsOrCheckpointsAndMultipleStart(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "sooor",
        "sooof",
        "sooor",
    };

    PatheryMap map = new PatheryMap(TileType.decode(codedMap), 0, Collections.emptyList());
    List<Position> path = pathFinder.findCompletePath(map, Collections.emptySet());

    List<Position> expectedPath = List.of(
        new Position(0, 1), new Position(1, 1), new Position(2, 1),
        new Position(3, 1), new Position(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPathFinders")
  public void testWithNoWallsButOneCheckpoint(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rocor",
        "rooof",
        "sooor",
    };

    PatheryMap map = new PatheryMap(TileType.decode(codedMap), 0, List.of(new Position(2, 0)));
    List<Position> path = pathFinder.findCompletePath(map, Collections.emptySet());

    List<Position> expectedPath = List.of(
        new Position(0, 2), new Position(1, 2), new Position(1, 1), new Position(1, 0),
        new Position(2, 0), new Position(3, 0), new Position(3, 1), new Position(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

}
