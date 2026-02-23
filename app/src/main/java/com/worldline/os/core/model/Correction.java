package com.worldline.os.core.model;

public class Correction {

    public String name;        // 補正名（例：7章補正、外攻め補正など）
    public double value;       // 補正値（+0.3 など）
    public String reason;      // 補正理由（ログ用）

    public Correction(String name, double value, String reason) {
        this.name = name;
        this.value = value;
        this.reason = reason;
    }
}