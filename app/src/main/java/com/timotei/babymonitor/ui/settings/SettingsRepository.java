package com.timotei.babymonitor.ui.settings;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timotei.babymonitor.data.model.VideoCamera;

public class SettingsRepository {

    private static SettingsRepository single_instance = null;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private VideoCamera videoCamera;

    private SettingsRepository(){
        database = FirebaseDatabase.getInstance("https://babymonitor-e580c-default-rtdb.europe-west1.firebasedatabase.app");
        myRef = database.getReference("server/sensors");
        this.videoCamera = new VideoCamera();
    }

    public static SettingsRepository getInstance(){
        if (single_instance == null)
            single_instance = new SettingsRepository();

        return single_instance;
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

    public void getSettings(){
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
    }
}
