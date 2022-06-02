package com.timotei.babymonitor;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.timotei.babymonitor.databinding.ActivityRoomConditionBinding;
import com.timotei.babymonitor.helpers.ThingspeakUpdater;
import com.timotei.babymonitor.ui.settings.SettingsRepository;

import org.w3c.dom.Text;

import java.util.Set;

public class RoomConditionActivity extends AppCompatActivity {

    private ActivityRoomConditionBinding binding;
    private SettingsRepository repo;
    private ThingspeakUpdater updater;
    private String tare="0";
    private String total="0";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        repo = SettingsRepository.getInstance();
        Context context=this;

        binding=ActivityRoomConditionBinding.inflate(getLayoutInflater());
        updater=new ThingspeakUpdater();
        final TextView temp = binding.temperature;
        final TextView humidity=binding.humidity;
        final TextView pulse=binding.pulse;
        final TextView weight= binding.weight;
        final TextView ppm=binding.value;
        final TextView text=binding.qualifier;
        final ProgressBar progressBar = binding.progressBar2;
        final Button weigh=binding.btnWeigh;
        final Button back=binding.btnBack;

        back.setOnClickListener(v -> startActivity(new Intent(context,HomeActivity.class)));
        weigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Baby weigh-in");
                alertDialog.setMessage("Pick up baby and press 'Tare'\nThen put back baby and press 'Weigh'");
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Tare", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Weigh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"Button pressed",Toast.LENGTH_SHORT).show();
                        tare=repo.getScale().getValue();
                        alertDialog.setMessage("Now put baby back and press 'Weigh'");
                    }
                });
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        total=repo.getScale().getValue();
                        Toast.makeText(context,"Getting the data...",Toast.LENGTH_SHORT).show();
                        float w=(Integer.parseInt(total)-Integer.parseInt(tare));
                        String weight=String.valueOf(w/1000);
                        alertDialog.setMessage("New weight: "+weight+" kg");
                        updater.push((float) (w/1000),context);
                        repo.storeLastWeight(weight);
                    }
                });
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);

            }
        });

        temp.setText(getString(R.string.temperature,repo.getTemperatureSensor().getDegrees()));
        humidity.setText(getString(R.string.humidity,repo.getHumiditySensor().getValue()));
        pulse.setText(getString(R.string.heart_rate,repo.getPulseSensor().getRate()));
        weight.setText(getString(R.string.weight,repo.getScale().getLast_weight()));

        int value=Integer.parseInt(repo.getAir().getValue());

        ppm.setText(getString(R.string.air_quality,value));
        Log.d("AIR_QUALITY","Set progress to: "+value/21);
        int progress=value/21; // I pick 2100 as a maximum value

        //progressBar.setProgress(progress);
        progressBar.setMax(100);
        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                progressBar.setProgress(value);
            }
        });

        animator.start();

        if(value<450){
            text.setText("Good");
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        }
        else if(value<800){
            text.setText("Normal");
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_green)));
        }
        else if(value<1000){
            text.setText("Acceptable");
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        }
        else if(value<2000){
            text.setText("Poor");
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
        }
        else{
            text.setText("Very poor");
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
