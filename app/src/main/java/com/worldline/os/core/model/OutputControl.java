package com.worldline.os.core.model;

import java.util.ArrayList;
import java.util.List;

public class OutputControl {

    public static class OutputEntry {
        public Worldline worldline;   // 採用された世界線
        public int rank;              // ランク
        public double score;          // スコア
        public String reason;         // 採用理由（ログ）

        public OutputEntry(Worldline wl, int rank, double score, String reason) {
            this.worldline = wl;
            this.rank = rank;
            this.score = score;
            this.reason = reason;
        }
    }

    public List<OutputEntry> outputs = new ArrayList<>();
    public int outputCount = 0;       // 最終的に出力した本数
    public String policy = "";        // 出力方針（例：強気 / 弱気 / 標準）

    // 世界線を追加
    public void add(Worldline wl, int rank, double score, String reason) {
        outputs.add(new OutputEntry(wl, rank, score, reason));
        outputCount = outputs.size();
    }
}