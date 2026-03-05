package ru.spbu.apcyb.svp.tasks;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Entry point for Task 3: recursive directory listing to a file.
 */
public final class Main {

  private Main() {
    // utility class
  }

  /**
   * Arguments:
   * <ol>
   *   <li>Path to directory</li>
   *   <li>Path to output file</li>
   * </ol>
   *
   * @param args program args
   */
  public static void main(String[] args) throws IOException {
    if (args == null || args.length != 2) {
      throw new IllegalArgumentException("Expected 2 args: <directory> <outputFile>");
    }

    Path directory = Path.of(args[0]);
    Path outputFile = Path.of(args[1]);

    DirectoryTreeWriter writer = new DirectoryTreeWriter();
    writer.write(directory, outputFile);
  }
}