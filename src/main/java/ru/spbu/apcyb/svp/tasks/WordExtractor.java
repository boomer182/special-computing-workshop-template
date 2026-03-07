package ru.spbu.apcyb.svp.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Extracts normalized words from text.
 */
public final class WordExtractor {

  private static final String WORD_SEPARATOR_REGEX = "[^\\p{L}\\p{Nd}]+";

  /**
   * Extracts words from a line.
   *
   * @param line input line
   * @return list of normalized words
   */
  public List<String> extractWords(final String line) {
    if (line == null || line.isBlank()) {
      return List.of();
    }

    return Arrays.stream(line.toLowerCase(Locale.ROOT).split(WORD_SEPARATOR_REGEX))
        .filter(word -> !word.isBlank())
        .collect(Collectors.toList());
  }
}