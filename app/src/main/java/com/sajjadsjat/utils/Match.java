package com.sajjadsjat.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Match {
    public static List<MatchResult> getMatchResults(String input, List<String> targets) {
        List<MatchResult> matchResults = new ArrayList<>();

        for (String target : targets) {
            double score = getMatchScore(target, input);
            matchResults.add(new MatchResult(target, score));
        }

        matchResults.sort((o1, o2) -> Double.compare(o2.score, o1.score));

        return matchResults;
    }

    public static double getMatchScore(String target, String input) {
        int n = target.length();
        int m = input.length();

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int[][] distance = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            distance[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (input.charAt(j - 1) == target.charAt(i - 1)) ? 0 : 1;
                distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1), distance[i - 1][j - 1] + cost);
            }
        }

        int maxDistance = Math.max(n, m);

        return 1.0 - ((double) distance[n][m] / (double) maxDistance);
    }

    public static class MatchResult {
        public String target;
        public double score;

        public MatchResult(String target, double score) {
            this.target = target;
            this.score = score;
        }
    }
}
