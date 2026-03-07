package ru.spbu.apcyb.svp.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Writes output files for the task.
 */
public final class WordFileWriter {

  private static final String COUNTS_FILE_NAME = "counts.txt";

  /**
   * Writes counts.txt file.
   *
   * @param outputDirectory output directory
   * @param counts word counts
   * @throws IOException if writing fails
   */
  public void writeCounts(final Path outputDirectory,
                          final Map<String, Long> counts) throws IOException {
    Files.createDirectories(outputDirectory);

    Path countsFile = outputDirectory.resolve(COUNTS_FILE_NAME);
    String content = counts.entrySet()
        .stream()
        .map(entry -> entry.getKey() + " " + entry.getValue())
        .collect(Collectors.joining(System.lineSeparator()));

    Files.writeString(countsFile, content);
  }

  /**
   * Writes separate files for each word with CompletableFuture.
   *
   * @param outputDirectory output directory
   * @param counts word counts
   */
  public void writeWordFiles(final Path outputDirectory,
                             final Map<String, Long> counts) {
    FilesHelper.createDirectory(outputDirectory);

    try (ExecutorService executorService = Executors.newFixedThreadPool(
        Math.max(1, Runtime.getRuntime().availableProcessors()))) {

      CompletableFuture<?>[] futures = counts.entrySet()
          .stream()
          .map(entry -> CompletableFuture.runAsync(
              () -> writeSingleWordFile(
                  outputDirectory,
                  entry.getKey(),
                  entry.getValue()
              ),
              executorService
          ))
          .toArray(CompletableFuture[]::new);

      CompletableFuture.allOf(futures).join();
    }
  }

  private void writeSingleWordFile(final Path outputDirectory,
                                   final String word,
                                   final long count) {
    Path file = outputDirectory.resolve(word + ".txt");
    String content = java.util.stream.Stream.generate(() -> word)
        .limit(count)
        .collect(Collectors.joining(" "));

    try {
      Files.writeString(file, content);
    } catch (IOException exception) {
      throw new IllegalStateException(
          "Failed to write file for word: " + word,
          exception
      );
    }
  }

  /**
   * Helper for directory creation without checked exception propagation.
   */
  private static final class FilesHelper {

    private FilesHelper() {
    }

    private static void createDirectory(final Path directory) {
      try {
        Files.createDirectories(directory);
      } catch (IOException exception) {
        throw new IllegalStateException(
            "Failed to create output directory: " + directory,
            exception
        );
      }
    }
  }
}