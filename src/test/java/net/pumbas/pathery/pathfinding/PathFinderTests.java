package net.pumbas.pathery.pathfinding;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PathFinderTests {

  private static List<PathFinder> getPathFinders() {
    return List.of(new BFSPathFinder());
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

    PatheryMap map = new PatheryMap(codedMap, 0, Collections.emptyList());
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

    PatheryMap map = new PatheryMap(codedMap, 0, Collections.emptyList());
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

    PatheryMap map = new PatheryMap(codedMap, 0, Collections.emptyList());
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

    List<Position> checkpoints = List.of(new Position(2, 0));
    PatheryMap map = new PatheryMap(codedMap, 0, checkpoints);

    List<Position> path = pathFinder.findCompletePath(map, Collections.emptySet());

    List<Position> expectedPath = List.of(
        new Position(0, 2), new Position(1, 2), new Position(1, 1), new Position(1, 0),
        new Position(2, 0), new Position(3, 0), new Position(3, 1), new Position(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPathFinders")
  public void testComplexMapWithNoWallsButOneCheckpoint(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rooooooooooor",
        "rooooooooooor",
        "roorooooorcor",
        "rooooorooooor",
        "rooooooooorof",
        "sooorooooooor",
    };

    List<Position> checkpoints = List.of(new Position(10, 2));
    PatheryMap map = new PatheryMap(codedMap, 0, checkpoints);
    List<Position> path = pathFinder.findCompletePath(map, Collections.emptySet());

    List<Position> expectedPath = List.of(
        new Position(0, 5), new Position(1, 5), new Position(1, 4), new Position(2, 4),
        new Position(3, 4), new Position(4, 4), new Position(5, 4), new Position(6, 4),
        new Position(7, 4), new Position(7, 3), new Position(8, 3), new Position(9, 3),
        new Position(10, 3), new Position(10, 2), new Position(11, 2), new Position(11, 3),
        new Position(11, 4), new Position(12, 4));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPathFinders")
  public void testComplexMapWith2WallsAndOneCheckpoint(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rooooooooooor",
        "rooooooooooor",
        "roorooooorcor",
        "rooooorooooor",
        "rooooooooorof",
        "sooorooooooor",
    };

    List<Position> checkpoints = List.of(new Position(10, 2));
    PatheryMap map = new PatheryMap(codedMap, 6, checkpoints);
    Set<Position> walls = Set.of(new Position(10, 3), new Position(11, 2));
    List<Position> path = pathFinder.findCompletePath(map, walls);

    List<Position> expectedPath = List.of(
        new Position(0, 5), new Position(1, 5), new Position(1, 4), new Position(1, 3),
        new Position(1, 2), new Position(1, 1), new Position(2, 1), new Position(3, 1),
        new Position(4, 1), new Position(5, 1), new Position(6, 1), new Position(7, 1),
        new Position(8, 1), new Position(9, 1), new Position(10, 1), new Position(10, 2),
        new Position(10, 1), new Position(9, 1), new Position(8, 1), new Position(8, 2),
        new Position(8, 3), new Position(9, 3), new Position(9, 4), new Position(9, 5),
        new Position(10, 5), new Position(11, 5), new Position(11, 4), new Position(12, 4));

    Assertions.assertEquals(expectedPath, path);
  }

}
