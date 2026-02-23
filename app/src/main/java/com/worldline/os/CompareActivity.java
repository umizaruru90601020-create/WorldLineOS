package com.worldline.os;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.worldline.os.core.model.Correction;
import com.worldline.os.core.model.Worldline;

public class CompareActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        LinearLayout left = findViewById(R.id.left_container);
        LinearLayout right = findViewById(R.id.right_container);

        Worldline wl1 = (Worldline) getIntent().getSerializableExtra("wl1");
        Worldline wl2 = (Worldline) getIntent().getSerializableExtra("wl2");

        if (wl1 != null) addWorldlineBlock(left, wl1);
        if (wl2 != null) addWorldlineBlock(right, wl2);
    }

    private void addWorldlineBlock(LinearLayout container, Worldline wl) {

        TextView title = new TextView(this);
        title.setText("世界線: " + wl.type);
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 12);
        container.addView(title);

        TextView score = new TextView(this);
        score.setText("スコア: " + String.format("%.1f", wl.correctionScore));
        score.setTextSize(16);
        score.setPadding(0, 0, 0, 12);
        container.addView(score);

        TextView reason = new TextView(this);
        reason.setText("理由:\n" + wl.tag);
        reason.setTextSize(14);
        reason.setPadding(0, 0, 0, 12);
        container.addView(reason);

        TextView orderTitle = new TextView(this);
        orderTitle.setText("着順:");
        orderTitle.setTextSize(16);
        orderTitle.setPadding(0, 0, 0, 8);
        container.addView(orderTitle);

        for (int i = 0; i < wl.order.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText((i + 1) + "着 → " + wl.order.get(i) + "号艇");
            tv.setTextSize(14);
            container.addView(tv);
        }

        TextView logTitle = new TextView(this);
        logTitle.setText("\n補正ログ:");
        logTitle.setTextSize(16);
        logTitle.setPadding(0, 16, 0, 8);
        container.addView(logTitle);

        for (Correction c : wl.logs) {
            TextView tv = new TextView(this);
            tv.setText("・" + c.name + " (" + c.value + ") : " + c.reason);
            tv.setTextSize(12);
            container.addView(tv);
        }
    }
}
