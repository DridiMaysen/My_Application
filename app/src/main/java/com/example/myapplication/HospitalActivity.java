package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HospitalActivity extends AppCompatActivity {
    EditText mTitleEt, mLocationEt, mTimeEt;
    Button mAdd, mList;
    ProgressDialog pd;
    FirebaseFirestore db;
    String pid, pname, plocation, ptime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_activity);

        // Initialize views
        mTitleEt = findViewById(R.id.name);
        mLocationEt = findViewById(R.id.localisation);
        mTimeEt = findViewById(R.id.time);
        mList = findViewById(R.id.showlist);
        mAdd = findViewById(R.id.add);  // Initialize mAdd Button before usage



        // Set up ActionBar title
        ActionBar actionBar = getSupportActionBar();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            actionBar.setTitle("Update Hospital");
            mAdd.setText("Update");
            pid = bundle.getString("pid");
            pname = bundle.getString("pname");
            plocation = bundle.getString("plocation");
            ptime = bundle.getString("ptime");
            mTitleEt.setText(pname);
            mLocationEt.setText(plocation);
            mTimeEt.setText(ptime);
        } else {
            actionBar.setTitle("Add Hospital");
            mAdd.setText("Add");
        }

        // Initialize Firebase Firestore and ProgressDialog
        pd = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        // Set OnClickListener for Add button
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleEt.getText().toString().trim();
                String location = mLocationEt.getText().toString().trim();
                String time = mTimeEt.getText().toString().trim();

                if (bundle != null) { // If we are updating
                    String id = pid;
                    updateData(id, title, location, time);
                } else { // If we are adding a new hospital
                    uploadData(title, location, time);
                }
            }
        });

        // Set OnClickListener for List button
        mList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HospitalActivity.this, ListHospitalsActivity.class));
                finish();
            }
        });
    }

    // Method to update data in Firestore
    private void updateData(String id, String title, String location, String time) {
        pd.setTitle("Updating data to Firebase");
        pd.show();

        db.collection("Documents").document(id)
                .update("title", title, "location", location, "time", time)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(HospitalActivity.this, "Hospital Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(HospitalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to upload new data to Firestore
    private void uploadData(String title, String location, String time) {
        pd.setTitle("Adding data to Firebase");
        pd.show();

        String id = UUID.randomUUID().toString(); // Generate a unique ID for the new document
        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("location", location);
        doc.put("time", time);

        db.collection("Documents").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(HospitalActivity.this, "Hospital Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(HospitalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
