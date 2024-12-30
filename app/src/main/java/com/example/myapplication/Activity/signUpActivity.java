package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.myapplication.Constant.AllConstant;
import com.example.myapplication.Permissions.AppPermissions;
import com.example.myapplication.R;
import com.example.myapplication.UserModel;
import com.example.myapplication.Utility.LoadingDialog;


import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;




public class signUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private Uri imageUri;
    private AppPermissions appPermissions;
    private LoadingDialog loadingDialog;
    private String email, username, password;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.txtLogin.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}