package com.timotei.babymonitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.timotei.babymonitor.databinding.ActivityGraphsBinding;

public class GraphsActivity extends AppCompatActivity{

    private ActivityGraphsBinding binding;
    private WebView webView;
    private Button btnBack;
    private Spinner dataSpinner;
    private Spinner typeSpinner;
    private Context context;
    private String channelID;
    private SharedPreferences sharedPreferences;
    private final String[] graphs = { "Temperature (°C)", "Humidity (%)","Air quality (ppm)","Weight"};
    private final String[] types = {"line","bar","column","spline","step"};
    private int parameter=1;
    private int type=0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityGraphsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context=this;
        webView=binding.webView;
        btnBack=binding.btnBack;
        dataSpinner=binding.spinnerData;
        typeSpinner = binding.spinnerType;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        channelID=sharedPreferences.getString("channelID","1747533");

        dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parameter=position+1;
                setUrl();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parameter=1;
                setUrl();
            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=position;
                setUrl();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type=0;
                setUrl();
            }
        });

        ArrayAdapter<String> adapterData=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,graphs);
        adapterData.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dataSpinner.setAdapter(adapterData);

        ArrayAdapter<String> adapterType=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,types);
        adapterType.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner.setAdapter(adapterType);

        setUrl();
        setListeners();
    }

    private void setListeners(){
        btnBack.setOnClickListener(v -> startActivity(new Intent(context,HomeActivity.class)));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUrl(){
        String url = "https://thingspeak.com/channels/"
                +channelID
                +"/charts/" +parameter
                +"?bgcolor=%23ffffff&"
                +"color=%23d62020&"
                +"dynamic=true&"
                +"results=1000&"
                +"type="+ types[type]
                +"&update=15";

        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }
}
