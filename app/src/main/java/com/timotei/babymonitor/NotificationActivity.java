package com.timotei.babymonitor;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.timotei.babymonitor.databinding.ActivityNotificationBinding;
import com.timotei.babymonitor.ui.settings.SettingsRepository;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private TextView message;
    private Button cancelBtn;
    private Button playBtn;
    private TextView noises;
    private ImageView img;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityNotificationBinding.inflate(getLayoutInflater());

        onNewIntent(getIntent());

        String notificationType=getIntent().getExtras().getString("id");
        message=binding.message;
        cancelBtn = binding.btnCancel;
        playBtn=binding.btnPlay;
        noises=binding.noisesMessage;
        img=binding.babyImg;
        Context context=this;

        setListeners();
        setAlertMessage(notificationType);
        startAnimation();

        setContentView(binding.getRoot());
    }

    public void setListeners(){
        playBtn.setOnClickListener(v -> SettingsRepository.getInstance().startWhiteNoises());
        cancelBtn.setOnClickListener(v -> startActivity(new Intent(this,HomeActivity.class)));
    }

    private void setAlertMessage(String notificationType) {
        switch (notificationType) {
            case "crying":
                startVibration();
                message.setText("Baby is crying!!");
                img.setImageResource(R.drawable.crying_256px);
                playBtn.setVisibility(View.VISIBLE);
                noises.setVisibility(View.VISIBLE);
                break;
            case "faceCovered":
                startVibration();
                message.setText("Baby's face is covered! Go check!");
                setColor(R.color.red);
                break;
            case "eyesOpen":
                message.setText("Baby's eyes are open!");
                img.setImageResource(R.drawable.baby_boy);
                setColor(R.color.beige);
                playBtn.setVisibility(View.VISIBLE);
                noises.setVisibility(View.VISIBLE);
                break;
            case "bodyUncovered":
                message.setText("Baby is not covered at all!");
                setColor(R.color.baby_blue);
                break;
            case "notFacingUp":
                message.setText("Baby is not facing up! Go fix harmful posture!");
                setColor(R.color.light_red);
                break;
            case "onSide":
                message.setText("Baby is sleeping on one side! Go fix harmful posture!");
                setColor(R.color.light_red);
                break;
            case "legsFullyVisible":
                message.setText("Baby legs are visible!");
                setColor(R.color.baby_yellow);
                break;
            default:
                message.setText("Go check baby!!");
        }
    }

    private void setColor(int color){
        binding.getRoot().setBackgroundResource(color);
    }

    private void startVibration(){
        Vibrator vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[] { 500, 1000, 1000, 1000, 1000,1000,1000 },-1);
    }

    private void startAnimation(){
        Animation zoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        Animation zoomOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);

        img.startAnimation(zoomIn);
        zoomIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                img.startAnimation(zoomOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                img.startAnimation(zoomIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

}
