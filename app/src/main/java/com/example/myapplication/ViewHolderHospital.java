package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderHospital extends RecyclerView.ViewHolder {
    TextView mName, mLocation, mTime;
    View mView;

    // Constructor
    public ViewHolderHospital(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        // Setting click listeners
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

        // Initialize views
        mName = itemView.findViewById(R.id.rName);
        mLocation = itemView.findViewById(R.id.rLocalisation);
        mTime = itemView.findViewById(R.id.rTime);
    }

    // ClickListener interface
    private ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    // Method to set the click listener
    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
