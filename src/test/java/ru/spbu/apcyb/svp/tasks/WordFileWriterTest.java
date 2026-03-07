package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link WordFileWriter}.
 */
class WordFileWriterTest {

  private final WordFileWriter writer = new WordFileWriter();

  @Test
  void writeCountsShouldCreateCountsFile(@TempDir final Path tempDir) throws IOException {
    Map<String, Long> counts = Map.of(
        "foo", 2L,
        "bar", 1L
    );

    writer.writeCounts(tempDir, counts);

    Path countsFile = tempDir.resolve("counts.txt");
    String content = Files.readString(countsFile);

    assertTrue(Files.exists(countsFile));
    assertTrue(content.contains("foo 2"));
    assertTrue(content.contains("bar 1"));
  }

  @Test
  void writeWordFilesShouldCreateSeparateFiles(@TempDir final Path tempDir) {
    Map<String, Long> counts = Map.of(
        "foo", 2L,
        "bar", 1L,
        "baz", 1L
    );

    writer.writeWordFiles(tempDir, counts);

    assertEquals("foo foo", readFile(tempDir.resolve("foo.txt")));
    assertEquals("bar", readFile(tempDir.resolve("bar.txt")));
    assertEquals("baz", readFile(tempDir.resolve("baz.txt")));
  }

  private String readFile(final Path path) {
    try {
      return Files.readString(path);
    } catch (IOException exception) {
      throw new IllegalStateException(exception);
    }
  }
}