package net.pumbas.pathery.pathfinding;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.pumbas.pathery.exceptions.NoPathException;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.PositionHashSet;
import net.pumbas.pathery.models.PositionSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PathFinderTests {

  private static List<PathFinder> getPatheryEquivalentPathFinderTests() {
    return List.of(new BFSPathFinder());
  }

  @ParameterizedTest
  @MethodSource("getPatheryEquivalentPathFinderTests")
  public void testWithNoWallsOrCheckpointsAndOneStartAndFinish(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rooor",
        "rooof",
        "sooor",
    };

    PatheryMap map = new PatheryMap(codedMap, 0, Collections.emptyList());
    List<Position> path = pathFinder.findCompletePath(map, PositionHashSet.EMPTY);

    List<Position> expectedPath = List.of(
        Position.of(0, 2), Position.of(1, 2), Position.of(1, 1),
        Position.of(2, 1), Position.of(3, 1), Position.of(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPatheryEquivalentPathFinderTests")
  public void testPriorityWithNoWallsOrCheckpointsAndOneStartAndFinish(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rooor",
        "sorof",
        "rooor",
    };

    PatheryMap map = new PatheryMap(codedMap, 0, Collections.emptyList());
    List<Position> path = pathFinder.findCompletePath(map, PositionHashSet.EMPTY);

    List<Position> expectedPath = List.of(
        Position.of(0, 1), Position.of(1, 1), Position.of(1, 0), Position.of(2, 0),
        Position.of(3, 0), Position.of(3, 1), Position.of(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPatheryEquivalentPathFinderTests")
  public void testWithNoWallsButOneCheckpoint(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rocor",
        "rooof",
        "sooor",
    };

    List<Position> checkpoints = List.of(Position.of(2, 0));
    PatheryMap map = new PatheryMap(codedMap, 0, checkpoints);
    List<Position> path = pathFinder.findCompletePath(map, PositionHashSet.EMPTY);

    List<Position> expectedPath = List.of(
        Position.of(0, 2), Position.of(1, 2), Position.of(1, 1), Position.of(1, 0),
        Position.of(2, 0), Position.of(3, 0), Position.of(3, 1), Position.of(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPatheryEquivalentPathFinderTests")
  public void testMultipleStartsWithNoWallsOrCheckpoints(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "sooor",
        "sooof",
        "sooor",
    };

    PatheryMap map = new PatheryMap(codedMap, 0, Collections.emptyList());
    List<Position> path = pathFinder.findCompletePath(map, PositionHashSet.EMPTY);

    List<Position> expectedPath = List.of(
        Position.of(0, 1), Position.of(1, 1), Position.of(2, 1),
        Position.of(3, 1), Position.of(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPatheryEquivalentPathFinderTests")
  public void testMultipleFinishesWithNoWallsOrCheckpoints(PathFinder pathFinder)
      throws NoPathException {
    String[] codedMap = {
        "rooof",
        "sooof",
        "rooof",
    };

    PatheryMap map = new PatheryMap(codedMap, 0, Collections.emptyList());
    List<Position> path = pathFinder.findCompletePath(map, PositionHashSet.EMPTY);

    List<Position> expectedPath = List.of(
        Position.of(0, 1), Position.of(1, 1), Position.of(2, 1), Position.of(3, 1),
        Position.of(4, 1));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPatheryEquivalentPathFinderTests")
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

    List<Position> checkpoints = List.of(Position.of(10, 2));
    PatheryMap map = new PatheryMap(codedMap, 0, checkpoints);
    List<Position> path = pathFinder.findCompletePath(map, PositionHashSet.EMPTY);

    List<Position> expectedPath = List.of(
        Position.of(0, 5), Position.of(1, 5), Position.of(1, 4), Position.of(2, 4),
        Position.of(3, 4), Position.of(4, 4), Position.of(5, 4), Position.of(6, 4),
        Position.of(7, 4), Position.of(7, 3), Position.of(8, 3), Position.of(9, 3),
        Position.of(10, 3), Position.of(10, 2), Position.of(11, 2), Position.of(11, 3),
        Position.of(11, 4), Position.of(12, 4));

    Assertions.assertEquals(expectedPath, path);
  }

  @ParameterizedTest
  @MethodSource("getPatheryEquivalentPathFinderTests")
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

    List<Position> checkpoints = List.of(Position.of(10, 2));
    PatheryMap map = new PatheryMap(codedMap, 6, checkpoints);
    PositionSet walls = new PositionHashSet(Set.of(Position.of(10, 3), Position.of(11, 2)));
    List<Position> path = pathFinder.findCompletePath(map, walls);

    List<Position> expectedPath = List.of(
        Position.of(0, 5), Position.of(1, 5), Position.of(1, 4), Position.of(1, 3),
        Position.of(1, 2), Position.of(1, 1), Position.of(2, 1), Position.of(3, 1),
        Position.of(4, 1), Position.of(5, 1), Position.of(6, 1), Position.of(7, 1),
        Position.of(8, 1), Position.of(9, 1), Position.of(10, 1), Position.of(10, 2),
        Position.of(10, 1), Position.of(9, 1), Position.of(8, 1), Position.of(8, 2),
        Position.of(8, 3), Position.of(9, 3), Position.of(9, 4), Position.of(9, 5),
        Position.of(10, 5), Position.of(11, 5), Position.of(11, 4), Position.of(12, 4));

    Assertions.assertEquals(expectedPath, path);
  }

  @Test
  public void testShortestPathFoundForComplexMapWith2WallsAndOneCheckpoint()
      throws NoPathException {
    PathFinder pathFinder = new AStarPathFinder();
    String[] codedMap = {
        "rooooooooooor",
        "rooooooooooor",
        "roorooooorcor",
        "rooooorooooor",
        "rooooooooorof",
        "sooorooooooor",
    };

    List<Position> checkpoints = List.of(Position.of(10, 2));
    PatheryMap map = new PatheryMap(codedMap, 6, checkpoints);
    PositionSet walls = new PositionHashSet(Set.of(Position.of(10, 3), Position.of(11, 2)));
    List<Position> path = pathFinder.findCompletePath(map, walls);

    Assertions.assertEquals(28, path.size());
  }

}
