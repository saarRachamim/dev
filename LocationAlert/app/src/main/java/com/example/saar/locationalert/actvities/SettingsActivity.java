package com.example.saar.locationalert.actvities;

import android.os.Bundle;
import com.example.saar.locationalert.R;

public class SettingsActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
