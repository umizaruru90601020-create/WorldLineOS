package com.worldline.os.core.model;

import java.util.ArrayList;
import java.util.List;

public class WorldlineResult {

    public List<Worldline> worldlines = new ArrayList<>();

    public void add(Worldline wl) {
        worldlines.add(wl);
    }
}