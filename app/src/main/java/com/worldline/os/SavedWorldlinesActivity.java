package com.worldline.os;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.worldline.os.core.model.Worldline;
import com.worldline.os.core.model.Correction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SavedWorldlinesActivity extends AppCompatActivity {

    private static final String PREF_NAME = "saved_worldlines";
    private static final String KEY_LIST = "worldline_list";

    private LinearLayout container;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        setContentView(container);

        List<Worldline> list = loadWorldlines();
        display(list);
    }

    // ---------------------------------------------------------
    // JSON 読み込み
    // ---------------------------------------------------------
    private List<Worldline> loadWorldlines() {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_LIST, "");

        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<Worldline>>(){}.getType();
        return gson.fromJson(json, type);
    }

    // ---------------------------------------------------------
    // JSON 保存
    // ---------------------------------------------------------
    private void saveWorldlines(List<Worldline> list) {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String json = gson.toJson(list);
        ed.putString(KEY_LIST, json);
        ed.apply();
    }

    // ---------------------------------------------------------
    // UI 表示
    // ---------------------------------------------------------
    private void display(List<Worldline> list) {

        container.removeAllViews();

        for (Worldline wl : list) {

            TextView tv = new TextView(this);
            tv.setTextSize(18);
            tv.setPadding(20, 20, 20, 20);

            StringBuilder sb = new StringBuilder();
            sb.append("タイプ: ").append(wl.type).append("\n");
            sb.append("スコア: ").append(String.format("%.1f", wl.correctionScore)).append("\n");
            sb.append("理由: ").append(wl.reason).append("\n\n");

            sb.append("補正ログ:\n");
            for (Correction c : wl.logs) {
                sb.append("・").append(c.name)
                        .append(" (").append(c.value).append(")")
                        .append(": ").append(c.reason)
                        .append("\n");
            }

            tv.setText(sb.toString());
            container.addView(tv);
        }
    }
}
