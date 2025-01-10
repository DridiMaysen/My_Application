package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderClinic extends RecyclerView.ViewHolder {
    TextView mName, mLocation, mTime;
    View mView;

    // Constructor
    public ViewHolderClinic(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        // Initialize views
        mName = itemView.findViewById(R.id.clinicName);
        mLocation = itemView.findViewById(R.id.clinicLocation);
        mTime = itemView.findViewById(R.id.clinicTime);
    }
}
