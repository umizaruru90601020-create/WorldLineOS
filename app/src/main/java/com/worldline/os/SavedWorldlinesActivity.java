package com.worldline.os;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldline.os.core.model.Worldline;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class SavedWorldlinesActivity extends Activity {

    private LinearLayout container;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_worldlines);

        container = findViewById(R.id.saved_container);

        ArrayList<Worldline> list = loadSavedWorldlines();

        for (Worldline wl : list) {
            addWorldlineBlock(wl);
        }
    }

    private ArrayList<Worldline> loadSavedWorldlines() {
        SharedPreferences sp = getSharedPreferences("saved_worldlines", MODE_PRIVATE);
        String json = sp.getString("list", "[]");
        return gson.fromJson(json, new TypeToken<ArrayList<Worldline>>(){}.getType());
    }

    private void addWorldlineBlock(Worldline wl) {
        TextView tv = new TextView(this);
        tv.setText(
                "世界線: " + wl.type + "\n" +
                "スコア: " + String.format("%.1f", wl.correctionScore) + "\n" +
                "理由: " + wl.reason + "\n" +
                "-----------------------------\n"
        );
        tv.setTextSize(16);
        tv.setPadding(0, 0, 0, 24);
        container.addView(tv);
    }
}