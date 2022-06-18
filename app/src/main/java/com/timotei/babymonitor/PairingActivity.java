package com.timotei.babymonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.InetAddresses;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.timotei.babymonitor.databinding.ActivityPairingBinding;
import com.timotei.babymonitor.helpers.SyncHandler;

import org.json.JSONException;

import java.net.Inet4Address;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


@RequiresApi(api = Build.VERSION_CODES.Q)
public class PairingActivity extends AppCompatActivity {

    private ActivityPairingBinding binding;
    private String IP;
    private String uid;
    private ImageButton qrBtn;
    private Context context;
    private SyncHandler syncHandler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding=ActivityPairingBinding.inflate(getLayoutInflater());
        qrBtn= binding.imageButton;
        setContentView(binding.getRoot());

        context = this;
        uid= FirebaseAuth.getInstance().getUid();
        syncHandler = new SyncHandler();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan a QR code");
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);

        qrBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                barcodeLauncher.launch(options);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean validateIP(){

        if(TextUtils.isEmpty(IP)){
            Toast.makeText(this,"Not a valid destination",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!InetAddresses.isNumericAddress(IP)){
            Toast.makeText(this,"Not a valid destination!",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void extractIp(String url){
        String temp=url.split("//")[1];
        String temp2=temp.split("/")[0];
        IP=temp2.split(":")[0];
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    showMessage("Scanning failed!");
                } else {
                    showMessage("Scanned successfully. Now syncing...");
                    try {
                        extractIp(result.getContents());
                        editor.putString("IpAddress",IP);
                        if(validateIP()){
                            syncHandler.sendIdToRaspberry(result.getContents(),uid,context);
                        }
                        else{
                            showMessage("Invalid QR!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    public void onPause() {
        editor.apply();
        super.onPause();
    }

    private void showMessage(String message){
        Toast.makeText(
                PairingActivity.this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}
