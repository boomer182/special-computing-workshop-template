package ru.spbu.apcyb.svp.tasks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Entry point for Task 4: multi-threaded tan computation and benchmarks.
 */
public final class Main {

  private static final int MAX_PARALLELISM = 10;

  private Main() {
    // no instances
  }

  /**
   * Program modes.
   *
   * <p>1) File processing:
   * <pre>
   *   Main &lt;inputFile&gt; &lt;outputFile&gt;
   * </pre>
   *
   * <p>2) Benchmark:
   * <pre>
   *   Main benchmark &lt;outputReportFile&gt;
   * </pre>
   *
   * @param args arguments
   * @throws IOException IO errors
   */
  public static void main(String[] args) throws IOException {
    if (args == null) {
      throw new IllegalArgumentException("args must not be null");
    }

    if (args.length == 2 && "benchmark".equalsIgnoreCase(args[0])) {
      runBenchmark(Path.of(args[1]));
      return;
    }

    if (args.length != 2) {
      throw new IllegalArgumentException("Expected: <inputFile> <outputFile> OR benchmark <outputReportFile>");
    }

    Path input = Path.of(args[0]);
    Path output = Path.of(args[1]);

    DoubleFileIO io = new DoubleFileIO();
    List<Double> values = io.readDoubles(input);

    TanCalculator calculator = new TanCalculator(MAX_PARALLELISM, Math::tan);

    TanComputationResult single = calculator.computeSingle(values);
    TanComputationResult multi = calculator.computeParallel(values);

    io.writeResults(output, multi.getValues(), multi.getCount(), single.getElapsedMillis(), multi.getElapsedMillis());
  }

  private static void runBenchmark(Path outputReport) throws IOException {
    TanCalculator calculator = new TanCalculator(MAX_PARALLELISM, Math::tan);
    BenchmarkRunner runner = new BenchmarkRunner(calculator);
    List<String> report = runner.run();

    DoubleFileIO io = new DoubleFileIO();
    io.writeReport(outputReport, report);
  }
}