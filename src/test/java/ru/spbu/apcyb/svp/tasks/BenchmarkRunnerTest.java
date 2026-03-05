package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class BenchmarkRunnerTest {

  @Test
  void run_returns_report_with_expected_header_and_rows() {
    TanCalculator calc = new TanCalculator(10, Math::tan);
    BenchmarkRunner runner = new BenchmarkRunner(calc);

    List<String> report = runner.run();

    // header
    assertTrue(report.get(0).contains("BENCHMARK"));
    // there should be at least: header + config + format + 3 sizes
    assertTrue(report.size() >= 6);
    // rows contain semicolon format
    assertTrue(report.stream().anyMatch(s -> s.startsWith("1;")));
    assertTrue(report.stream().anyMatch(s -> s.startsWith("100;")));
  }
}