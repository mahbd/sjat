package com.sajjadsjat.utils;

import java.util.ArrayList;
import java.util.List;

public class ScoreMatch {
    public static List<String> filter(String input, List<String> targets) {
        List<MatchResult> matchResults = new ArrayList<>();

        for (String target : targets) {
            String temTarget = target.toLowerCase();
            double score = getMatchScore(temTarget, input);
            if (temTarget.contains(input)) {
                score += 1;
                for (int i = 0; i < temTarget.length(); i++) {
                    if (input.charAt(0) == temTarget.charAt(i)) {
                        score += temTarget.length() - i;
                        break;
                    }
                }
            }
            matchResults.add(new MatchResult(target, score));
        }

        matchResults.sort((o1, o2) -> Double.compare(o2.score, o1.score));
        List<String> results = new ArrayList<>();
        for (MatchResult matchResult : matchResults) {
            results.add(matchResult.target);
        }

        return results;
    }

    public static double getMatchScore(String target, String input) {
        int n = target.length();
        int m = input.length();

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        double[][] distance = new double[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            distance[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if ((target.charAt(i - 1) == input.charAt(j - 1))) {
                    distance[i][j] = distance[i - 1][j - 1];
                    continue;
                }
                distance[i][j] = Math.max(distance[i - 1][j] + 1, distance[i][j - 1] + 1);
            }
        }

        double maxDistance = Math.max(n, m);

        return 1.0 - (distance[n][m] / maxDistance);
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
