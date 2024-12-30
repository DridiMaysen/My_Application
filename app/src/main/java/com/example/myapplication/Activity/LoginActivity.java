package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utility.LoadingDialog;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;



    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog = new LoadingDialog(this);

        binding.btnSignUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, signUpActivity.class));
        });

        binding.txtForgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
        });

    }}