package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.android.material.floatingactionbutton.FloatingActionButton; // Corrected import
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListHospitalsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<ModelHospital> modelList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;
    ImageButton btnBack;

    CustomAdapter adapter;
    ProgressDialog pd;
    FloatingActionButton mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hospitals);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("List Of Hospitals");

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        mRecyclerView = findViewById(R.id.recylerview);
        mAddBtn = findViewById(R.id.addBtn);
        btnBack=findViewById(R.id.Backk);
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

        // Load data
        showData();

        // Set up FloatingActionButton click listener
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListHospitalsActivity.this, HospitalActivity.class));
                finish(); // Finish current activity
            }
        });
    }
    public void deleteData(int index){
        pd.setTitle("Deleting Data");
        pd.show();
        db.collection("Documents").document(modelList.get(index).getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ListHospitalsActivity.this, "Deleting ...", Toast.LENGTH_SHORT).show();
                        showData();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListHospitalsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showData() {
        pd.setTitle("Loading Data");
        pd.show();

        // Fetch data from Firestore
        db.collection("Documents").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                modelList.clear();
                pd.dismiss(); // Dismiss the progress dialog

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        ModelHospital modelHospital = new ModelHospital(
                                doc.getString("id"),
                                doc.getString("title"),
                                doc.getString("location"),
                                doc.getString("time")
                        );
                        modelList.add(modelHospital); // Add hospital to the list
                    }

                    // Set up the adapter
                    adapter = new CustomAdapter(ListHospitalsActivity.this, modelList);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ListHospitalsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss(); // Dismiss the progress dialog
                Toast.makeText(ListHospitalsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
