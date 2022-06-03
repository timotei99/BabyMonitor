package com.timotei.babymonitor.ui.settings;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timotei.babymonitor.data.model.Sensors;

public class SettingsRepository {

    private static SettingsRepository single_instance = null;
    private final FirebaseDatabase database;
    private final DatabaseReference myRef;
    private final String uid;
    private final String DB_LINK="https://babymonitor-e580c-default-rtdb.europe-west1.firebasedatabase.app";

    private SettingsRepository(){
        uid= FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance(DB_LINK);
        myRef = database.getReference("server/"+uid+"/raspberry_actions");
    }

    public static SettingsRepository getInstance(){
        if (single_instance == null)
            single_instance = new SettingsRepository();

        return single_instance;
    }



    public void startWhiteNoises(){
        myRef.child("whitenoise")
                .setValue("on")
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("FIREBASE", "White noises started.");
                    }
                    else{
                        Log.e("FIREBASE", "Error starting white noises!", task.getException());
                    }
                });
    }


    public void updateSetting(String setting,String value){
        myRef.child(setting)
                .setValue(value)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("FIREBASE", "Setting updated: "+setting);
                    }
                    else{
                        Log.e("FIREBASE", "Error updating setting: "+setting, task.getException());
                    }
                });
    }

}
