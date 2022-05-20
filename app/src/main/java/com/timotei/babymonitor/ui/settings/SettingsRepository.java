package com.timotei.babymonitor.ui.settings;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timotei.babymonitor.data.model.HumiditySensor;
import com.timotei.babymonitor.data.model.PulseSensor;
import com.timotei.babymonitor.data.model.TemperatureSensor;
import com.timotei.babymonitor.data.model.VideoCamera;

public class SettingsRepository {

    private static SettingsRepository single_instance = null;
    private final FirebaseDatabase database;
    private final DatabaseReference myRef;

    private VideoCamera videoCamera;
    private TemperatureSensor temperatureSensor;
    private HumiditySensor humiditySensor;
    private PulseSensor pulseSensor;

    private SettingsRepository(){
        database = FirebaseDatabase.getInstance("https://babymonitor-e580c-default-rtdb.europe-west1.firebasedatabase.app");
        myRef = database.getReference("server/sensors");
        this.videoCamera = new VideoCamera();
        this.temperatureSensor = new TemperatureSensor();
        this.humiditySensor = new HumiditySensor();
        this.pulseSensor= new PulseSensor();
    }

    public static SettingsRepository getInstance(){
        if (single_instance == null)
            single_instance = new SettingsRepository();

        return single_instance;
    }

    public PulseSensor getPulseSensor() {
        return pulseSensor;
    }

    public void setPulseSensor(PulseSensor pulseSensor) {
        this.pulseSensor = pulseSensor;
    }

    public TemperatureSensor getTemperatureSensor() {
        return temperatureSensor;
    }

    public void setTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    public HumiditySensor getHumiditySensor() {
        return humiditySensor;
    }

    public void setHumiditySensor(HumiditySensor humiditySensor) {
        this.humiditySensor = humiditySensor;
    }

    public VideoCamera getVideoCamera() {
        return videoCamera;
    }

    public void setVideoCamera(VideoCamera videoCamera) {
        this.videoCamera = videoCamera;
    }

    public void setVideoCameraStatus(String value){
        videoCamera.setStatus(value);
        myRef.child("video_camera").child("status").setValue(value);
    }

    /*public void getSettings(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("video_camera")){
                    getVideoCamera().setStatus(snapshot.child("video_camera").child("status").getValue().toString());
                }
                else{
                    Log.w("DATABASE","Not getting the value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
            }
        });
    }*/

    public boolean getSensorData(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setTemperatureSensor(snapshot.child("temperature").getValue(TemperatureSensor.class));
                //Log.d("DATABASE","temp: "+temperatureSensor.getDegrees());
                setHumiditySensor(snapshot.child("humidity").getValue(HumiditySensor.class));
                setPulseSensor(snapshot.child("heart_rate").getValue(PulseSensor.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
            }
        });
        return true;
    }
}
