package com.timotei.babymonitor.helpers;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.logging.LogRecord;

public class BluetoothHandler extends Handler {

    @Override
    public void handleMessage(@NonNull Message msg) {
        Log.d("BLUETOOTH","Received: "+msg.toString());
    }


}
