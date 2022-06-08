package com.timotei.babymonitor.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.firebase.auth.FirebaseAuth;
import com.timotei.babymonitor.LoginActivity;
import com.timotei.babymonitor.PairingActivity;
import com.timotei.babymonitor.R;
import com.timotei.babymonitor.databinding.FragmentSettingsBinding;

public class SettingsFragment extends PreferenceFragmentCompat {

    private FragmentSettingsBinding binding;
    private SettingsRepository repo;


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {

        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        repo= SettingsRepository.getInstance();

        SharedPreferences sharedPreferences= getContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        addPreferencesFromResource(R.xml.preference);

        Preference syncBtn = findPreference("syncBtn");
        if(syncBtn!=null){
            syncBtn.setOnPreferenceClickListener(preference -> {
                startActivity(new Intent(requireContext(), PairingActivity.class));
                return true;
            });
        }

        Preference logoutBtn = findPreference("logoutBtn");
        if(logoutBtn!=null) {
            logoutBtn.setOnPreferenceClickListener(preference -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            });
        }

        final SwitchPreference cameraPref = findPreference("camera_switch");
        final SwitchPreference notificationPref = findPreference("notification_switch");
        final SwitchPreference screenshotPref=findPreference("screenshot_switch");
        final EditTextPreference screenshotFreqPref=findPreference("screenshot_frequency");

        if (cameraPref != null) {
            cameraPref.setChecked(sharedPreferences.getBoolean("camera", true));
        }
        if(notificationPref!=null){
            notificationPref.setChecked(sharedPreferences.getBoolean("notifications",true));
        }
        if(screenshotPref!=null){
            screenshotPref.setChecked(sharedPreferences.getBoolean("screenshot",true));
        }
        if(screenshotFreqPref!=null){
            screenshotFreqPref.setText(sharedPreferences.getString("frequency","60"));
        }


        Preference.OnPreferenceChangeListener changeListener = (preference, newValue) -> {
            String key= preference.getKey();
            boolean value=newValue.toString().equals("true");
            switch(key) {
                case "camera_switch":
                    if (value) {
                        repo.updateSetting("camera","on");
                    } else {
                        repo.updateSetting("camera","off");
                    }
                    editor.putBoolean("camera", value);
                break;
                case "notification_switch":
                    if (value) {
                        repo.updateSetting("notifications","on");
                    } else {
                        repo.updateSetting("notifications","off");
                    }
                    editor.putBoolean("notifications", value);
                break;
                case "screenshot_switch":
                    if (value) {
                        repo.updateSetting("screenshot_status","on");
                    } else {
                        repo.updateSetting("screenshot_status","off");
                    }
                    editor.putBoolean("screenshot", value);
                break;
                case "screenshot_frequency":
                    repo.updateSetting("screenshot_frequency",newValue.toString());
                    editor.putString("frequency",newValue.toString());
                break;
            }
            editor.apply();
            return true;
        };

        if (cameraPref != null) {
            cameraPref.setOnPreferenceChangeListener(changeListener);
        }
        if(notificationPref!=null){
            notificationPref.setOnPreferenceChangeListener(changeListener);
        }
        if(screenshotPref!=null){
            screenshotPref.setOnPreferenceChangeListener(changeListener);
        }
        if (screenshotFreqPref!=null){
            screenshotFreqPref.setOnPreferenceChangeListener(changeListener);
        }

    }


}
