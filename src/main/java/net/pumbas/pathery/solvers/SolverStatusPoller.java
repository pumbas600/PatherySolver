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

  public OptimalSolution run() throws NoSolutionException {
    final long startTimeMs = System.currentTimeMillis();
    final long pollingIntervalMs = this.pollingInterval.toMillis();

    final ScheduledFuture<?> progressPollingFuture = executorService.scheduleAtFixedRate(() -> {
      final long elapsedTimeMs = System.currentTimeMillis() - startTimeMs;
      System.out.println(
          "Explored: %d, Pruned: %d, Current Longest Path: %d. Elapsed Time: %s".formatted(
              this.solver.getExploredCount(), this.solver.getPrunedCount(),
              this.solver.getCurrentLongestPathLength(), msToTimeString(elapsedTimeMs)));
    }, pollingIntervalMs, pollingIntervalMs, TimeUnit.MILLISECONDS);

    try {
      final OptimalSolution optimalSolution = CompletableFuture
          .supplyAsync(() -> this.solver.findOptimalSolution(this.map)).get();

      final long totalTimeMs = System.currentTimeMillis() - startTimeMs;
      System.out.println("Final Result: %s. Total Time: %s".formatted(optimalSolution, msToTimeString(totalTimeMs)));

      progressPollingFuture.cancel(true);

      return optimalSolution;
    } catch (final InterruptedException e) {
      throw new RuntimeException("Solver execution interrupted", e);
    } catch (final ExecutionException e) {
      throw new RuntimeException("Solver execution failed", e);
    }
  }
  
  private String msToTimeString(long ms) {
    final Duration duration = Duration.ofMillis(ms);
    final long hours = duration.toHours();
    final long minutes = duration.toMinutesPart();
    final long seconds = duration.toSecondsPart();
    final long millis = duration.toMillisPart();

    return "%02d:%02d:%02d.%03d".formatted(hours, minutes, seconds, millis);
  }
}
