package com.worldline.os.causal;

import com.worldline.os.core.model.Worldline;
import java.util.ArrayList;
import java.util.List;

public class ScoreCalculate {

    // 世界線とスコアを保持する最小単位
    public static class Entry {
        public Worldline worldline;
        public double score;

        public Entry(Worldline wl, double score) {
            this.worldline = wl;
            this.score = score;
        }
    }

    // Entry のリスト
    public List<Entry> entries = new ArrayList<>();

    // 世界線を追加
    public void add(Worldline wl, double score) {
        entries.add(new Entry(wl, score));
    }

    // スコア順に並べ替え（降順）
    public void sort() {
        entries.sort((a, b) -> Double.compare(b.score, a.score));
    }
}
