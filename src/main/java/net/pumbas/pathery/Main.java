package net.pumbas.pathery;

import java.util.List;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.solvers.OptimalSolver;
import net.pumbas.pathery.solvers.Solver;

public class Main {

  public static void main(String[] args) {
    String[] codedMap = new String[]{
        "sooooooooooor",
        "roooooooorror",
        "roocoooooooof",
        "rooroooooooor",
        "rooooooooooor",
        "rooooooooroor"
    };

    List<Position> checkpoints = List.of(new Position(3, 2));
    PatheryMap map = new PatheryMap(codedMap, 7, checkpoints);

    Solver solver = new OptimalSolver();
    OptimalSolution solution = solver.findOptimalSolution(map);

    System.out.println(solution);


  }

}
