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

    List<Position> checkpoints = List.of(new Position(2, 0));
    PatheryMap map = new PatheryMap(codedMap, 1, checkpoints);

    Solver solver = new OptimalSolver();
    OptimalSolution optimalSolution = solver.findOptimalSolution(map);

    Assertions.assertEquals(11, optimalSolution.getMaxMoveCount());
    Assertions.assertEquals(Set.of(new Position(3, 0)), optimalSolution.getWalls());
  }

  @Test
  public void testOptimalPathLengthIsFound() {
    String[] codedMap = {
        "rocof",
        "roror",
        "sooor",
    };

    List<Position> checkpoints = List.of(new Position(2, 0));
    PatheryMap map = new PatheryMap(codedMap, 1, checkpoints);

    Solver solver = new EfficientSolver();
    OptimalSolution optimalSolution = solver.findOptimalSolution(map);

    Assertions.assertEquals(8, optimalSolution.getMaxMoveCount());
  }

  @Test
  public void testOptimalWallPositionsFoundOnComplexMap() {
    String[] codedMap = {
        "rooooooooooor",
        "rooroooooroof",
        "sooooooocooor",
        "rooooooooooor",
        "rooorooooooor",
        "rooooooooooor",
    };

    List<Position> checkpoints = List.of(new Position(8, 2));
    PatheryMap map = new PatheryMap(codedMap, 6, checkpoints);

    Solver solver = new EfficientSolver();
    OptimalSolution optimalSolution = solver.findOptimalSolution(map);

    Assertions.assertEquals(39, optimalSolution.getMaxMoveCount());

  }

  @Test
  public void testOptimalWallPositionsFoundOnSimplePatheryMap() {
    String[] codedMap = {
        "rooooooooooor",
        "rooooooooocof",
        "roooooooooror",
        "rorooooooooor",
        "roororrrrooor",
        "sooooorooooor"
    };

    List<Position> checkpoints = List.of(new Position(10, 1));
    PatheryMap map = new PatheryMap(codedMap, 8, checkpoints);

    Solver solver = new EfficientSolver();
    OptimalSolution optimalSolution = solver.findOptimalSolution(map);

    Assertions.assertEquals(32, optimalSolution.getMaxMoveCount());

  }
}
