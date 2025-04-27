package net.pumbas.pathery;

import java.util.List;
import java.util.concurrent.ExecutionException;

import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.solvers.DuplicateFreeSolver;
import net.pumbas.pathery.solvers.Solver;
import net.pumbas.pathery.solvers.SolverStatusPoller;

public class Main {

  public static void main(String[] args) {
    final String[] codedMap = new String[]{
        "rcoooooorooooooof",
        "rooooooooooroooof",
        "rorooooooooooooof",
        "roooooooooroooorf",
        "rooooorooooooroof",
        "roooooooooooooorf",
        "sooorooooroooooof",
        "roooorooooooroorf",
        "rooooooooooorooof",
    };

    final List<Position> checkpoints = List.of(Position.of(1, 0));
    final PatheryMap map = new PatheryMap(codedMap, 12, checkpoints);

    final Solver solver = new DuplicateFreeSolver();

    new SolverStatusPoller(solver, map).run();
  }

}
