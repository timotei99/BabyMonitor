package com.timotei.babymonitor;

import android.content.Context;
import android.net.InetAddresses;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.timotei.babymonitor.databinding.ActivityPairingBinding;
import com.timotei.babymonitor.helpers.SyncHandler;

import org.json.JSONException;

import java.net.Inet4Address;
import java.util.Objects;
import java.util.regex.Pattern;


public class PairingActivity extends AppCompatActivity {

    private ActivityPairingBinding binding;
    private String IP;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding=ActivityPairingBinding.inflate(getLayoutInflater());
        Button syncBtn=binding.btnSync;
        EditText etIP=binding.ipAddress;
        setContentView(binding.getRoot());


        Context context = this;
        String uid= FirebaseAuth.getInstance().getUid();
        SyncHandler syncHandler= new SyncHandler();

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                IP= Objects.requireNonNull(etIP.getText().toString());
                //validateIP(etIP);
                try {
                    syncHandler.sendIdToRaspberry(uid,context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void validateIP(EditText etIP){

        if(TextUtils.isEmpty(IP)){
            etIP.setError("IP cannot be empty!");
            etIP.requestFocus();
        }
        else if(!InetAddresses.isNumericAddress(IP)){
            etIP.setError("Not a valid IP!");
            etIP.requestFocus();
        }
    }


}
