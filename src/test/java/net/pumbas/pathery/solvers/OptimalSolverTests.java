package net.pumbas.pathery.solvers;

import java.util.Collections;
import java.util.Set;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptimalSolverTests {

  @Test
  public void testAllWallCombinationsAreGenerated() {
    String[] codedMap = {
        "rrror",
        "rrrof",
        "sooor",
    };

    PatheryMap map = new PatheryMap(codedMap, 2, Collections.emptyList());
    OptimalSolver solver = new OptimalSolver();

    Set<Set<Position>> wallPositionCombinations = solver.getAllWallCombinations(map);
    Set<Set<Position>> expectedWallPositionCombinations = Set.of(
        Set.of(new Position(3, 0), new Position(3, 1)),
        Set.of(new Position(3, 0), new Position(1, 2)),
        Set.of(new Position(3, 0), new Position(2, 2)),
        Set.of(new Position(3, 0), new Position(3, 2)),

        Set.of(new Position(3, 1), new Position(1, 2)),
        Set.of(new Position(3, 1), new Position(2, 2)),
        Set.of(new Position(3, 1), new Position(3, 2)),

        Set.of(new Position(1, 2), new Position(2, 2)),
        Set.of(new Position(1, 2), new Position(3, 2)),

        Set.of(new Position(2, 2), new Position(3, 2))
    );

    Assertions.assertEquals(expectedWallPositionCombinations, wallPositionCombinations);
  }
}
