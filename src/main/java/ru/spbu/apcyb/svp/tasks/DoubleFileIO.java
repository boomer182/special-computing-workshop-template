package ru.spbu.apcyb.svp.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reads/writes doubles from/to files.
 */
public final class DoubleFileIO {

  /**
   * Reads doubles from file. Numbers can be separated by whitespace.
   *
   * @param input input file path
   * @return list of parsed doubles
   * @throws IOException if IO error occurs
   */
  public List<Double> readDoubles(Path input) throws IOException {
    Objects.requireNonNull(input, "input");

    List<Double> out = new ArrayList<>();
    try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
      String line;
      while ((line = reader.readLine()) != null) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
          continue;
        }
        String[] parts = trimmed.split("\\s+");
        for (String p : parts) {
          if (!p.isEmpty()) {
            out.add(Double.parseDouble(p));
          }
        }
      }
    }
    return out;
  }

  /**
   * Writes computed values and metadata.
   *
   * @param output output file
   * @param computed computed values
   * @param count count of inputs
   * @param elapsedSingleMs elapsed time of single-threaded run in ms
   * @param elapsedMultiMs elapsed time of multi-threaded run in ms
   * @throws IOException if IO error occurs
   */
  public void writeResults(
      Path output,
      List<Double> computed,
      int count,
      long elapsedSingleMs,
      long elapsedMultiMs
  ) throws IOException {
    Objects.requireNonNull(output, "output");
    Objects.requireNonNull(computed, "computed");

    Path parent = output.toAbsolutePath().normalize().getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }

    try (BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
      for (Double v : computed) {
        writer.write(String.valueOf(v));
        writer.newLine();
      }
      writer.write("COUNT=" + count);
      writer.newLine();
      writer.write("ELAPSED_MS_SINGLE=" + elapsedSingleMs);
      writer.newLine();
      writer.write("ELAPSED_MS_MULTI=" + elapsedMultiMs);
      writer.newLine();
    }
  }

  /**
   * Writes benchmark report.
   *
   * @param output output report file
   * @param lines report lines
   * @throws IOException if IO error occurs
   */
  public void writeReport(Path output, List<String> lines) throws IOException {
    Objects.requireNonNull(output, "output");
    Objects.requireNonNull(lines, "lines");

    Path parent = output.toAbsolutePath().normalize().getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }
    Files.write(output, lines, StandardCharsets.UTF_8);
  }
}