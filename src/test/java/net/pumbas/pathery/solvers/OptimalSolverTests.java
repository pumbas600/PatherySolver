package net.pumbas.pathery.solvers;

import java.util.List;
import java.util.Set;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptimalSolverTests {

  @Test
  public void testOptimalWallPositionIsFound() {
    String[] codedMap = {
        "rocor",
        "rorof",
        "sooor",
    };

    List<Position> checkpoints = List.of(Position.of(2, 0));
    PatheryMap map = new PatheryMap(codedMap, 1, checkpoints);

    Solver solver = new OptimalSolver(map);
    OptimalSolution optimalSolution = solver.findOptimalSolution();

    Assertions.assertEquals(11, optimalSolution.getMaxMoveCount());
    Assertions.assertEquals(Set.of(Position.of(3, 0)), optimalSolution.getWalls());
  }

  @Test
  public void testOptimalPathLengthIsFound() {
    String[] codedMap = {
        "rocof",
        "roror",
        "sooor",
    };

    List<Position> checkpoints = List.of(Position.of(2, 0));
    PatheryMap map = new PatheryMap(codedMap, 1, checkpoints);

    Solver solver = new EfficientSolver(map);
    OptimalSolution optimalSolution = solver.findOptimalSolution();

    Assertions.assertEquals(8, optimalSolution.getMaxMoveCount());
  }

  @Test
  public void testOptimalSolutionFoundOnComplexMap() {
    String[] codedMap = {
        "rooooooooooor",
        "rooroooooroof",
        "sooooooocooor",
        "rooooooooooor",
        "rooorooooooor",
        "rooooooooooor",
    };

    List<Position> checkpoints = List.of(Position.of(8, 2));
    PatheryMap map = new PatheryMap(codedMap, 6, checkpoints);

    Solver solver = new DuplicateFreeSolver(map);
    OptimalSolution optimalSolution = solver.findOptimalSolution();

    Assertions.assertEquals(39, optimalSolution.getMaxMoveCount());

  }

  @Test
  public void testOptimalSolutionFoundOnSimplePatheryMap25_04_25() {
    String[] codedMap = {
        "rooooooooooor",
        "rooooooooocof",
        "roooooooooror",
        "rorooooooooor",
        "roororrrrooor",
        "sooooorooooor"
    };

    List<Position> checkpoints = List.of(Position.of(10, 1));
    PatheryMap map = new PatheryMap(codedMap, 8, checkpoints);

    Solver solver = new DuplicateFreeSolver(map);
    OptimalSolution optimalSolution = solver.findOptimalSolution();

    Assertions.assertEquals(32, optimalSolution.getMaxMoveCount());
  }

  @Test
  public void testOptimalSolutionFoundOnSimplePatheryMap27_04_25() {
    String[] codedMap = {
        "rorroooooooor",
        "rooooooooooor",
        "rocooooooooor",
        "rooorooooooof",
        "sooooooooooor",
        "rooorooooooor",
    };

    List<Position> checkpoints = List.of(Position.of(2, 2));
    PatheryMap map = new PatheryMap(codedMap, 6, checkpoints);

    Solver solver = new DuplicateFreeSolver(map);
    OptimalSolution optimalSolution = solver.findOptimalSolution();

    Assertions.assertEquals(37, optimalSolution.getMaxMoveCount());
  }


}
