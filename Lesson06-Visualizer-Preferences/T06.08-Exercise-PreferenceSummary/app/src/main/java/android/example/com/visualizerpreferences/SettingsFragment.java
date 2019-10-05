package android.example.com.visualizerpreferences;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_visualizer);

        PreferenceScreen prefScreen = getPreferenceScreen();
        SharedPreferences sharedPref = prefScreen.getSharedPreferences();

        for (int i = 0; i < prefScreen.getPreferenceCount(); i++) {
            Preference pref = prefScreen.getPreference(i);
            if (pref instanceof ListPreference) {
                setPreferenceSummary(pref, sharedPref.getString(pref.getKey(), ""));
            }
        }

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref != null && pref instanceof ListPreference) {
            String value = sharedPreferences.getString(key, "");
            setPreferenceSummary(pref, value);
        }
    }

    private void setPreferenceSummary(Preference pref, String value) {
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            int prefIndex = listPref.findIndexOfValue(value);
            if (prefIndex >= 0)
                listPref.setSummary(listPref.getEntries()[prefIndex]);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}