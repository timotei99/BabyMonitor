package com.timotei.babymonitor.helpers;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.timotei.babymonitor.R;

public class ThingspeakUpdater {

    public ThingspeakUpdater() {
    }

    public void push(Float weight, Context context){
        RequestQueue queue= Volley.newRequestQueue(context);
        String API_KEY=context.getString(R.string.THINGSPEAK_WRITE_API_KEY);
        String url="https://api.thingspeak.com/update?api_key="+API_KEY+"&field4="+weight;

        StringRequest request=new StringRequest(Request.Method.GET, url,
                response -> Log.d("THINGSPEAK", "Response is: " + response),
                error -> {
                    Log.e("THINGSPEAK", "Response is: " + error.networkResponse);
                });
        queue.add(request);
    }
}
