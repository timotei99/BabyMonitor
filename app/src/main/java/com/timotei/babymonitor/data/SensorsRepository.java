package com.timotei.babymonitor.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timotei.babymonitor.data.model.Sensors;

public class SensorsRepository {
    private static SensorsRepository single_instance = null;
    private final FirebaseDatabase database;
    private final DatabaseReference myRef;
    private final String uid;
    private final String DB_LINK="https://babymonitor-e580c-default-rtdb.europe-west1.firebasedatabase.app";
    private Sensors sensors;

    private SensorsRepository(){
        uid= FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance(DB_LINK);
        myRef = database.getReference("server/"+uid);
        this.sensors=new Sensors();
    }

    public static SensorsRepository getInstance(){
        if (single_instance == null)
            single_instance = new SensorsRepository();

        return single_instance;
    }

    public Sensors getSensors() {
        return sensors;
    }

    public void setSensors(Sensors sensors) {
        this.sensors = sensors;
    }

    public void getSensorData(){
        myRef.child("sensors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setSensors(snapshot.getValue(Sensors.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
            }
        });
    }

    public void storeLastWeight(String weight){
        myRef.child("sensors").child("last_weight")
                .setValue(weight)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("FIREBASE", "Last weight updated.");
                    }
                    else{
                        Log.e("FIREBASE", "Error updating weight!", task.getException());
                    }
                });
    }


}
