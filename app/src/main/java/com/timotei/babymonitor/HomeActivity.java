package com.timotei.babymonitor;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.timotei.babymonitor.data.SensorsRepository;
import com.timotei.babymonitor.databinding.ActivityHomeBinding;
import com.timotei.babymonitor.ui.home.HomeRepository;
import com.timotei.babymonitor.ui.notifications.NotificationRepository;
import com.timotei.babymonitor.ui.settings.SettingsRepository;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private SensorsRepository sensorsRepo;
    private HomeRepository homeRepository;
    private String uid;
    private NavController navController;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sensorsRepo = SensorsRepository.getInstance();
        homeRepository = HomeRepository.getInstance();
        uid = FirebaseAuth.getInstance().getUid();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupWithNavController(binding.navView, navController);
        subscribeToNotificationTopic();
        sensorsRepo.getSensorData();

    }

    private void subscribeToNotificationTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("user_"+uid);
    }
}