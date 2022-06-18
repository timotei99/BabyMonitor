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

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {

    private FragmentSettingsBinding binding;
    private SettingsRepository repo;
    private SharedPreferences sharedPreferences;
    private Preference syncBtn;
    private Preference logoutBtn;
    private SwitchPreference cameraPref;
    private SwitchPreference notificationPref;
    private SwitchPreference screenshotPref;
    private EditTextPreference screenshotFreqPref;
    private Preference.OnPreferenceChangeListener changeListener;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {

        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        repo= SettingsRepository.getInstance();

        sharedPreferences= requireContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        addPreferencesFromResource(R.xml.preference);

        findPreferences();
        setPreferenceValues();
        createChangeListener();
        setListeners();

    }

    private void createChangeListener(){
        changeListener = (preference, newValue) -> {
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
            //editor.apply();
            return true;
        };
    }

    private void findPreferences(){
        syncBtn = findPreference("syncBtn");
        logoutBtn = findPreference("logoutBtn");
        cameraPref = findPreference("camera_switch");
        notificationPref = findPreference("notification_switch");
        screenshotPref=findPreference("screenshot_switch");
        screenshotFreqPref=findPreference("screenshot_frequency");
    }

    private void setPreferenceValues(){
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
    }

    private void setListeners(){


        if(syncBtn!=null){
            syncBtn.setOnPreferenceClickListener(preference -> {
                startActivity(new Intent(requireContext(), PairingActivity.class));
                return true;
            });
        }

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

    @Override
    public void onPause() {
        editor.apply();
        super.onPause();
    }

}
