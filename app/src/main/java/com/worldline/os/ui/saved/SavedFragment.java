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

import com.worldline.os.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SavedFragment extends Fragment {

    private LinearLayout savedContainer;
    private TextView emptyText;

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
                requireActivity().getSharedPreferences("saved_worldlines", Context.MODE_PRIVATE);

        String json = prefs.getString("list", "[]");

        try {
            JSONArray arr = new JSONArray(json);

            if (arr.length() == 0) {
                emptyText.setVisibility(View.VISIBLE);
                return;
            }

            emptyText.setVisibility(View.GONE);

            for (int i = 0; i < arr.length(); i++) {

                JSONObject obj = arr.getJSONObject(i);

                String title = obj.optString("type", "worldline");
                double score = obj.optDouble("score", 0);

                TextView item = new TextView(getContext());
                item.setText("[" + title + "] score: " + score);
                item.setTextSize(16);
                item.setPadding(20, 20, 20, 20);

                savedContainer.addView(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            emptyText.setVisibility(View.VISIBLE);
        }
    }
}