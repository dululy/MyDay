package com.example.myday;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingNotification extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_notification_preference);
    }
}
