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

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(!prefs.contains("IpAddress")){
            startActivity(new Intent(this,PairingActivity.class));
        }

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(this,HomeActivity.class));
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final TextView createAccountTextView =binding.createAccountTv;
        loadingProgressBar = binding.loading;


        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginUser(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });

        assert createAccountTextView != null;
        createAccountTextView.setOnClickListener(v -> {
            startActivity(new Intent(this,RegisterActivity.class));
        });
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user!=null){
                            // check email is verified
                            if(!user.isEmailVerified()){
                                mAuth.getCurrentUser().sendEmailVerification();
                                Toast.makeText(LoginActivity.this,"Please verify your email!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this,"User logged in successfully!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "signInWithEmail:success");
                                updateUI(user);

                            }
                        }
                        // Sign in success, update UI with the signed-in user's information

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithPassword:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser != null){
            Toast.makeText(this,"You signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,HomeActivity.class));

        }else {
            Toast.makeText(this,"You didn't signed in",Toast.LENGTH_LONG).show();
        }
    }

}
