package com.worldline.os.core.model;

import java.util.ArrayList;
import java.util.List;

public class ScoredResult {

    public static class Entry {
        public Worldline worldline;   // 世界線そのもの
        public double score;          // 最終スコア（補正後）
        public int rank;              // ランク（1位〜）

        public Entry(Worldline wl, double score) {
            this.worldline = wl;
            this.score = score;
        }
    }

    public List<Entry> entries = new ArrayList<>();

    // 世界線を追加
    public void add(Worldline wl) {
        entries.add(new Entry(wl, wl.correctionScore));
    }

    // スコア順に並べ替え
    public void sort() {
        entries.sort((a, b) -> Double.compare(b.score, a.score));

        int r = 1;
        for (Entry e : entries) {
            e.rank = r++;
        }
    }
}