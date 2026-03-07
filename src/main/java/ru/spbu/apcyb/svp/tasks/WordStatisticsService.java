package ru.spbu.apcyb.svp.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for word counting using Stream API.
 */
public final class WordStatisticsService {

  private final WordExtractor wordExtractor;

  /**
   * Creates service with default extractor.
   */
  public WordStatisticsService() {
    this(new WordExtractor());
  }

  /**
   * Creates service with custom extractor.
   *
   * @param wordExtractor word extractor
   */
  public WordStatisticsService(final WordExtractor wordExtractor) {
    this.wordExtractor = wordExtractor;
  }

  /**
   * Counts word occurrences in a file.
   *
   * @param inputFile input file path
   * @return sorted map with counts
   * @throws IOException if file reading fails
   */
  public Map<String, Long> countWords(final Path inputFile) throws IOException {
    try (var lines = Files.lines(inputFile)) {
      return lines
          .flatMap(line -> wordExtractor.extractWords(line).stream())
          .collect(Collectors.groupingBy(
              Function.identity(),
              TreeMap::new,
              Collectors.counting()
          ));
    }
  }
}