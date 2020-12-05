package com.fypcuiatk.feeclearanceapp.Fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.fypcuiatk.feeclearanceapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_settings_screen, rootKey)
    }
}