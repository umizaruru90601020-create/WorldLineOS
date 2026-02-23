package com.worldline.os.core.order;

import com.worldline.os.core.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderGenerator {

    // ---------------------------------------------------------
    // ★ ログ追加関数
    // ---------------------------------------------------------
    private void addLog(Worldline wl, String name, double value, String reason) {
        wl.logs.add(new Correction(name, value, reason));
    }

    // ---------------------------------------------------------
    // ★ Block 6：攻め艇スコア（本物の式＋ログ）
    // ---------------------------------------------------------

    private double scoreST(Boat b, Worldline wl) {
        double v = (0.20 - b.st) * 120;
        addLog(wl, "ST補正", v, "STが速いほど加点");
        return v;
    }

    private double scoreTenji(Boat b, Worldline wl) {
        double v = (7.00 - b.tenji) * 80;
        addLog(wl, "展示補正", v, "展示タイムが速いほど加点");
        return v;
    }

    private double scoreTurn(Boat b, Worldline wl) {
        double v = b.turn * 1.2;
        addLog(wl, "旋回補正", v, "旋回力が高いほど加点");
        return v;
    }

    private double scoreStretch(Boat b, Worldline wl) {
        double v = b.stretch * 1.0;
        addLog(wl, "伸び補正", v, "伸びが強いほど加点");
        return v;
    }

    private double scoreAttackTendency(Boat b, Worldline wl) {
        double v = b.attack * 1.5;
        addLog(wl, "攻め傾向補正", v, "攻め傾向が強いほど加点");
        return v;
    }

    private double scoreOutsidePotential(Boat b, Worldline wl) {
        double v = b.outside * 1.0;
        addLog(wl, "外攻め補正", v, "外攻め可能性が高いほど加点");
        return v;
    }

    private double scoreAccident(Boat b, Worldline wl) {
        double v = (1.0 - b.accidentRate) * 60;
        addLog(wl, "事故率補正", v, "事故率が低いほど加点");
        return v;
    }

    private double scoreCondition(Boat b, Worldline wl) {
        double v = b.condition * 0.8;
        addLog(wl, "気配補正", v, "その日の気配が良いほど加点");
        return v;
    }

    // ---------------------------------------------------------
    // ★ Block 7：7章補正（本物の式＋ログ）
    // ---------------------------------------------------------

    private double corr7A(Boat b, Worldline wl) {
        double v = b.attack * 0.8 + b.turn * 0.3 + (0.20 - b.st) * 50;
        addLog(wl, "7-A 攻め補正", v, "攻め艇が成立しやすい状況");
        return v;
    }

    private double corr7S(Boat b, Worldline wl) {
        double v = b.sashi * 0.7 + b.turn * 0.4 - b.stretch * 0.2;
        addLog(wl, "7-S 差し補正", v, "差し艇が成立しやすい状況");
        return v;
    }

    private double corr7O(Boat b, Worldline wl) {
        double v = b.outside * 0.9 + b.stretch * 0.5;
        addLog(wl, "7-O 外補正", v, "外攻めが成立しやすい状況");
        return v;
    }

    private double corr7W(Boat b, Worldline wl) {
        double v = b.weak * 0.6 + (7.00 - b.tenji) * 30;
        addLog(wl, "7-W 弱艇補正", v, "弱艇が残る状況");
        return v;
    }

    private double corr7G(Boat b, Worldline wl) {
        double v = b.strong * 0.8 + b.turn * 0.5;
        addLog(wl, "7-G 強艇補正", v, "強艇が押し切る状況");
        return v;
    }

    private double corr7_2Z_G(Boat b, Worldline wl) {
        double v = b.outside * 0.7 + b.stretch * 0.6 - b.accidentRate * 40;
        addLog(wl, "7-2Z-G 外残り補正", v, "外枠が残る状況");
        return v;
    }

    private double corr7_X_A(Boat b, Worldline wl) {
        double v = b.outside * 0.5 + b.attack * 0.5;
        addLog(wl, "7-X-A 外攻め→攻め補正", v, "外攻め成立→攻め艇連動");
        return v;
    }

    private double corr7_X_G(Boat b, Worldline wl) {
        double v = b.outside * 0.4 + b.strong * 0.6;
        addLog(wl, "7-X-G 外攻め→強艇補正", v, "外攻め成立→強艇連動");
        return v;
    }

    private double correction7(Boat b, Worldline wl) {
        return corr7A(b, wl) + corr7S(b, wl) + corr7O(b, wl)
                + corr7W(b, wl) + corr7G(b, wl)
                + corr7_2Z_G(b, wl) + corr7_X_A(b, wl) + corr7_X_G(b, wl);
    }

    // ---------------------------------------------------------
    // ★ Block 20：20章補正（本物の式＋ログ）
    // ---------------------------------------------------------

    private double corr20_A_X(Boat b, Worldline wl) {
        double v = b.attack * 0.6 + b.outside * 0.8 + b.stretch * 0.5;
        addLog(wl, "20-A-X 攻め→外攻め補正", v, "攻め艇成立→外攻め連動");
        return v;
    }

    private double corr20_X_A(Boat b, Worldline wl) {
        double v = b.outside * 0.7 + b.attack * 0.6 + b.turn * 0.4;
        addLog(wl, "20-X-A 外攻め→攻め補正", v, "外攻め成立→攻め艇連動");
        return v;
    }

    private double corr20_Z(Boat b, Worldline wl) {
        double v = b.weak * 0.5 + (7.00 - b.tenji) * 20 + (0.20 - b.st) * 30;
        addLog(wl, "20-Z 残り補正", v, "弱艇が残る状況");
        return v;
    }

    private double corr20_W(Boat b, Worldline wl) {
        double v = b.weak * 0.7 + (1.0 - b.accidentRate) * 40;
        addLog(wl, "20-W 弱艇浮上補正", v, "弱艇が浮上する状況");
        return v;
    }

    private double corr20_G(Boat b, Worldline wl) {
        double v = b.strong * 0.8 + b.turn * 0.5 + (7.00 - b.tenji) * 20;
        addLog(wl, "20-G 強艇補正", v, "強艇が押し切る状況");
        return v;
    }

    private double correction20(Boat b, Worldline wl) {
        return corr20_A_X(b, wl) + corr20_X_A(b, wl)
                + corr20_Z(b, wl) + corr20_W(b, wl) + corr20_G(b, wl);
    }

    // ---------------------------------------------------------
    // ★ 世界線スコア（correctionScore）
    // ---------------------------------------------------------
    private double calcWorldlineScore(List<Boat> boats, Worldline wl) {
        double total = 0;
        for (Boat b : boats) {
            total += calcSemeScore(b, wl);
        }
        return total;
    }

    // ---------------------------------------------------------
    // ★ 攻め艇スコア（総合）
    // ---------------------------------------------------------
    private double calcSemeScore(Boat b, Worldline wl) {
        return scoreST(b, wl) + scoreTenji(b, wl) + scoreTurn(b, wl)
                + scoreStretch(b, wl) + scoreAttackTendency(b, wl)
                + scoreOutsidePotential(b, wl) + scoreAccident(b, wl)
                + scoreCondition(b, wl)
                + correction7(b, wl) + correction20(b, wl);
    }

    // ---------------------------------------------------------
    // ★ 世界線生成（スコア付き）
    // ---------------------------------------------------------

    public Worldline makeAttack(ParsedInput p) {
        Worldline wl = new Worldline("attack");

        List<Boat> sorted = new ArrayList<>(p.boats);
        sorted.sort((a, b) -> Double.compare(calcSemeScore(b, wl), calcSemeScore(a, wl)));

        List<Integer> order = new ArrayList<>();
        for (Boat b : sorted) order.add(b.lane);

        wl.order = order;
        wl.reason = "攻め艇が最も優勢。";

        wl.correctionScore = calcWorldlineScore(sorted, wl);
        return wl;
    }

    public Worldline makeSashi(ParsedInput p) {
        Worldline wl = new Worldline("sashi");

        List<Boat> sorted = new ArrayList<>(p.boats);
        sorted.sort((a, b) -> Double.compare(
                (b.sashi * 1.2 + b.turn * 0.8),
                (a.sashi * 1.2 + a.turn * 0.8)
        ));

        List<Integer> order = new ArrayList<>();
        for (Boat b : sorted) order.add(b.lane);

        wl.order = order;
        wl.reason = "差し艇が旋回力で優勢。";

        wl.correctionScore = calcWorldlineScore(sorted, wl);
        return wl;
    }

    public Worldline makeOutside(ParsedInput p) {
        Worldline wl = new Worldline("outside");

        List<Boat> sorted = new ArrayList<>(p.boats);
        sorted.sort((a, b) -> Double.compare(
                (b.outside * 1.5 + b.stretch * 1.2),
                (a.outside * 1.5 + a.stretch * 1.2)
        ));

        List<Integer> order = new ArrayList<>();
        for (Boat b : sorted) order.add(b.lane);

        wl.order = order;
        wl.reason = "外伸びが強く、外攻め補正が成立。";

        wl.correctionScore = calcWorldlineScore(sorted, wl);
        return wl;
    }

    public Worldline makeNeutral(ParsedInput p) {
        Worldline wl = new Worldline("neutral");

        List<Boat> sorted = new ArrayList<>(p.boats);
        sorted.sort((a, b) -> Double.compare(
                (b.condition * 0.5 + b.turn * 0.3),
                (a.condition * 0.5 + a.turn * 0.3)
        ));

        List<Integer> order = new ArrayList<>();
        for (Boat b : sorted) order.add(b.lane);

        wl.order = order;
        wl.reason = "平均的な能力値が反映される世界線。";

        wl.correctionScore = calcWorldlineScore(sorted, wl);
        return wl;
    }

    public Worldline makeChaos(ParsedInput p) {
        Worldline wl = new Worldline("chaos");

        List<Boat> sorted = new ArrayList<>(p.boats);
        sorted.sort((a, b) -> Double.compare(
                (b.strong * 0.6 - b.accidentRate * 50),
                (a.strong * 0.6 - a.accidentRate * 50)
        ));

        List<Integer> order = new ArrayList<>();
        for (Boat b : sorted) order.add(b.lane);

        wl.order = order;
        wl.reason = "強艇補正と事故率が支配する混沌世界線。";

        wl.correctionScore = calcWorldlineScore(sorted, wl);
        return wl;
    }
}