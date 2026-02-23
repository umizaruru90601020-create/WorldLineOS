package com.worldline.os.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Worldline implements Serializable {

    public String type;                 // 世界線タイプ（attack / sashi / outside / neutral / chaos）
    public String tag;                  // UI 表示用タグ（攻め・差し・外・中立・混沌）
    public List<Integer> order;         // 着順
    public List<Correction> logs;       // 補正ログ
    public double correctionScore;      // 補正後スコア

    public Worldline(String type) {
        this.type = type;
        this.tag = type;                // とりあえず type をそのままタグにする（後で UI で変換してもOK）
        this.order = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.correctionScore = 0.0;
    }

    // 補正を追加する（ログも残す）
    public void addCorrection(Correction c) {
        logs.add(c);
        correctionScore += c.value;
    }
}
