package com.worldline.os;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.worldline.os.core.model.Correction;
import com.worldline.os.core.model.Worldline;
import com.worldline.os.causal.WorldlineCausalView;

public class WorldlineDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worldline_detail);

        Worldline wl = (Worldline) getIntent().getSerializableExtra("worldline");

        TextView title = findViewById(R.id.detail_title);
        TextView info = findViewById(R.id.detail_info);
        TextView reason = findViewById(R.id.detail_reason);
        TextView logs = findViewById(R.id.detail_logs);

        WorldlineCausalView causalView = findViewById(R.id.detail_causal_graph);

        if (wl != null) {

            title.setText("世界線: " + wl.type);

            info.setText(
                    "スコア: " + String.format("%.1f", wl.correctionScore)
            );

            reason.setText("理由: " + wl.tag);

            // 補正ログ表示
            StringBuilder sb = new StringBuilder();
            for (Correction c : wl.logs) {
                sb.append("・")
                  .append(c.name)
                  .append(" (").append(c.value).append(")")
                  .append(" : ").append(c.reason)
                  .append("\n");
            }
            logs.setText(sb.toString());

            // ★ 因果グラフに世界線をセット
            causalView.setWorldline(wl);
        }
    }
}
