package com.worldline.os.core.model;

import com.worldline.os.core.CoreEngine;

import java.util.ArrayList;
import java.util.List;

public class OutputControl {

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

    public void add(Worldline wl, int rank, double score, String reason) {
        outputs.add(new OutputEntry(wl, rank, score, reason));
        outputCount = outputs.size();
    }

    // ---------------------------------------------------------
    // 公式 run() — CoreEngine を実行して OutputEntry を返す
    // ---------------------------------------------------------
    public static List<OutputEntry> run(String input) {

        CoreEngine engine = new CoreEngine();

        // 1. パース
        ParsedInput parsed = engine.parse(input);

        // 2. 世界線生成
        WorldlineResult wlResult = engine.generate(parsed);

        // 3. スコアリング
        ScoredResult scored = engine.score(wlResult);

        // 4. 出力制御
        OutputControl oc = engine.output(scored);

        return oc.outputs;
    }
}
