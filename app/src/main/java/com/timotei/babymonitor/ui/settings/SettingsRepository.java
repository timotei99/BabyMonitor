package com.timotei.babymonitor.ui.settings;

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
        myRef.child("video_camera").child("status").setValue(value);
    }
}
