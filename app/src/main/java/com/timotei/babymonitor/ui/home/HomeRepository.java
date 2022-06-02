package com.timotei.babymonitor.ui.home;

import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.timotei.babymonitor.ui.settings.SettingsRepository;

import java.util.Objects;

public class HomeRepository {
    private String name=null;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static HomeRepository single_instance = null;

    private HomeRepository(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static HomeRepository getInstance(){
        if (single_instance == null)
            single_instance = new HomeRepository();

        return single_instance;
    }

    public String getName(){
        return name;
    }

    public void setName(TextView tv){

        if(this.name!=null){
            tv.setText(this.name);
            return;
        }

        String uid=mAuth.getCurrentUser().getUid();
        Log.d("UID","USER ID is: "+uid);
        db.collection("users").document(uid).get()
                .addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful() && task.getResult() != null){
                                this.name=task.getResult().getString("firstName");
                            }
                            else{
                                this.name="User";
                                Log.w("FIREBASE","Task not successful!!");
                            }
                            tv.setText(this.name);
                        })
                .addOnFailureListener(e -> Log.w("Error loading document",e));
    }


}
