package com.diezsiete.lscapp.ui.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.diezsiete.lscapp.R;

/**
 * Settings fragment
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);
    }
}
