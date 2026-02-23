package com.worldline.os.core;

import com.worldline.os.core.model.*;
import com.worldline.os.core.order.OrderGenerator;

import java.util.ArrayList;
import java.util.List;

public class CoreEngine {

    private final OrderGenerator orderGen = new OrderGenerator();

    // ---------------------------------------------------------
    // ① parse() → ParsedInput を作る
    // ---------------------------------------------------------
    public ParsedInput parse(String raw) {
        return parseInput(raw);
    }

    // 旧 parseInput() を内部処理として残す
    private ParsedInput parseInput(String raw) {
        ParsedInput p = new ParsedInput();
        p.raw = raw;

        // ★ 仮データ（後で INPUT_SCREEN から実データを入れる）
        for (int i = 1; i <= 6; i++) {
            Boat b = new Boat(i);

            b.st = 0.15 + (i * 0.01);
            b.tenji = 6.70 + (i * 0.02);
            b.turn = 50 + i;
            b.stretch = 50 + (6 - i);
            b.power = 40 + (i * 2);
            b.stability = 30 + (i * 3);

            b.attack = (i == 1) ? 80 : 40;
            b.sashi = (i == 3) ? 80 : 40;
            b.outside = (i >= 5) ? 80 : 30;
            b.weak = (i == 6) ? 70 : 30;
            b.strong = (i == 2) ? 70 : 30;

            b.tenjiSt = 0.20 + (i * 0.01);
            b.accidentRate = (i == 4) ? 0.10 : 0.02;
            b.condition = 50 + (i * 5);

            p.boats.add(b);
        }

        return p;
    }

    // ---------------------------------------------------------
    // ② generate() → 世界線生成
    // ---------------------------------------------------------
    public WorldlineResult generate(ParsedInput p) {
        return generateWorldlines(p);
    }

    // 旧 generateWorldlines() を内部処理として残す
    private WorldlineResult generateWorldlines(ParsedInput p) {
        WorldlineResult result = new WorldlineResult();
        result.worldlines = new ArrayList<>();

        result.worldlines.add(orderGen.makeAttack(p));
        result.worldlines.add(orderGen.makeSashi(p));
        result.worldlines.add(orderGen.makeOutside(p));
        result.worldlines.add(orderGen.makeNeutral(p));
        result.worldlines.add(orderGen.makeChaos(p));

        return result;
    }

    // ---------------------------------------------------------
    // ③ score() → 世界線スコアで順位付け
    // ---------------------------------------------------------
    public ScoredResult score(WorldlineResult wlResult) {
        ScoredResult s = new ScoredResult();
        s.entries = new ArrayList<>();

        // correctionScore の降順で並べ替え
        wlResult.worldlines.sort(
                (a, b) -> Double.compare(b.correctionScore, a.correctionScore)
        );

        int rank = 1;
        for (Worldline wl : wlResult.worldlines) {
            s.entries.add(new ScoredResult.Entry(
                    wl,
                    wl.correctionScore,
                    rank,
                    "世界線スコアによる順位付け"
            ));
            rank++;
        }

        return s;
    }

    // ---------------------------------------------------------
    // ④ output() → 出力形式に変換
    // ---------------------------------------------------------
    public OutputControl output(ScoredResult s) {
        OutputControl oc = new OutputControl();
        oc.policy = "topN";
        oc.outputCount = s.entries.size();
        oc.outputs = new ArrayList<>();

        for (ScoredResult.Entry e : s.entries) {
            oc.outputs.add(new OutputControl.OutputEntry(
        e.worldline,
        e.rank,
        e.score,
        e.reason
));
        }

        return oc;
    }
}
