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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.timotei.babymonitor.data.SensorsRepository;
import com.timotei.babymonitor.databinding.ActivityRoomConditionBinding;
import com.timotei.babymonitor.helpers.ThingspeakUpdater;
import com.timotei.babymonitor.ui.settings.SettingsRepository;

import org.w3c.dom.Text;

import java.util.Set;

public class RoomConditionActivity extends AppCompatActivity {

    private ActivityRoomConditionBinding binding;
    private SensorsRepository repo;
    private ThingspeakUpdater updater;
    private String tare="0";
    private String total="0";
    private TextView temp;
    private TextView humidity;
    private TextView pulse;
    private TextView weight;
    private TextView ppm;
    private TextView textStatus;
    private ProgressBar progressBar;
    private Button weigh;
    private Button back;
    private ImageButton refresh;
    private Context context;
    private ValueAnimator animator;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        repo = SensorsRepository.getInstance();
        context=this;
        binding=ActivityRoomConditionBinding.inflate(getLayoutInflater());
        updater=new ThingspeakUpdater();
        temp = binding.temperature;
        humidity=binding.humidity;
        pulse=binding.pulse;
        weight= binding.weight;
        ppm=binding.value;
        textStatus=binding.qualifier;
        progressBar = binding.progressBar2;
        weigh=binding.btnWeigh;
        back=binding.btnBack;
        refresh=binding.refreshBtn;

        setListeners();
        setParameters();

        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setParameters(){
        temp.setText(getString(R.string.temperature,repo.getSensors().getTemperature()));
        humidity.setText(getString(R.string.humidity,repo.getSensors().getHumidity()));
        pulse.setText(getString(R.string.heart_rate,repo.getSensors().getHeart_rate()));
        weight.setText(getString(R.string.weight,repo.getSensors().getLast_weight()));

        int value=Integer.parseInt(repo.getSensors().getAir_quality());
        ppm.setText(getString(R.string.air_quality,value));
        int progress=value/21; // I pick 2100 as a maximum value

        progressBar.setMax(100);
        animator = ValueAnimator.ofInt(0, progress);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(1000);
        animator.addUpdateListener(valueAnimator -> {
            int value1 = (int) valueAnimator.getAnimatedValue();
            progressBar.setProgress(value1);
        });

        animator.start();

        setBarColorAndText(value);
    }

    private void setListeners(){
        back.setOnClickListener(v -> startActivity(new Intent(context,HomeActivity.class)));
        weigh.setOnClickListener(v -> {
            startAlertDialog();
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setParameters();
            }
        });
    }

    private void startAlertDialog(){
        // create alert dialog
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Baby weigh-in");
        alertDialog.setMessage("Pick up baby and press 'Tare'\nThen put back baby and press 'Weigh'");
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Tare", (dialog, which) -> {});
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Weigh", (dialog, which) -> {});
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {});
        alertDialog.show();

        // implement 'Tare' button logic
        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v1 -> {
            tare=repo.getSensors().getCurrent_weight();
            alertDialog.setMessage("Now put baby back and press 'Weigh'");
        });
        // implement 'Weigh' button logic
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v12 -> {
            total=repo.getSensors().getCurrent_weight();
            Toast.makeText(context,"Getting the data...",Toast.LENGTH_SHORT).show();
            // check for correct weight sampling
            float sampleTare, sampleTotal;
            try {
                sampleTare = Float.parseFloat(tare);
                sampleTotal = Float.parseFloat(total);
                if (sampleTare >= sampleTotal) {
                    showErrorMessage("1");
                    alertDialog.hide();
                } else {
                    //compute the new weight as total minus tare
                    // 'sampleTotal' is the weight at the moment 'Weigh' button was pressed
                    // 'sampleTare'  is the weight at the moment 'Tare' was pressed
                    float w = (sampleTotal - sampleTare);
                    String weight = String.valueOf(w / 1000);
                    alertDialog.setMessage("New weight: " + weight + " kg");
                    // update  ThingSpeak 'Weight' field and database field 'last_weight'
                    updateNewWeight(w, weight);
                }
            } catch (NullPointerException | NumberFormatException e) {
                showErrorMessage("2");
            }
        });
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }

    private void setBarColorAndText(int value){
        if(value<450){
            textStatus.setText("Good");
            setColor(R.color.green);
        }
        else if(value<800){
            textStatus.setText("Normal");
            setColor(R.color.light_green);
        }
        else if(value<1000){
            textStatus.setText("Acceptable");
            setColor(R.color.yellow);
        }
        else if(value<2000){
            textStatus.setText("Poor");
            setColor(R.color.light_red);
        }
        else{
            textStatus.setText("Very poor");
            setColor(R.color.red);
        }

    }

    private void setColor(int color){
        progressBar.setProgressTintList(
                ColorStateList.valueOf(getResources().getColor(color))
        );
    }

    private void updateNewWeight(Float w,String weight){
        updater.push((float) (w/1000),context);
        repo.storeLastWeight(weight);
    }

    private void showErrorMessage(String msg){
        Toast.makeText(context, "Error getting correct weight!" +
                " Please follow the steps. "+msg, Toast.LENGTH_LONG).show();
    }
}
