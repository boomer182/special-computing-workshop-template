package ru.spbu.apcyb.svp.tasks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Writes a recursive listing of files and directories under a given root directory into a file.
 *
 * <p>Output format:
 * <ul>
 *   <li>One entry per line</li>
 *   <li>Paths are relative to the root directory</li>
 *   <li>Directory entries end with '/'</li>
 *   <li>Path separator is always '/'</li>
 *   <li>Entries are sorted lexicographically for deterministic output</li>
 * </ul>
 */
public final class DirectoryTreeWriter {

  /**
   * Writes a recursive listing of the subtree under {@code directory} into {@code outputFile}.
   *
   * @param directory root directory to walk (must exist and be a directory)
   * @param outputFile destination file (parent directories will be created if needed)
   * @throws IOException if IO error occurs
   * @throws IllegalArgumentException if {@code directory} is not a directory
   * @throws NullPointerException if any argument is null
   */
  public void write(Path directory, Path outputFile) throws IOException {
    Objects.requireNonNull(directory, "directory");
    Objects.requireNonNull(outputFile, "outputFile");

    Path root = directory.normalize();
    if (!Files.isDirectory(root)) {
      throw new IllegalArgumentException("Not a directory: " + directory);
    }

    Path parent = outputFile.toAbsolutePath().normalize().getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }

    List<String> lines = collectLines(root);

    Files.write(outputFile, lines, StandardCharsets.UTF_8);
  }

  private List<String> collectLines(Path root) throws IOException {
    try (Stream<Path> walk = Files.walk(root)) {
      return walk
          .filter(path -> !path.equals(root))
          .sorted(Comparator.comparing(path -> normalizeForCompare(root, path)))
          .map(path -> toOutputLine(root, path))
          .collect(Collectors.toList());
    }
  }

  private String normalizeForCompare(Path root, Path path) {
    return toOutputLine(root, path);
  }

  private String toOutputLine(Path root, Path path) {
    Path relative = root.relativize(path);
    String normalized = relative.toString().replace('\\', '/');
    if (Files.isDirectory(path)) {
      return normalized + "/";
    }
    return normalized;
  }
}