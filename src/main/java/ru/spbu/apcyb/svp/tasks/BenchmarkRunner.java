package ru.spbu.apcyb.svp.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Benchmarks single-thread vs limited-parallel tan computation.
 */
public final class BenchmarkRunner {

  private static final long DEFAULT_SEED = 12_345L;
  private static final int[] SIZES = {1, 100, 1_000_000};

  private static final int WARMUP_RUNS = 2;
  private static final int MEASURE_RUNS = 5;

  private final TanCalculator calculator;

  /**
   * Creates benchmark runner.
   *
   * @param calculator calculator to benchmark
   */
  public BenchmarkRunner(TanCalculator calculator) {
    this.calculator = calculator;
  }

  /**
   * Runs benchmarks for sizes 1, 100, 1_000_000.
   *
   * @return report lines
   */
  public List<String> run() {
    List<String> report = new ArrayList<>();
    report.add("BENCHMARK tan(x): single vs multi (maxParallel=10)");
    report.add("WARMUP_RUNS=" + WARMUP_RUNS + ", MEASURE_RUNS=" + MEASURE_RUNS);
    report.add("FORMAT: N; single_nanos; single_ms; multi_nanos; multi_ms");

    for (int n : SIZES) {
      List<Double> data = generate(n);

      warmup(data);

      TanComputationResult bestSingle = bestSingle(data);
      TanComputationResult bestMulti = bestMulti(data);

      report.add(n
          + "; " + bestSingle.getElapsedNanos() + "; " + bestSingle.getElapsedMillis()
          + "; " + bestMulti.getElapsedNanos() + "; " + bestMulti.getElapsedMillis());
    }

    return report;
  }

  private void warmup(List<Double> data) {
    for (int i = 0; i < WARMUP_RUNS; i++) {
      calculator.computeSingle(data);
      calculator.computeParallel(data);
    }
  }

  private TanComputationResult bestSingle(List<Double> data) {
    TanComputationResult best = null;
    long bestNanos = Long.MAX_VALUE;

    for (int i = 0; i < MEASURE_RUNS; i++) {
      TanComputationResult r = calculator.computeSingle(data);
      long nanos = r.getElapsedNanos();
      if (nanos < bestNanos) {
        bestNanos = nanos;
        best = r;
      }
    }

    return best;
  }

  private TanComputationResult bestMulti(List<Double> data) {
    TanComputationResult best = null;
    long bestNanos = Long.MAX_VALUE;

    for (int i = 0; i < MEASURE_RUNS; i++) {
      TanComputationResult r = calculator.computeParallel(data);
      long nanos = r.getElapsedNanos();
      if (nanos < bestNanos) {
        bestNanos = nanos;
        best = r;
      }
    }

    return best;
  }

  private List<Double> generate(int n) {
    Random rnd = new Random(DEFAULT_SEED + n);
    List<Double> out = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      out.add(rnd.nextDouble() * 1000.0 - 500.0);
    }
    return out;
  }
}