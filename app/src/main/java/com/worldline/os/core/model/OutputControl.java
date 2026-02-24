package com.worldline.os.core.model;

import com.worldline.os.core.CoreEngine;

import java.util.ArrayList;
import java.util.List;

public class OutputControl {

    // 出力1件分
    public static class OutputEntry {
        public Worldline worldline;
        public int rank;
        public double score;
        public String reason;

        public OutputEntry(Worldline wl, int rank, double score, String reason) {
            this.worldline = wl;
            this.rank = rank;
            this.score = score;
            this.reason = reason;
        }
    }

    public List<OutputEntry> outputs = new ArrayList<>();
    public int outputCount = 0;
    public String policy = "";

    // OutputEntry を追加
    public void add(Worldline wl, int rank, double score, String reason) {
        outputs.add(new OutputEntry(wl, rank, score, reason));
        outputCount = outputs.size();
    }

    // ---------------------------------------------------------
    // ★ パイプライン実行（唯一の入口）
    // ---------------------------------------------------------
    public static List<OutputEntry> run(String input) {

        CoreEngine engine = new CoreEngine();

        // ① parseInput
        ParsedInput parsed = engine.parseInput(input);

        // ② generateWorldlines
        WorldlineResult wlResult = engine.generateWorldlines(parsed);

        // ③ score
        ScoredResult scored = engine.score(wlResult);

        // ④ output
        OutputControl oc = engine.output(scored);

        return oc.outputs;
    }
}
