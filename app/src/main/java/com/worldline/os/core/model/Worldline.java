package com.worldline.os.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Worldline implements Serializable {

    public String type;
    public List<Integer> order = new ArrayList<>();
    public List<Correction> logs = new ArrayList<>();
    public double correctionScore = 0.0;

    public Worldline(String type) {
        this.type = type;
    }
}