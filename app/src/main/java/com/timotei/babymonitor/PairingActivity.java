package com.timotei.babymonitor;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.timotei.babymonitor.databinding.ActivityPairingBinding;
import com.timotei.babymonitor.helpers.SyncHandler;


public class PairingActivity extends AppCompatActivity {

    private ActivityPairingBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding=ActivityPairingBinding.inflate(getLayoutInflater());
        Button syncBtn=binding.btnSync;
        setContentView(binding.getRoot());

        Context context = this;
        String uid= FirebaseAuth.getInstance().getUid();
        SyncHandler syncHandler= new SyncHandler();

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncHandler.sendIdToRaspberry(uid,context);
            }
        });

    }


}
