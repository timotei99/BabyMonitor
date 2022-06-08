package com.timotei.babymonitor.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.timotei.babymonitor.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

public class SyncHandler {

    public SyncHandler() {
    }

    public void sendIdToRaspberry(String url, String uid, Context context) throws JSONException {
        RequestQueue requestQueue= Volley.newRequestQueue(context);

        JSONObject msg=new JSONObject();
        msg.put("uid",uid);

        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST, url, msg,
                response -> {Log.d("SYNC_REQUEST", response.toString());
                    Toast.makeText(context,"Sync succeeded!",Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, HomeActivity.class));},
                error ->{Log.d("SYNC_REQUEST", error.getMessage());
                    Toast.makeText(context,"Sync failed!",Toast.LENGTH_LONG).show();}
        );

        requestQueue.add(req);
    }
}
