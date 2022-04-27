package com.timotei.babymonitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.timotei.babymonitor.databinding.ActivityStreamBinding;

public class StreamActivity extends AppCompatActivity {

    private ActivityStreamBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityStreamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final WebView webView = binding.video;

        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        startStream(webView,"https://www.youtube.com/watch?v=r6AQOLwgKDU");
    }

    public void startStream(WebView vw,String url){
        vw.loadUrl(url);
    }

}
