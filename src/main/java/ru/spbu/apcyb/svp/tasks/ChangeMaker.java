package ru.spbu.apcyb.svp.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ChangeMaker {

    public List<List<Integer>> findAllCombinations(int amount, List<Integer> denominations) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        if (denominations == null || denominations.isEmpty()) {
            return List.of();
        }

        List<Integer> denoms = normalizeDenominations(denominations);
        if (denoms.isEmpty()) {
            return List.of();
        }

        List<List<Integer>> result = new ArrayList<>();
        backtrack(amount, denoms, 0, new ArrayList<>(), result);
        return result;
    }

    private List<Integer> normalizeDenominations(List<Integer> denominations) {
        return denominations.stream()
                .filter(Objects::nonNull)
                .filter(d -> d > 0)
                .distinct()
                .sorted(Collections.reverseOrder())
                .toList();
    }

    private void backtrack(int remaining,
                           List<Integer> denoms,
                           int index,
                           List<Integer> current,
                           List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        if (index >= denoms.size() || remaining < 0) {
            return;
        }

        int denom = denoms.get(index);
        int maxCount = remaining / denom;

        for (int k = maxCount; k >= 0; k--) {
            for (int i = 0; i < k; i++) {
                current.add(denom);
            }

            backtrack(remaining - k * denom, denoms, index + 1, current, result);

            for (int i = 0; i < k; i++) {
                current.remove(current.size() - 1);
            }
        }
    }
}