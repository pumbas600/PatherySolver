package net.pumbas.pathery.solvers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;
import net.pumbas.pathery.models.OptimalSolution;
import net.pumbas.pathery.models.PositionSet;

@RequiredArgsConstructor
public class ParallelTreeSolver<TTree extends List<?>> implements Solver, AutoCloseable {

  private final Supplier<TreeSolver<TTree>> solverFactory;
  private final ExecutorService executorService;

  public ParallelTreeSolver(final Supplier<TreeSolver<TTree>> solverFactory) {
    /* By default subtract one because we use one thread for the status polling. */
    this(solverFactory, Runtime.getRuntime().availableProcessors() - 1);
  }

  public ParallelTreeSolver(
      final Supplier<TreeSolver<TTree>> solverFactory, final int threadCount) {
    this.solverFactory = solverFactory;
    this.executorService = Executors.newFixedThreadPool(threadCount);
  }


  @Override
  public OptimalSolution findOptimalSolution() {
    final TreeSolver<TTree> solver = this.solverFactory.get();
    final TTree tree = solver.getInitialTree();

    solver.expandTree(tree);

    if (tree.isEmpty()) {
      return null;
    }

    return null;
  }

  @Override
  public long getPrunedCount() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPrunedCount'");
  }

  @Override
  public long getExploredCount() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getExploredCount'");
  }

  @Override
  public int getCurrentLongestPathLength() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCurrentLongestPathLength'");
  }

  @Override
  public PositionSet getCurrentBestWallCombination() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCurrentBestWallCombination'");
  }

  @Override
  public void close() {
    this.executorService.close();
  }
}
