package com.timotei.babymonitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.timotei.babymonitor.databinding.ActivityRoomConditionBinding;
import com.timotei.babymonitor.ui.settings.SettingsRepository;

import java.util.Set;

public class RoomConditionActivity extends AppCompatActivity {

    private ActivityRoomConditionBinding binding;
    private SettingsRepository repo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        repo = SettingsRepository.getInstance();


        binding=ActivityRoomConditionBinding.inflate(getLayoutInflater());
        final TextView temp = binding.temperature;
        final TextView humidity=binding.humidity;
        final TextView pulse=binding.pulse;
        temp.setText(getString(R.string.temperature,repo.getTemperatureSensor().getDegrees()));
        humidity.setText(getString(R.string.humidity,repo.getHumiditySensor().getValue()));
        pulse.setText(getString(R.string.heart_rate,repo.getPulseSensor().getRate()));

        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
