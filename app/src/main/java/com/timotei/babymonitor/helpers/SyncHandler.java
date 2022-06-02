package com.timotei.babymonitor.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SyncHandler {

    public SyncHandler() {
    }

    public void sendIdToRaspberry(String uid, Context context){
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        String url="http://79.114.83.150:9000/user?uid=test123";
        //TODO: put real uid

        StringRequest request=new StringRequest(Request.Method.POST, url,
                response -> Log.d("SYNCREQUEST","Successful"),
                error -> Log.e("SYNCREQUEST",error.getMessage())
                );
        requestQueue.add(request);
    }
}
