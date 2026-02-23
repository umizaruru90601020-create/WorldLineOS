package com.worldline.os.core.model;

import java.io.Serializable;

public class Boat implements Serializable {

    public int lane;            // 1〜6（進入）
    public double st;           // ST
    public double tenji;        // 展示タイム
    public double turn;         // 旋回力
    public double stretch;      // 伸び
    public double power;        // モーター出力
    public double stability;    // 安定性

    // 選手傾向（OS の攻め艇判定に必須）
    public double attack;       // 攻め傾向
    public double sashi;        // 差し傾向
    public double outside;      // 外攻め傾向
    public double weak;         // 弱さ（弱艇補正）
    public double strong;       // 強さ（強艇補正）

    // 展示 ST（OS の 7章補正で使用）
    public double tenjiSt;

    // 事故率（OS の 20章補正で使用）
    public double accidentRate;

    // その日の気配（OS の 20章補正で使用）
    public double condition;

    public Boat(int lane) {
        this.lane = lane;
    }
}