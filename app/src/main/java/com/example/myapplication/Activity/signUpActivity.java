package com.example.myapplication.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.Constant.AllConstant;
import com.example.myapplication.MapsActivity;
import com.example.myapplication.Permissions.AppPermissions;
import com.example.myapplication.R;
import com.example.myapplication.UserModel;
import com.example.myapplication.Utility.LoadingDialog;


import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.example.myapplication.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;




public class signUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding  binding;
    private Uri imageUri;
    private AppPermissions appPermissions;
    private LoadingDialog loadingDialog;

    private StorageReference storageReference;
    TextInputEditText editTextEmail,editTextPassword,editUserName,editTextCPassword,edtphone;


    Button buttonSignUp;
    FirebaseAuth auth;
    ProgressBar progressBar;
    TextView textView;
    ActivityMainBinding bindingg;
    String  userName,email,password,phone;
    FirebaseDatabase db;
    DatabaseReference reference;

    public void  onStart() {
        super.onStart();
        FirebaseUser currentUser= auth.getCurrentUser();

        if (currentUser != null) {
            Intent intent=new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Near Me");
        auth=FirebaseAuth.getInstance();

        editTextEmail= findViewById(R.id.edtEmail);
        editTextPassword= findViewById(R.id.edtPassword);
        editTextCPassword=findViewById(R.id.edtCPassword);
        edtphone=findViewById(R.id.edtPhone);
        buttonSignUp=findViewById(R.id.btnSignUp);
        editUserName=findViewById(R.id.edtUsername);
        progressBar =findViewById(R.id.progressBar);
        textView=findViewById(R.id.txtLogin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonSignUp.setOnClickListener(v -> signUp());









    }
        private void signUp() {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String userName=editUserName.getText().toString().trim();
            String cpassword=editTextCPassword.getText().toString().trim();
            String phone=edtphone.getText().toString().trim();

            if (userName.isEmpty()) {
                editTextEmail.setError("Please enter a name");
                editUserName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                editTextEmail.setError("please enter an email");
                editTextEmail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Invalid Email");
                editTextEmail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                editTextPassword.setError("Please enter a Password");
                editTextPassword.requestFocus();
                return;
            }
            if (password.length() < 6) {
                editTextPassword.setError("Password should contain at least 6 caracters");
                editTextPassword.requestFocus();
                return;
            }
            if (phone.length() !=8) {
                edtphone.setError("Phone number should contain at leat 8 numbers");
                edtphone.requestFocus();
                return;
            }
            if (!password.equals(cpassword)){
                editTextCPassword.setError("Passwords are not matching");
                editTextPassword.requestFocus();
                return;

            }
            String sanitizedEmail = sanitizeEmail(email);

            // Création de l'utilisateur
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            users users = new users(userName,email,phone,password);
                            db=FirebaseDatabase.getInstance();
                            reference=db.getReference("users");
                            reference.child(sanitizedEmail).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    editUserName.setText("");
                                    edtphone.setText("");
                                    editTextEmail.setText("");
                                    edtphone.setText("");

                                }
                            });

                            Toast.makeText(signUpActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(signUpActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            String errorMessage = task.getException() != null ?
                                    task.getException().getMessage() : "Erreur inconnue";
                            Toast.makeText(signUpActivity.this, "Échec de l'inscription: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    private String sanitizeEmail(String email) {
        return email.replace(".", "_dot_").replace("@", "_at_");
    }


}