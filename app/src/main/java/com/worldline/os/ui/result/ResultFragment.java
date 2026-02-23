package com.worldline.os.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.worldline.os.R;
import com.worldline.os.core.OutputControl;
import com.worldline.os.core.model.Correction;
import com.worldline.os.core.model.Worldline;
import com.worldline.os.ui.SharedViewModel;

import java.util.List;

public class ResultFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private LinearLayout resultContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        resultContainer = view.findViewById(R.id.result_container);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // 入力テキストが更新されたら自動解析
        sharedViewModel.getInputText().observe(getViewLifecycleOwner(), text -> {
            if (text != null && !text.trim().isEmpty()) {
                runPipeline(text);
            }
        });
    }

    // -------------------------
    // 世界線生成パイプライン
    // -------------------------
    private void runPipeline(String input) {

        resultContainer.removeAllViews();

        List<OutputControl.OutputEntry> results = OutputControl.run(input);

        for (OutputControl.OutputEntry e : results) {
            addWorldlineCard(e);
        }
    }

    // -------------------------
    // 世界線カード生成（MainActivity から移植）
    // -------------------------
    private void addWorldlineCard(OutputControl.OutputEntry e) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View card = inflater.inflate(R.layout.worldline_card, resultContainer, false);

        TextView title = card.findViewById(R.id.wl_title);
        TextView info = card.findViewById(R.id.wl_info);
        TextView reason = card.findViewById(R.id.wl_reason);
        TextView logText = card.findViewById(R.id.wl_log_text);
        TextView toggle = card.findViewById(R.id.wl_log_toggle);
        LinearLayout logContainer = card.findViewById(R.id.wl_log_container);

        LinearLayout orderTags = card.findViewById(R.id.wl_order_tags);

        // -------------------------
        // 世界線タイプごとの色分け
        // -------------------------
        int bgColor = 0xFFFFFFFF;
        int tagColor = 0xFFCCCCCC;

        switch (e.worldline.type) {
            case "attack":
                bgColor = 0xFFFFDDDD;
                tagColor = 0xFFFF8888;
                break;
            case "sashi":
                bgColor = 0xFFDDDDFF;
                tagColor = 0xFF8888FF;
                break;
            case "outside":
                bgColor = 0xFFDDFFDD;
                tagColor = 0xFF66CC66;
                break;
            case "neutral":
                bgColor = 0xFFEEEEEE;
                tagColor = 0xFFBBBBBB;
                break;
            case "chaos":
                bgColor = 0xFFF0DDFF;
                tagColor = 0xFFCC88FF;
                break;
        }

        card.setBackgroundColor(bgColor);

        // -------------------------
        // order タグ
        // -------------------------
        orderTags.removeAllViews();

        for (Integer num : e.worldline.order) {

            TextView tag = new TextView(getContext());
            tag.setText(String.valueOf(num));
            tag.setTextSize(16);
            tag.setTextColor(0xFF000000);
            tag.setPadding(20, 10, 20, 10);
            tag.setBackgroundColor(tagColor);
            tag.setTypeface(null, android.graphics.Typeface.BOLD);

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            lp.setMargins(8, 0, 8, 0);
            tag.setLayoutParams(lp);

            orderTags.addView(tag);
        }

        // -------------------------
        // テキスト設定
        // -------------------------
        title.setText(e.worldline.type);
        info.setText("ランク: " + e.rank + "　スコア: " + String.format("%.1f", e.worldline.correctionScore));
        reason.setText(e.worldline.reason);

        StringBuilder sb = new StringBuilder();
        for (Correction c : e.worldline.logs) {
            sb.append("・").append(c.name)
              .append(" (").append(c.value).append(")")
              .append(" : ").append(c.reason)
              .append("\n");
        }
        logText.setText(sb.toString());

        toggle.setOnClickListener(v -> {
            if (logContainer.getVisibility() == View.GONE) {
                logContainer.setVisibility(View.VISIBLE);
                toggle.setText("▲ 補正ログを閉じる");
            } else {
                logContainer.setVisibility(View.GONE);
                toggle.setText("▼ 補正ログを開く");
            }
        });

        resultContainer.addView(card);
    }
}