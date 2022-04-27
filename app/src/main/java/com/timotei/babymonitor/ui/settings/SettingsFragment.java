package com.timotei.babymonitor.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.timotei.babymonitor.R;
import com.timotei.babymonitor.databinding.FragmentHomeBinding;
import com.timotei.babymonitor.databinding.FragmentSettingsBinding;
import com.timotei.babymonitor.ui.home.HomeViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;

    /*@NonNull
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }*/

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {

        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        binding=FragmentSettingsBinding.inflate(getLayoutInflater());



        addPreferencesFromResource(R.xml.preference);

        final Preference cameraPref = findPreference("camera_switch");
        Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("SETTINGS_FRAGMENT","Camera setting changed!");
                return true;
            }
        };


        if(cameraPref!=null) {
            cameraPref.setOnPreferenceChangeListener(changeListener);
        }
    }

}
