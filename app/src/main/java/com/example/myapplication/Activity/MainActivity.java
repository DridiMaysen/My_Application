package com.example.myapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.HospitalActivity;
import com.example.myapplication.ListHospitalsActivity;
import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    TextInputEditText editTextEmail, editTextPassword, editUserName, edtphone;
    Button btnUpdate;

    // SharedPreferences to store user data
    SharedPreferences sharedPreferences;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextEmail = findViewById(R.id.mail);
        editUserName = findViewById(R.id.name);
        editTextPassword = findViewById(R.id.Pass);
        edtphone = findViewById(R.id.Phone);
        btnUpdate = findViewById(R.id.Update); // Update Button

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Retrieve user data from SharedPreferences
        String userName = sharedPreferences.getString("userName", null);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        String phone = sharedPreferences.getString("phone", null);

        // Set data to views if available
        if (userName != null && email != null && password != null && phone != null) {
            editUserName.setText(userName);
            editTextEmail.setText(email);
            editTextPassword.setText(password);
            edtphone.setText(phone);
        } else {
            // Handle missing data scenario
            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
        }

        // Set up ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile Information");
        }

        // Drawer setup
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.na_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Navigation item selection handling
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent homeIntent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(homeIntent);
                finish();
            }

            if (item.getItemId() == R.id.hospital) {
                Toast.makeText(MainActivity.this, "Hospital is clicked", Toast.LENGTH_SHORT).show();
                Intent hospitalIntent = new Intent(getApplicationContext(), ListHospitalsActivity.class);
                startActivity(hospitalIntent);
                finish();
            }

            if (item.getItemId() == R.id.clinics) {
                Toast.makeText(MainActivity.this, "Clinics is clicked", Toast.LENGTH_SHORT).show();
            }

            if (item.getItemId() == R.id.pharmacy) {
                Toast.makeText(MainActivity.this, "Pharmacy is clicked", Toast.LENGTH_SHORT).show();
            }

            if (item.getItemId() == R.id.logout) {
                // Clear SharedPreferences when logging out
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(MainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                auth.signOut(); // Log out the user from Firebase
                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoutIntent);
                finish();
            }

            if (item.getItemId() == R.id.about) {
                Toast.makeText(MainActivity.this, "About is clicked", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(getApplicationContext(), about.class);
                startActivity(homeIntent);
                finish();
            }

            return false;
        });

        // Update Button OnClickListener
        btnUpdate.setOnClickListener(v -> {
            String updatedUserName = editUserName.getText().toString();
            String updatedEmail = editTextEmail.getText().toString();
            String updatedPassword = editTextPassword.getText().toString();
            String updatedPhone = edtphone.getText().toString();

            // Check if any of the fields are empty
            if (updatedUserName.isEmpty() || updatedEmail.isEmpty() || updatedPassword.isEmpty() || updatedPhone.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Update SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", updatedUserName);
                editor.putString("email", updatedEmail);
                editor.putString("password", updatedPassword);
                editor.putString("phone", updatedPhone);
                editor.apply();

                // Update Firebase Database
                String sanitizedEmail = updatedEmail.replace(".", "_dot_").replace("@", "_at_");
                reference = FirebaseDatabase.getInstance().getReference("users");
                reference.child(sanitizedEmail).child("userName").setValue(updatedUserName);
                reference.child(sanitizedEmail).child("email").setValue(updatedEmail);
                reference.child(sanitizedEmail).child("password").setValue(updatedPassword);
                reference.child(sanitizedEmail).child("phone").setValue(updatedPhone);

                // Show success toast
                Toast.makeText(MainActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
