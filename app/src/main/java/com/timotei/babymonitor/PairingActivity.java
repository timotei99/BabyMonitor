package com.timotei.babymonitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.timotei.babymonitor.databinding.ActivityPairingBinding;
import com.timotei.babymonitor.helpers.BluetoothHandler;
import com.timotei.babymonitor.helpers.MyBluetoothService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;


public class PairingActivity extends AppCompatActivity {

    private ActivityPairingBinding binding;
    BluetoothAdapter adapter;
    BluetoothSocket mmSocket;
    UUID uuid;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        adapter=BluetoothAdapter.getDefaultAdapter();
        binding=ActivityPairingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button btnSync= binding.btnSync;
        MyBluetoothService bt=new MyBluetoothService(new BluetoothHandler(),adapter);
        BluetoothDevice dev=null;
        Set<BluetoothDevice> devs=adapter.getBondedDevices();
        for(BluetoothDevice d : devs){
            if(d.getName().equals("DESKTOP-QRO5SGK")){
                Log.d("BLUETOOTH","Found device!! It has address: "+d.getAddress());
                dev=adapter.getRemoteDevice(d.getAddress());
            }
        }



        Log.d("BLUETOOTH","Uuids: "+dev.getUuids().clone()[0]);
        uuid=dev.getUuids().clone()[0].getUuid();

        /*if(dev!=null) {
            ConnectThread c = new ConnectThread(dev);
            c.start();
        }*/

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BLUETOOTH","Starting communication...");
               //bt.startCommunication(mmSocket);
            }
        });

        /*registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        bluetooth.enable();
        if (!bluetooth.isDiscovering()) {
            bluetooth.startDiscovery();
        }*/

    }

    private void manageMyConnectedSocket(BluetoothSocket mmSocket){
        this.mmSocket=mmSocket;
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e("BLUETOOTH", "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            adapter.cancelDiscovery();
            int cnt=0;
            while((cnt<5)&&(!mmSocket.isConnected())) {
                try {
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    Log.d("BLUETOOTH", "Trying to connect...");
                    mmSocket.connect();
                    Log.d("BLUETOOTH", "Connected!!");
                } catch (IOException connectException) {
                    // Unable to connect; close the socket and return.
                    try {
                        Log.d("BLUETOOTH", "Unable to connect! " + connectException.getMessage());
                        mmSocket.close();
                    } catch (IOException closeException) {
                        Log.e("BLUETOOTH", "Could not close the client socket", closeException);
                    }
                }
                cnt++;
            }

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.

                manageMyConnectedSocket(mmSocket);

        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("BLUETOOTH", "Could not close the client socket", e);
            }
        }
    }



    /*DataOutputStream os;

    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice;

            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            Toast.makeText(getApplicationContext(), "Discovered: " + remoteDeviceName + " address " + remoteDevice.getAddress(), Toast.LENGTH_SHORT).show();

            try{
                BluetoothDevice device = bluetooth.getRemoteDevice(remoteDevice.getAddress());

                Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});

                BluetoothSocket clientSocket =  (BluetoothSocket) m.invoke(device, 1);

                clientSocket.connect();

                os = new DataOutputStream(clientSocket.getOutputStream());

                new clientSock().start();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("BLUETOOTH", e.getMessage());
            }
        }
    };



    public class clientSock extends Thread {
        public void run () {
            try {
                Log.d("BLUETOOTH","Sending stuff...");
                os.writeBytes("ceau Andrei!"); // anything you want
                os.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
    }*/
}
