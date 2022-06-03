package com.timotei.babymonitor.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SyncHandler {

    public SyncHandler() {
    }

    public void sendIdToRaspberry(String uid, Context context) throws JSONException {
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        String url="http://79.114.83.150:5000/user";

        JSONObject msg=new JSONObject();
        msg.put("uid",uid);

        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST, url, msg,
                response -> Log.d("SYNCREQUEST", response.toString()),
                error -> Log.d("SYNCREQUEST", error.getMessage())
        );

        requestQueue.add(req);
    }
}
