package ru.spbu.apcyb.svp.tasks;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChangeMakerTest {

    @Test
    void amount5_denoms3_2_has_one_combo() {
        ChangeMaker cm = new ChangeMaker();
        List<List<Integer>> res = cm.findAllCombinations(5, List.of(3, 2));

        assertEquals(1, res.size());
        assertEquals(List.of(3, 2), res.get(0));
    }

    @Test
    void amount4_denoms2_1_has_three_combos() {
        ChangeMaker cm = new ChangeMaker();
        List<List<Integer>> res = cm.findAllCombinations(4, List.of(2, 1));

        assertEquals(3, res.size());
        assertTrue(res.contains(List.of(2, 2)));
        assertTrue(res.contains(List.of(2, 1, 1)));
        assertTrue(res.contains(List.of(1, 1, 1, 1)));
    }

    @Test
    void invalid_denoms_are_ignored_and_duplicates_removed() {
        ChangeMaker cm = new ChangeMaker();
        List<List<Integer>> res = cm.findAllCombinations(
                3,
                Arrays.asList(null, 0, -5, 3, 3)
        );

        assertEquals(1, res.size());
        assertEquals(List.of(3), res.get(0));
    }

    @Test
    void negative_amount_throws() {
        ChangeMaker cm = new ChangeMaker();
        assertThrows(IllegalArgumentException.class,
                () -> cm.findAllCombinations(-1, List.of(1, 2)));
    }
}