package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListClinicsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<ModelClinic> modelList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;
    ImageButton btnBack;
    ClinicAdapter adapter;
    ProgressDialog pd;
    FloatingActionButton mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clinics);

        // ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("List of Clinics");

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        mRecyclerView = findViewById(R.id.recylerview);
        mAddBtn = findViewById(R.id.addBtn);
        btnBack = findViewById(R.id.Backk);

        // Set up Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set up RecyclerView
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Initialize ProgressDialog
        pd = new ProgressDialog(this);

        // Fetch and display data
        showData();

        // Add button click listener
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the add clinic page
                startActivity(new Intent(ListClinicsActivity.this, ClinicActivity.class));
                finish(); // Close the current activity
            }
        });
    }

    // Delete data (clinic) from Firestore
    public void deleteData(int index) {
        pd.setTitle("Deleting Data");
        pd.show();

        // Get clinic ID to delete
        db.collection("Clinics").document(modelList.get(index).getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ListClinicsActivity.this, "Deleting...", Toast.LENGTH_SHORT).show();
                        showData(); // Reload data
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListClinicsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Fetch and display data from Firestore
    private void showData() {
        pd.setTitle("Loading Data");
        pd.show();

        // Fetch clinics from Firestore collection
        db.collection("Clinics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                modelList.clear();
                pd.dismiss();

                if (task.isSuccessful()) {
                    // Add clinic data to the list
                    for (DocumentSnapshot doc : task.getResult()) {
                        ModelClinic modelClinic = new ModelClinic(
                                doc.getString("id"),
                                doc.getString("name"),
                                doc.getString("location"),
                                doc.getString("time")
                        );
                        modelList.add(modelClinic); // Add clinic to list
                    }

                    // Set up adapter for RecyclerView
                    adapter = new ClinicAdapter(ListClinicsActivity.this, modelList);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ListClinicsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ListClinicsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
