package com.worldline.os.ui.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.worldline.os.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
    }
}
