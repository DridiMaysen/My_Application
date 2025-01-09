package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utility.LoadingDialog;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth auth;
    ProgressBar progressBar;
    TextView textView;
    private ActivityLoginBinding binding;
    TextInputEditText Email, Password, UserName, Phone;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Near Me");

        // Initialize views
        Email = findViewById(R.id.mail);
        UserName = findViewById(R.id.name);
        Phone = findViewById(R.id.Phone);
        Password = findViewById(R.id.Pass);

        auth = FirebaseAuth.getInstance();

        // Using binding to access views
        editTextEmail = binding.edtEmaill;
        editTextPassword = binding.edtPasswordd;
        buttonLogin = binding.btnLogin;
        progressBar = binding.progressBar;
        textView = binding.txtForgetPassword;

        // Forget Password Button
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ForgetActivity.class);
            startActivity(intent);
            finish();
        });

        // Login Button Click
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String pass = editTextPassword.getText().toString();

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!pass.isEmpty()) {
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(authResult -> {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                readData(email);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    editTextPassword.setError("Empty fields are not allowed");
                }
            } else if (email.isEmpty()) {
                editTextEmail.setError("Empty fields are not allowed");
            } else {
                editTextEmail.setError("Please enter correct email");
            }
        });

        // Sign Up Button Click
        binding.btnSignUp.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, signUpActivity.class)));
    }

    // Method to sanitize email to replace "." and "@" for Firebase key compatibility
    private String sanitizeEmail(String email) {
        return email.replace(".", "_dot_").replace("@", "_at_");
    }

    // Read user data from Firebase and send to MainActivity
    private void readData(String email) {
        String sanitizedEmail = sanitizeEmail(email);
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(sanitizedEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String user = String.valueOf(dataSnapshot.child("userName").getValue());
                    String emailVal = String.valueOf(dataSnapshot.child("email").getValue());
                    String password = String.valueOf(dataSnapshot.child("password").getValue());
                    String phone = String.valueOf(dataSnapshot.child("phone").getValue());

                    // Intent pour envoyer les données à MainActivity
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.putExtra("userName", user);
                    mainIntent.putExtra("email", emailVal);
                    mainIntent.putExtra("password", password);
                    mainIntent.putExtra("phone", phone);

                    // Storing user data in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", user);
                    editor.putString("email", emailVal);
                    editor.putString("password", password);
                    editor.putString("phone", phone);
                    editor.apply();

                    // Lancer MapsActivity, mais envoyer les données à MainActivity
                    Intent mapsIntent = new Intent(LoginActivity.this, MapsActivity.class);
                    mapsIntent.putExtra("userName", user);
                    mapsIntent.putExtra("email", emailVal);
                    mapsIntent.putExtra("password", password);
                    mapsIntent.putExtra("phone", phone);

                    // Démarrer MapsActivity
                    startActivity(mapsIntent);

                    // Si vous voulez fermer LoginActivity après avoir lancé MapsActivity
                    finish();
                }
            }
        });
    }

}

