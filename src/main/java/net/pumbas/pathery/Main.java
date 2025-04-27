package net.pumbas.pathery;

import java.util.List;

import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.solvers.DuplicateFreeSolver;
import net.pumbas.pathery.solvers.Solver;
import net.pumbas.pathery.solvers.SolverStatusPoller;

public class Main {

  public static void main(String[] args) {
    final String[] codedMap = new String[]{
        "rorooorooorooooof",
        "sooooooooooororof",
        "rooooroooooooooof",
        "rooooooorooooooof",
        "rorooroooooooooof",
        "rooooorooooorooof",
        "roocoooooroooooof",
        "roorrooooooooooof",
        "rocooooooooooooof",
    };

    final List<Position> checkpoints = List.of(Position.of(3, 6), Position.of(2, 8));
    final PatheryMap map = new PatheryMap(codedMap, 10, checkpoints);

    final Solver solver = new DuplicateFreeSolver(map);

    try (final SolverStatusPoller statusPoller = new SolverStatusPoller(solver)) {
      statusPoller.run();
    };
  }

}
