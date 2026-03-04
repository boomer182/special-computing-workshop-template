package ru.spbu.apcyb.svp.tasks;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

  @Test
  void main_prints_expected_output_for_example_4_2_1() throws Exception {
    String input = "4\n2 1\n";
    String output = runMain(input);

    // Важно: порядок строк должен совпадать с логикой сортировки (2 2, затем 2 1 1, затем 1 1 1 1)
    String expected = String.join(System.lineSeparator(),
        "3",
        "2 2",
        "2 1 1",
        "1 1 1 1",
        ""
    );
    assertEquals(expected, output);
  }

  @Test
  void main_with_no_solution_prints_zero_only() throws Exception {
    String input = "3\n2\n";
    String output = runMain(input);

    String expected = String.join(System.lineSeparator(),
        "0",
        ""
    );
    assertEquals(expected, output);
  }

  @Test
  void main_with_empty_input_prints_nothing() throws Exception {
    String output = runMain("");
    assertEquals("", output);
  }

  private String runMain(String stdin) throws Exception {
    PrintStream oldOut = System.out;
    java.io.InputStream oldIn = System.in;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream(stdin.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
      Main.main(new String[0]);
      return out.toString(StandardCharsets.UTF_8);
    } finally {
      System.setOut(oldOut);
      System.setIn(oldIn);
    }
  }
}