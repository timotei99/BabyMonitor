package com.timotei.babymonitor.ui.home;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mName;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private HomeRepository repo;

    public HomeViewModel() {
        repo = new HomeRepository();
        mText = new MutableLiveData<>();
        mText.setValue(dateTimeFormatter.format(LocalDateTime.now()));
        mName = repo.getName();

    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getName(){
        return mName;}
}