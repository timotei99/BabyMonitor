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
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timotei.babymonitor.R;
import com.timotei.babymonitor.data.model.VideoCamera;
import com.timotei.babymonitor.databinding.FragmentHomeBinding;
import com.timotei.babymonitor.databinding.FragmentSettingsBinding;
import com.timotei.babymonitor.ui.home.HomeViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private SettingsRepository repo = SettingsRepository.getInstance();



    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {


        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        binding=FragmentSettingsBinding.inflate(getLayoutInflater());




        addPreferencesFromResource(R.xml.preference);
        final SwitchPreference cameraPref = findPreference("camera_switch");
        if(cameraPref!=null) {
            if(!repo.getVideoCamera().getStatus().equals("on")) {
                cameraPref.setChecked(false);
            }
        }

        Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("SETTINGS_FRAGMENT","Camera setting changed!");
                String val="";
                if(newValue.toString().equals("true")) {
                    repo.setVideoCameraStatus("on");
                }
                else{
                    repo.setVideoCameraStatus("off");
                }
                return true;
            }
        };

        if(cameraPref!=null) {
            cameraPref.setOnPreferenceChangeListener(changeListener);
        }


    }



}
