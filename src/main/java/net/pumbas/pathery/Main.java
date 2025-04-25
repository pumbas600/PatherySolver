package net.pumbas.pathery;

import java.util.List;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.solvers.EfficientSolver;
import net.pumbas.pathery.solvers.ParallelOptimalSolver;
import net.pumbas.pathery.solvers.Solver;

public class Main {

  public static void main(String[] args) {
    
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

    // Best path length should be 32.
    System.out.println(solution);


  }

}
