package ru.spbu.apcyb.svp.tasks;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Result of tan computations.
 */
public final class TanComputationResult {

  private final List<Double> values;
  private final int count;
  private final long elapsedNanos;

  /**
   * Creates a result.
   *
   * @param values computed values
   * @param count number of processed inputs
   * @param elapsedNanos elapsed time in nanoseconds
   */
  public TanComputationResult(List<Double> values, int count, long elapsedNanos) {
    this.values = Collections.unmodifiableList(Objects.requireNonNull(values, "values"));
    this.count = count;
    this.elapsedNanos = elapsedNanos;
  }

  public List<Double> getValues() {
    return values;
  }

  public int getCount() {
    return count;
  }

  public long getElapsedNanos() {
    return elapsedNanos;
  }

  public long getElapsedMillis() {
    return elapsedNanos / 1_000_000L;
  }
}