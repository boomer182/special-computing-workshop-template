package ru.spbu.apcyb.svp.tasks;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Main class for Stream API task.
 */
public final class StreamApiTaskMain {

  private StreamApiTaskMain() {
  }

  /**
   * Application entry point.
   *
   * @param args command line arguments:
   *             args[0] - input file path,
   *             args[1] - output directory path
   * @throws IOException if file processing fails
   */
  public static void main(final String[] args) throws IOException {
    if (args.length < 2) {
      throw new IllegalArgumentException(
          "Usage: java StreamApiTaskMain <input-file> <output-directory>"
      );
    }

    Path inputFile = Paths.get(args[0]);
    Path outputDirectory = Paths.get(args[1]);

    WordStatisticsService statisticsService = new WordStatisticsService();
    WordFileWriter fileWriter = new WordFileWriter();

    Map<String, Long> counts = statisticsService.countWords(inputFile);
    fileWriter.writeCounts(outputDirectory, counts);
    fileWriter.writeWordFiles(outputDirectory, counts);
  }
}