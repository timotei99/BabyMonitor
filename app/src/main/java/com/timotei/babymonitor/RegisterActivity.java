package com.timotei.babymonitor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;
import com.timotei.babymonitor.databinding.ActivityLoginBinding;
import com.timotei.babymonitor.databinding.ActivityRegisterBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private EditText emailEt;
    private EditText nameEt;
    private EditText phoneEt;
    private EditText passwordEt;
    private EditText confirmPasswordEt;
    private Button registerBtn;
    private TextView login;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        emailEt = binding.editTextTextEmailAddress;
        nameEt = binding.nameEt;
        phoneEt = binding.editTextTextPhoneNumber;
        passwordEt = binding.editTextTextPassword;
        confirmPasswordEt = binding.editTextTextPassword2;
        registerBtn = binding.registerBtn;
        login= binding.loginTv;

        setContentView(binding.getRoot());
        setOnClickListeners();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerUser(){
        final String email = Objects.requireNonNull(emailEt.getText()).toString();
        final String name = Objects.requireNonNull(nameEt.getText().toString());
        final String phone = Objects.requireNonNull(phoneEt.getText()).toString();
        final String password = Objects.requireNonNull(passwordEt.getText()).toString();
        final String confirmPassword = Objects.requireNonNull(confirmPasswordEt.getText()).toString();
        final ProgressBar loadingProgressBar = binding.loading;

        if(TextUtils.isEmpty(email)){
            emailEt.setError("Email cannot be empty!");
            emailEt.requestFocus();
        }
        else if(TextUtils.isEmpty(name)){
            nameEt.setError("First name cannot be empty!");
            nameEt.requestFocus();
        }
        else if(TextUtils.isEmpty(phone)){
            phoneEt.setError("Phone number cannot be empty!");
            phoneEt.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            passwordEt.setError("Password cannot be empty!");
            passwordEt.requestFocus();
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            confirmPasswordEt.setError("Confirmation password cannot be empty!");
            confirmPasswordEt.requestFocus();
        }
        else if(!password.equals(confirmPassword)){
            confirmPasswordEt.setError("Passwords do not match!");
            confirmPasswordEt.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   loadingProgressBar.setVisibility(View.VISIBLE);
                    mAuth.getCurrentUser().sendEmailVerification();
                    createUserData(email,name,phone);
                   Toast.makeText(RegisterActivity.this,
                           "User registered successfully. Please verify your account!",
                           Toast.LENGTH_SHORT).show();
                   FirebaseAuth.getInstance().signOut();
                   startActivity(new Intent(this, LoginActivity.class));
               }
               else{
                   Toast.makeText(RegisterActivity.this,
                           "Registration failed. Please try again!",
                           Toast.LENGTH_SHORT).show();
               }
            });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUserData(String email, String name, String phoneNumber){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Map<String,String> user = new HashMap<>();
        user.put("email",email);
        user.put("firstName",name);
        user.put("phoneNumber",phoneNumber);
        user.put("registerDate",dateTimeFormatter.format(LocalDateTime.now()));
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(unused -> Log.d("AUTH","DocumentSnapshot added with ID: "+uid))
                .addOnFailureListener(e -> Log.w("Error adding document",e));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setOnClickListeners(){
        login.setOnClickListener(v-> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        registerBtn.setOnClickListener(v -> {
            registerUser();
        });
    }
}
