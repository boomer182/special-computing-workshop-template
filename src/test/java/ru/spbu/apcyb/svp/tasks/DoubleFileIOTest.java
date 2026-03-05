package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link DoubleFileIO}.
 */
public class DoubleFileIOTest {

  @TempDir
  Path temp;

  @Test
  void reads_whitespace_separated_doubles() throws IOException {
    Path in = temp.resolve("in.txt");
    Files.writeString(in, "1.0  2.5\n\n-3.0\t4", StandardCharsets.UTF_8);

    DoubleFileIO io = new DoubleFileIO();
    List<Double> values = io.readDoubles(in);

    assertEquals(List.of(1.0, 2.5, -3.0, 4.0), values);
  }

  @Test
  void writes_results_with_metadata() throws IOException {
    Path out = temp.resolve("out.txt");

    DoubleFileIO io = new DoubleFileIO();
    io.writeResults(out, List.of(0.0, 1.0), 2, 10, 5);

    List<String> lines = Files.readAllLines(out, StandardCharsets.UTF_8);

    assertEquals("0.0", lines.get(0));
    assertEquals("1.0", lines.get(1));
    assertEquals("COUNT=2", lines.get(2));
    assertEquals("ELAPSED_MS_SINGLE=10", lines.get(3));
    assertEquals("ELAPSED_MS_MULTI=5", lines.get(4));
  }
}