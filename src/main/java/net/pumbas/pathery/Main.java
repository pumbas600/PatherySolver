package net.pumbas.pathery;

import java.util.List;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.solvers.EfficientSolver;
import net.pumbas.pathery.solvers.Solver;

public class Main {

  public static void main(String[] args) {
    
    final long startTime = System.currentTimeMillis();
    final String[] codedMap = new String[]{
        "rooooooooooor",
        "rooooooooocof",
        "roooooooooror",
        "rorooooooooor",
        "roororrrrooor",
        "sooooorooooor"
    };

    final List<Position> checkpoints = List.of(new Position(10, 1));
    final PatheryMap map = new PatheryMap(codedMap, 8, checkpoints);

    final Solver solver = new EfficientSolver();
    final OptimalSolution solution = solver.findOptimalSolution(map);
    final long endTime = System.currentTimeMillis();

    System.out.println("Execution time: " + (endTime - startTime) + "ms");

    // Best move count should be 32.
    System.out.println(solution);


  }

}
