package com.timotei.babymonitor.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timotei.babymonitor.LoginActivity;
import com.timotei.babymonitor.R;
import com.timotei.babymonitor.data.model.VideoCamera;
import com.timotei.babymonitor.databinding.FragmentHomeBinding;
import com.timotei.babymonitor.databinding.FragmentSettingsBinding;
import com.timotei.babymonitor.ui.home.HomeViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private FirebaseAuth mAuth;
    private SettingsRepository repo = SettingsRepository.getInstance();


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {


        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());

        SharedPreferences sharedPreferences= getContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        addPreferencesFromResource(R.xml.preference);

        Preference button = findPreference("logoutBtn");
        button.setOnPreferenceClickListener(preference -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        });

        final SwitchPreference cameraPref = findPreference("camera_switch");
        final SwitchPreference micPref = findPreference("audio_switch");
        /*if (cameraPref != null) {
            if (!repo.getVideoCamera().getStatus().equals("on")) {
                cameraPref.setChecked(false);
            }
        }*/
        cameraPref.setChecked(sharedPreferences.getBoolean("camera",true));
        micPref.setChecked(sharedPreferences.getBoolean("audio",true));


        Preference.OnPreferenceChangeListener cameraChangeListener = (preference, newValue) -> {
            String key= preference.getKey();
            if (newValue.toString().equals("true")) {
                repo.setVideoCameraStatus("on");
                editor.putBoolean("camera",true);
            } else {
                repo.setVideoCameraStatus("off");
                editor.putBoolean("camera",false);
            }
            editor.apply();
            return true;
        };

        if (cameraPref != null) {
            cameraPref.setOnPreferenceChangeListener(cameraChangeListener);
        }


    }


}
