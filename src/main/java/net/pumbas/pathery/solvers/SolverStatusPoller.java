package net.pumbas.pathery.solvers;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.pumbas.pathery.exceptions.NoSolutionException;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PatheryMap;

/**
 * Runs a solver in a separate thread and polls it's status at a given interval. This is useful for
 * long running solvers.
 */
public class SolverStatusPoller {

  private final Solver solver;
  private final PatheryMap map;
  private final Duration pollingInterval;
  private final ScheduledExecutorService executorService;

  public SolverStatusPoller(final Solver solver, final PatheryMap map) {
    this(solver, map, Duration.ofMinutes(1));
  }

  public SolverStatusPoller(final Solver solver, final PatheryMap map, final Duration pollingInterval) {
    this.solver = solver;
    this.map = map;
    this.pollingInterval = pollingInterval;
    this.executorService = Executors.newSingleThreadScheduledExecutor();
  }

  public OptimalSolution run() throws NoSolutionException{
    final ScheduledFuture<?> progressPollingFuture = executorService.scheduleAtFixedRate(() -> {
      System.out.println(
        "Explored: %d, Pruned: %d, Current Longest Path: %d"
          .formatted(this.solver.getExploredCount(), this.solver.getPrunedCount(), this.solver.getCurrentLongestPathLength())
      );
    }, this.pollingInterval.toMillis(), this.pollingInterval.toMillis(), TimeUnit.MILLISECONDS);

    try {
      final OptimalSolution solution = CompletableFuture.supplyAsync(() -> this.solver.findOptimalSolution(this.map)).get();
      progressPollingFuture.cancel(true);

      return solution;
    } catch (final InterruptedException e) {
      throw new RuntimeException("Solver execution interrupted", e);
    } catch (final ExecutionException e) {
      if (e.getCause() instanceof NoSolutionException noSolutionException) {
        throw noSolutionException;
      }

      throw new RuntimeException("Solver execution failed", e);
    }
  }
}
