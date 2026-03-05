package ru.spbu.apcyb.svp.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.DoubleUnaryOperator;

/**
 * Computes tan(x) for a list of doubles in single-threaded or limited-parallel mode.
 *
 * <p>Parallel mode limits the number of in-flight computations to maxParallelism.
 */
public final class TanCalculator {

  private final int maxParallelism;
  private final DoubleUnaryOperator operator;

  /**
   * Creates calculator.
   *
   * @param maxParallelism maximum number of simultaneously processed values
   * @param operator function to apply (usually Math::tan)
   */
  public TanCalculator(int maxParallelism, DoubleUnaryOperator operator) {
    if (maxParallelism <= 0) {
      throw new IllegalArgumentException("maxParallelism must be positive");
    }
    this.maxParallelism = maxParallelism;
    this.operator = Objects.requireNonNull(operator, "operator");
  }

  /**
   * Computes results sequentially.
   *
   * @param values input values
   * @return computation result including timing
   */
  public TanComputationResult computeSingle(List<Double> values) {
    Objects.requireNonNull(values, "values");

    long start = System.nanoTime();
    List<Double> out = new ArrayList<>(values.size());
    for (Double value : values) {
      out.add(operator.applyAsDouble(value));
    }
    long end = System.nanoTime();
    return new TanComputationResult(out, values.size(), end - start);
  }

  /**
   * Computes results in parallel with a strict upper bound on in-flight tasks.
   *
   * @param values input values
   * @return computation result including timing
   * @throws RuntimeException if worker task fails
   */
  public TanComputationResult computeParallel(List<Double> values) {
    Objects.requireNonNull(values, "values");

    long start = System.nanoTime();

    ExecutorService pool = Executors.newFixedThreadPool(maxParallelism);
    try {
      CompletionService<IndexedValue> completion = new ExecutorCompletionService<>(pool);

      int submitted = 0;
      int completed = 0;

      int total = values.size();
      double[] results = new double[total];

      // Prime the pipeline with up to maxParallelism tasks
      int initial = Math.min(maxParallelism, total);
      for (int i = 0; i < initial; i++) {
        submit(completion, i, values.get(i));
        submitted++;
      }

      while (completed < total) {
        try {
          Future<IndexedValue> future = completion.take();
          IndexedValue done = future.get();
          results[done.index] = done.value;
          completed++;

          if (submitted < total) {
            submit(completion, submitted, values.get(submitted));
            submitted++;
          }
        } catch (Exception ex) {
          throw new RuntimeException("Parallel computation failed", ex);
        }
      }

      List<Double> out = new ArrayList<>(total);
      for (double v : results) {
        out.add(v);
      }

      long end = System.nanoTime();
      return new TanComputationResult(out, total, end - start);
    } finally {
      pool.shutdownNow();
    }
  }

  private void submit(CompletionService<IndexedValue> completion, int index, double x) {
    completion.submit(() -> new IndexedValue(index, operator.applyAsDouble(x)));
  }

  private static final class IndexedValue {
    private final int index;
    private final double value;

    private IndexedValue(int index, double value) {
      this.index = index;
      this.value = value;
    }
  }
}