package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link StreamApiTaskMain}.
 */
class StreamApiTaskMainTest {

  @Test
  void mainShouldThrowExceptionWhenArgumentsAreMissing() {
    assertThrows(IllegalArgumentException.class, () -> StreamApiTaskMain.main(new String[0]));
  }

  @Test
  void mainShouldCreateExpectedFiles(@TempDir final Path tempDir) throws IOException {
    Path inputFile = tempDir.resolve("input.txt");
    Path outputDir = tempDir.resolve("out");

    Files.writeString(inputFile, "foo foo bar baz");

    StreamApiTaskMain.main(new String[]{
        inputFile.toString(),
        outputDir.toString()
    });

    assertEquals("foo foo", Files.readString(outputDir.resolve("foo.txt")));
    assertEquals("bar", Files.readString(outputDir.resolve("bar.txt")));
    assertEquals("baz", Files.readString(outputDir.resolve("baz.txt")));

    String countsContent = Files.readString(outputDir.resolve("counts.txt"));
    org.junit.jupiter.api.Assertions.assertTrue(countsContent.contains("foo 2"));
    org.junit.jupiter.api.Assertions.assertTrue(countsContent.contains("bar 1"));
    org.junit.jupiter.api.Assertions.assertTrue(countsContent.contains("baz 1"));
  }
}