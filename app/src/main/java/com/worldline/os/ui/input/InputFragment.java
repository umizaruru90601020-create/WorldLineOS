package com.worldline.os.ui.input;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.worldline.os.R;
import com.worldline.os.ui.SharedViewModel;

public class InputFragment extends Fragment {

    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        EditText inputField = view.findViewById(R.id.input_edit_text);
        Button analyzeBtn = view.findViewById(R.id.input_analyze_button);

        analyzeBtn.setOnClickListener(v -> {

            String text = inputField.getText().toString();
            sharedViewModel.setInputText(text);

            // RESULT タブに切り替え
            BottomNavigationView nav = requireActivity().findViewById(R.id.main_bottom_nav);
            nav.setSelectedItemId(R.id.nav_result);
        });
    }
}