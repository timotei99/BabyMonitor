package com.timotei.babymonitor.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class HomeRepository {
    MutableLiveData<String> name;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public HomeRepository(){
        this.name=new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<String> getName(){
        String uid=mAuth.getCurrentUser().getUid();
        Log.d("UID","USER ID is: "+uid);
        db.collection("users").document(uid).get()
                .addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful() && task.getResult() != null){
                                name.setValue(task.getResult().getString("firstName"));
                                Log.w("FIREBASE","Task successful! User name retrieved. "
                                        +name.toString());
                            }
                            else{
                                name.setValue("name");
                                Log.w("FIREBASE","Task not successful!!");
                            }
                        })
                .addOnFailureListener(e -> Log.w("Error loading document",e));

        return name;
    }


}
