package ru.spbu.apcyb.svp.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class Main {

    public static void main(String[] args) throws IOException {
        List<Integer> input = readAllInts();
        if (input.isEmpty()) {
            return;
        }

        int amount = input.get(0);
        List<Integer> denoms = input.subList(1, input.size());

        ChangeMaker changeMaker = new ChangeMaker();
        List<List<Integer>> combos = changeMaker.findAllCombinations(amount, denoms);

        System.out.println(combos.size());
        for (List<Integer> combo : combos) {
            System.out.println(joinWithSpaces(combo));
        }
    }

    private static List<Integer> readAllInts() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Integer> res = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                res.add(Integer.parseInt(st.nextToken()));
            }
        }
        return res;
    }

    private static String joinWithSpaces(List<Integer> values) {
        if (values.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(values.get(i));
        }
        return sb.toString();
    }
}