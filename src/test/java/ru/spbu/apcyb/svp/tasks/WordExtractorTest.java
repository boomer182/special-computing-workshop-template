package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WordExtractor}.
 */
class WordExtractorTest {

  private final WordExtractor wordExtractor = new WordExtractor();

  @Test
  void extractWordsShouldReturnNormalizedWords() {
    List<String> words = wordExtractor.extractWords("Foo foo, BAR! baz");

    assertEquals(List.of("foo", "foo", "bar", "baz"), words);
  }

  @Test
  void extractWordsShouldReturnEmptyListForBlankLine() {
    List<String> words = wordExtractor.extractWords("   ");

    assertEquals(List.of(), words);
  }

  @Test
  void extractWordsShouldSupportDigits() {
    List<String> words = wordExtractor.extractWords("abc 123 test42");

    assertEquals(List.of("abc", "123", "test42"), words);
  }
}