package com.worldline.os;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.worldline.os.ui.input.InputFragment;
import com.worldline.os.ui.result.ResultFragment;
import com.worldline.os.ui.saved.SavedFragment;
import com.worldline.os.ui.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.main_bottom_nav);

        // 初期表示：RESULT タブ
        replaceFragment(new ResultFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment target = null;

            int id = item.getItemId();

            if (id == R.id.nav_input) {
                target = new InputFragment();
            } else if (id == R.id.nav_result) {
                target = new ResultFragment();
            } else if (id == R.id.nav_saved) {
                target = new SavedFragment();
            } else if (id == R.id.nav_settings) {
                target = new SettingsFragment();
            }

            if (target != null) {
                replaceFragment(target);
                return true;
            }

            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }
}
