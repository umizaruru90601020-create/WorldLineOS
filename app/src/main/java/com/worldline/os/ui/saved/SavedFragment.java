package com.worldline.os.ui.saved;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.worldline.os.R;
import com.worldline.os.core.model.Worldline;
import com.worldline.os.core.model.Correction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    private LinearLayout savedContainer;
    private TextView emptyText;

    private static final String PREF_NAME = "saved_worldlines";
    private static final String KEY_LIST = "worldline_list";

    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        savedContainer = view.findViewById(R.id.saved_container);
        emptyText = view.findViewById(R.id.saved_empty_text);

        loadSavedWorldlines();
    }

    private void loadSavedWorldlines() {

        savedContainer.removeAllViews();

        SharedPreferences prefs =
                requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String json = prefs.getString(KEY_LIST, "");

        if (json == null || json.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        Type type = new TypeToken<List<Worldline>>(){}.getType();
        List<Worldline> list = gson.fromJson(json, type);

        if (list == null || list.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        emptyText.setVisibility(View.GONE);

        for (Worldline wl : list) {

            TextView item = new TextView(getContext());
            item.setTextSize(16);
            item.setPadding(20, 20, 20, 20);

            StringBuilder sb = new StringBuilder();
            sb.append("[").append(wl.type).append("]\n");
            sb.append("score: ").append(String.format("%.1f", wl.correctionScore)).append("\n");
            sb.append("reason: ").append(wl.reason).append("\n\n");

            sb.append("logs:\n");
            for (Correction c : wl.logs) {
                sb.append("・").append(c.name)
                        .append(" (").append(c.value).append(")")
                        .append(": ").append(c.reason)
                        .append("\n");
            }

            item.setText(sb.toString());
            savedContainer.addView(item);
        }
    }
}
