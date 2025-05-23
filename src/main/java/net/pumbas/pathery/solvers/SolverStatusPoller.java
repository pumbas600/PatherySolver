package net.pumbas.pathery.solvers;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.pumbas.pathery.models.OptimalSolution;

/**
 * Runs a solver in a separate thread and polls it's status at a given interval. This is useful for
 * long running solvers.
 */
public class SolverStatusPoller implements AutoCloseable {

  private final Solver solver;
  private final Duration pollingInterval;
  private final ScheduledExecutorService executorService;

  public SolverStatusPoller(final Solver solver) {
    this(solver, Duration.ofMinutes(1));
  }

  public SolverStatusPoller(final Solver solver, final Duration pollingInterval) {
    this.solver = solver;
    this.pollingInterval = pollingInterval;
    this.executorService = Executors.newSingleThreadScheduledExecutor();
  }

  public OptimalSolution run() {
    final long startTimeMs = System.currentTimeMillis();
    final long pollingIntervalMs = this.pollingInterval.toMillis();

    final ScheduledFuture<?> progressPollingFuture = executorService.scheduleAtFixedRate(() -> {
      final long elapsedTimeMs = System.currentTimeMillis() - startTimeMs;
      System.out.println(
          "[%s]: Explored: %d, Pruned: %d, Path Length: %d. Best Walls: %s".formatted(
              msToTimeString(elapsedTimeMs), this.solver.getExploredCount(), 
              this.solver.getPrunedCount(), this.solver.getCurrentLongestPathLength(),
              this.solver.getCurrentBestWallCombination()
          ));
    }, pollingIntervalMs, pollingIntervalMs, TimeUnit.MILLISECONDS);

    final OptimalSolution optimalSolution = this.solver.findOptimalSolution();

    final long totalTimeMs = System.currentTimeMillis() - startTimeMs;
    System.out.println("Final Result: %s. Total Time: %s".formatted(optimalSolution, msToTimeString(totalTimeMs)));

    progressPollingFuture.cancel(true);

    return optimalSolution;
  }
  
  private String msToTimeString(long ms) {
    final Duration duration = Duration.ofMillis(ms);
    final long hours = duration.toHours();
    final long minutes = duration.toMinutesPart();
    final long seconds = duration.toSecondsPart();
    final long millis = duration.toMillisPart();

    return "%02d:%02d:%02d.%03d".formatted(hours, minutes, seconds, millis);
  }

  @Override
  public void close() {
    this.executorService.close();
  }
}
