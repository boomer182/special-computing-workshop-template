package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link WordStatisticsService}.
 */
class WordStatisticsServiceTest {

  private final WordStatisticsService service = new WordStatisticsService();

  @Test
  void countWordsShouldCountAllWords(@TempDir final Path tempDir) throws IOException {
    Path inputFile = tempDir.resolve("input.txt");
    Files.writeString(inputFile, "foo foo bar baz");

    Map<String, Long> counts = service.countWords(inputFile);

    assertEquals(3, counts.size());
    assertEquals(2L, counts.get("foo"));
    assertEquals(1L, counts.get("bar"));
    assertEquals(1L, counts.get("baz"));
  }

  @Test
  void countWordsShouldHandleMultipleLines(@TempDir final Path tempDir) throws IOException {
    Path inputFile = tempDir.resolve("input.txt");
    Files.writeString(inputFile, "foo bar\nbar baz\nfoo");

    Map<String, Long> counts = service.countWords(inputFile);

    assertEquals(2L, counts.get("foo"));
    assertEquals(2L, counts.get("bar"));
    assertEquals(1L, counts.get("baz"));
  }

  @Test
  void countWordsShouldReturnEmptyMapForEmptyFile(@TempDir final Path tempDir)
      throws IOException {
    Path inputFile = tempDir.resolve("empty.txt");
    Files.writeString(inputFile, "");

    Map<String, Long> counts = service.countWords(inputFile);

    assertEquals(Map.of(), counts);
  }
}