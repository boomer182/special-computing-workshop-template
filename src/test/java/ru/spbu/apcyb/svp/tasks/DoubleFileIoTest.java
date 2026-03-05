package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DoubleFileIoTest {

  @TempDir
  Path tempDir;

  @Test
  void read_doubles_reads_all_lines() throws Exception {
    Path input = tempDir.resolve("input.txt");
    Files.writeString(input, "1.0\n-2.5\n3\n");

    DoubleFileIo io = new DoubleFileIo();
    List<Double> values = io.readDoubles(input);

    assertEquals(List.of(1.0, -2.5, 3.0), values);
  }

  @Test
  void write_report_writes_lines() throws Exception {
    Path out = tempDir.resolve("report.txt");
    List<String> report = List.of("line1", "line2", "line3");

    DoubleFileIo io = new DoubleFileIo();
    io.writeReport(out, report);

    assertEquals(report, Files.readAllLines(out));
  }

  @Test
  void write_results_creates_non_empty_file() throws Exception {
    Path out = tempDir.resolve("out.txt");

    DoubleFileIo io = new DoubleFileIo();
    io.writeResults(out, List.of(0.0, 1.0), 2, 10, 5);

    List<String> lines = Files.readAllLines(out);
    assertTrue(lines.size() >= 2);
    String joined = String.join("\n", lines);
    assertTrue(joined.contains("2"));
  }
}