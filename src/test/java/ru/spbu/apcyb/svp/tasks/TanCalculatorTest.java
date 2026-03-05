package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoubleUnaryOperator;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TanCalculator}.
 */
public class TanCalculatorTest {

  @Test
  void parallel_matches_single() {
    TanCalculator calc = new TanCalculator(10, Math::tan);

    List<Double> input = List.of(0.0, 0.5, -1.0, 2.0, 10.0);

    TanComputationResult single = calc.computeSingle(input);
    TanComputationResult multi = calc.computeParallel(input);

    assertEquals(single.getCount(), multi.getCount());
    assertEquals(single.getValues().size(), multi.getValues().size());

    for (int i = 0; i < input.size(); i++) {
      assertEquals(single.getValues().get(i), multi.getValues().get(i), 1e-12);
    }
  }

  @Test
  void parallel_never_exceeds_max_in_flight() {
    AtomicInteger active = new AtomicInteger(0);
    AtomicInteger maxActive = new AtomicInteger(0);

    DoubleUnaryOperator slowOp = x -> {
      int now = active.incrementAndGet();
      maxActive.updateAndGet(prev -> Math.max(prev, now));
      try {
        TimeUnit.MILLISECONDS.sleep(10);
      } catch (InterruptedException ignored) {
        Thread.currentThread().interrupt();
      } finally {
        active.decrementAndGet();
      }
      return Math.tan(x);
    };

    TanCalculator calc = new TanCalculator(10, slowOp);

    List<Double> input = new java.util.ArrayList<>();
    for (int i = 0; i < 200; i++) {
      input.add((double) i);
    }

    calc.computeParallel(input);

    assertTrue(maxActive.get() <= 10, "Max active was " + maxActive.get());
  }

  @Test
  void compute_single_on_empty_returns_empty() {
    TanCalculator calc = new TanCalculator(10, Math::tan);

    TanComputationResult r = calc.computeSingle(List.of());

    assertEquals(0, r.getCount());
    assertTrue(r.getValues().isEmpty());
  }

  @Test
  void compute_parallel_count_matches_input_size() {
    TanCalculator calc = new TanCalculator(10, Math::tan);
    List<Double> input = List.of(0.0, 1.0, -1.0, 2.0);

    TanComputationResult r = calc.computeParallel(input);

    assertEquals(input.size(), r.getCount());
    assertEquals(input.size(), r.getValues().size());
  }
}