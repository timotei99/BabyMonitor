package com.timotei.babymonitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import com.timotei.babymonitor.databinding.ActivityGraphsBinding;

public class GraphsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ActivityGraphsBinding binding;
    private String[] urls = {"https://thingspeak.com/channels/1747533/charts/1?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15",
                            "https://thingspeak.com/channels/1747533/charts/2?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15",
                            "https://thingspeak.com/channels/1747533/charts/3?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15",
                            "https://thingspeak.com/channels/1747533/charts/4?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15",
                            "https://thingspeak.com/channels/1747533/charts/4?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15"};

    String[] graphs = { "Temperature", "Humidity", "Pulse", "Weight", "CO2"};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityGraphsBinding.inflate(getLayoutInflater());
        Context context=this;
        WebView webView=binding.webView;
        Button btnBack=binding.btnBack;
        Spinner spinner=binding.spinner;
        spinner.setOnItemSelectedListener(this);


        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,graphs);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(adapter);

        setContentView(binding.getRoot());

        webView.loadUrl(urls[0]);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        btnBack.setOnClickListener(v -> startActivity(new Intent(context,HomeActivity.class)));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        WebView webView=binding.webView;
        webView.loadUrl(urls[position]);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        WebView webView=binding.webView;
        webView.loadUrl(urls[0]);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }
}
