package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link DirectoryTreeWriter}.
 */
public class DirectoryTreeWriterTest {

  @TempDir
  Path tempDir;

  @Test
  void writes_sorted_recursive_listing_with_dir_suffix() throws IOException {
    Path root = tempDir.resolve("root");
    Files.createDirectories(root);

    Files.createDirectories(root.resolve("b"));
    Files.createDirectories(root.resolve("a"));
    Files.createDirectories(root.resolve("a/sub"));

    Files.writeString(root.resolve("a/file.txt"), "x", StandardCharsets.UTF_8);
    Files.writeString(root.resolve("b/z.txt"), "y", StandardCharsets.UTF_8);

    Path out = tempDir.resolve("out.txt");

    DirectoryTreeWriter writer = new DirectoryTreeWriter();
    writer.write(root, out);

    List<String> lines = Files.readAllLines(out, StandardCharsets.UTF_8);

    assertEquals(List.of(
        "a/",
        "a/file.txt",
        "a/sub/",
        "b/",
        "b/z.txt"
    ), lines);
  }

  @Test
  void throws_if_not_a_directory() {
    Path file = tempDir.resolve("notDir.txt");
    assertThrows(IOException.class, () -> Files.writeString(file, "x", StandardCharsets.UTF_8));

    DirectoryTreeWriter writer = new DirectoryTreeWriter();
    assertThrows(IllegalArgumentException.class, () -> writer.write(file, tempDir.resolve("out.txt")));
  }

  @Test
  void supports_relative_paths() throws IOException {
    Path root = tempDir.resolve("root2");
    Files.createDirectories(root);
    Files.writeString(root.resolve("x.txt"), "x", StandardCharsets.UTF_8);

    Path cwd = Path.of("").toAbsolutePath().normalize();
    Path relRoot = cwd.relativize(root.toAbsolutePath().normalize());
    Path relOut = cwd.relativize(tempDir.resolve("relOut.txt").toAbsolutePath().normalize());

    DirectoryTreeWriter writer = new DirectoryTreeWriter();
    writer.write(relRoot, relOut);

    List<String> lines = Files.readAllLines(tempDir.resolve("relOut.txt"), StandardCharsets.UTF_8);
    assertEquals(List.of("x.txt"), lines);
  }
}