package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClinicActivity extends AppCompatActivity {

    // Views
    EditText mNameEt, mLocationEt, mTimeEt;
    Button mAddBtn, mListBtn;
    ProgressDialog pd;

    // Firebase Firestore reference
    FirebaseFirestore db;

    // Clinic details
    String clinicId, clinicName, clinicLocation, clinicTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);

        // Initialize views
        mNameEt = findViewById(R.id.clinicName);
        mLocationEt = findViewById(R.id.clinicLocation);
        mTimeEt = findViewById(R.id.clinicTime);
        mAddBtn = findViewById(R.id.addOrUpdateBtn);
        mListBtn = findViewById(R.id.showClinicsBtn);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);

        // Setup ActionBar
        ActionBar actionBar = getSupportActionBar();

        // Retrieve data from the intent extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // We're in the update mode
            actionBar.setTitle("Update Clinic");
            mAddBtn.setText("Update Clinic");

            // Get the clinic data from the intent
            clinicId = bundle.getString("clinicId");
            clinicName = bundle.getString("clinicName");
            clinicLocation = bundle.getString("clinicLocation");
            clinicTime = bundle.getString("clinicTime");

            // Set data to EditText fields
            mNameEt.setText(clinicName);
            mLocationEt.setText(clinicLocation);
            mTimeEt.setText(clinicTime);
        } else {
            // We're in the add mode
            actionBar.setTitle("Add Clinic");
            mAddBtn.setText("Add Clinic");
        }

        // Handle Add/Update Button click
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from EditTexts
                clinicName = mNameEt.getText().toString().trim();
                clinicLocation = mLocationEt.getText().toString().trim();
                clinicTime = mTimeEt.getText().toString().trim();

                // If we are updating
                if (clinicId != null) {
                    updateClinic(clinicId, clinicName, clinicLocation, clinicTime);
                } else {
                    // If we are adding a new clinic
                    addClinic(clinicName, clinicLocation, clinicTime);
                }
            }
        });

        // Handle Show Clinics button click (Navigating to list view)
        mListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClinicActivity.this, ListClinicsActivity.class));
                finish();
            }
        });
    }

    // Method to update clinic data in Firestore
    private void updateClinic(String id, String name, String location, String time) {
        pd.setTitle("Updating Clinic");
        pd.show();

        db.collection("Clinics").document(id)
                .update("name", name, "location", location, "time", time)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(ClinicActivity.this, "Clinic Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ClinicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to add a new clinic to Firestore
    private void addClinic(String name, String location, String time) {
        pd.setTitle("Adding Clinic");
        pd.show();

        String id = UUID.randomUUID().toString(); // Generate a unique ID for the new clinic
        Map<String, Object> clinicData = new HashMap<>();
        clinicData.put("id", id);
        clinicData.put("name", name);
        clinicData.put("location", location);
        clinicData.put("time", time);

        db.collection("Clinics").document(id).set(clinicData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(ClinicActivity.this, "Clinic Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ClinicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
