package com.worldline.os.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParsedInput implements Serializable {

    public String raw;

    // 6艇分のデータ（本物の OS と同じ構造）
    public List<Boat> boats = new ArrayList<>();
}