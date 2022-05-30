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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Vibrator vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[] { 500, 1000, 1000, 1000, 1000,1000,1000 },-1);

        binding = ActivityNotificationBinding.inflate(getLayoutInflater());

        onNewIntent(getIntent());

        String notificationType=getIntent().getExtras().getString("id");
        final TextView text=binding.message;
        final Button cancelBtn = binding.btnCancel;
        final Button playBtn=binding.btnPlay;
        final TextView noises=binding.noisesMessage;
        final ImageView img=binding.babyImg;
        Context context=this;

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsRepository.getInstance().startWhiteNoises();
            }
        });

        switch(notificationType){
            case "crying": text.setText("Baby is crying!!");
                            playBtn.setVisibility(View.VISIBLE);
                            noises.setVisibility(View.VISIBLE);
            break;
            case "face_covered": text.setText("Baby has face covered!");
                                binding.getRoot().setBackgroundResource(R.color.beige);
            break;
            default: text.setText("Go check baby!!");
        }

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

        cancelBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(context,HomeActivity.class));
                return true;
            }
        });

        cancelBtn.setOnClickListener(v -> startActivity(new Intent(context,HomeActivity.class)));
        setContentView(binding.getRoot());
    }

    /*@Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String notificationType=getIntent().getExtras().getString("type");
        Log.d("NOTIFICATION","Here "+notificationType);
    }*/
}
