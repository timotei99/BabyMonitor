package com.timotei.babymonitor;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.timotei.babymonitor.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private ProgressBar loadingProgressBar;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView createAccountTextView;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        emailEditText = binding.email;
        passwordEditText = binding.password;
        loginButton = binding.login;
        createAccountTextView =binding.createAccountTv;
        loadingProgressBar = binding.loading;

        checkDevicesArePaired();
        checkUserIsLoggedIn();
        setOnClickListeners();
        setContentView(binding.getRoot());
    }

    private void loginUser(String email, String password){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(LoginActivity.this,"Please fill in all fields.",Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // check email is verified
                                if (!user.isEmailVerified()) {
                                    mAuth.getCurrentUser().sendEmailVerification();
                                    Toast.makeText(LoginActivity.this, "Please verify your email!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginActivity.this, "User logged in successfully!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "signInWithEmail:success");
                                    updateUI(user);
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithPassword:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser != null){
            Toast.makeText(this,"You signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,HomeActivity.class));
        }else {
            Toast.makeText(this,"Sign in failed!",Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.GONE);
        }
    }

    private void setOnClickListeners(){
        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginUser(emailEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });

        assert createAccountTextView != null;
        createAccountTextView.setOnClickListener(v -> {
            startActivity(new Intent(this,RegisterActivity.class));
        });
    }

    private void checkDevicesArePaired(){
        if(!prefs.contains("IpAddress")){
            startActivity(new Intent(this,PairingActivity.class));
        }
    }

    private void checkUserIsLoggedIn(){
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(this,HomeActivity.class));
        }
    }


}
