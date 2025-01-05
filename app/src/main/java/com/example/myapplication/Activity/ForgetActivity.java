package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityForgetBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {
    Button reset;
    EditText edtEmail;
    ProgressBar progressBar;
    FirebaseAuth auth;
    String strEmail;


    private ActivityForgetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Near Me");
        reset=findViewById(R.id.btnForgetPassword);
        edtEmail=findViewById(R.id.edtEmailf);
        progressBar=findViewById(R.id.progress);
        auth=FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 strEmail= edtEmail.getText().toString().trim();
                if (! TextUtils.isEmpty(strEmail)){
                    resetPassword();

                }else {
                    edtEmail.setError("email field cant be empty");

                }
            }
        });



        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });


    }

    private void resetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        reset.setVisibility(View.INVISIBLE);
        auth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgetActivity.this, "Reset password link has been sent to your Email", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(ForgetActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetActivity.this, "Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
            }
        });
    }
}