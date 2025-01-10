package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ViewHolderClinic> {
    List<ModelClinic> modelList;
    ListClinicsActivity listActivity;

    public ClinicAdapter(ListClinicsActivity listActivity, List<ModelClinic> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
    }

    @Override
    public ViewHolderClinic onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_clinic, parent, false);
        return new ViewHolderClinic(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderClinic holder, int position) {
        holder.mName.setText(modelList.get(position).getName());
        holder.mLocation.setText(modelList.get(position).getLocation());
        holder.mTime.setText(modelList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolderClinic extends RecyclerView.ViewHolder {
        TextView mName, mLocation, mTime;

        public ViewHolderClinic(View view) {
            super(view);
            mName = view.findViewById(R.id.clinicName);
            mLocation = view.findViewById(R.id.clinicLocation);
            mTime = view.findViewById(R.id.clinicTime);

            // Long click listener for Delete and Update options
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();

                    // Show AlertDialog for "Update" and "Delete" options
                    AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
                    String[] options = {"Update", "Delete"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                // Update option
                                Intent intent = new Intent(listActivity, ClinicActivity.class);
                                intent.putExtra("clinicId", modelList.get(position).getId());
                                intent.putExtra("clinicName", modelList.get(position).getName());
                                intent.putExtra("clinicLocation", modelList.get(position).getLocation());
                                intent.putExtra("clinicTime", modelList.get(position).getTime());
                                listActivity.startActivity(intent);
                            }
                            if (i == 1) {
                                // Delete option
                                listActivity.deleteData(position);
                            }
                        }
                    }).create().show();
                    return true;
                }
            });
        }
    }
}
