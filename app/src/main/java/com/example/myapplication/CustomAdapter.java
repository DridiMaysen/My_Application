package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolderHospital> {
    ListHospitalsActivity listActivity;
    List<ModelHospital> modelList;
    Context context;

    public CustomAdapter(ListHospitalsActivity listActivity, List<ModelHospital> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolderHospital onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_hospital, viewGroup, false);
        ViewHolderHospital viewHolderHospital = new ViewHolderHospital(itemView);

        viewHolderHospital.setOnClickListener(new ViewHolderHospital.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = modelList.get(position).getName();
                String location = modelList.get(position).getLocation();
                String time = modelList.get(position).getTime();
                Toast.makeText(listActivity, name + "," + location + "," + time, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // Handle long-click event if necessary
                AlertDialog.Builder builder=new AlertDialog.Builder(listActivity);
                String[] options={"Update","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){
                            String id =modelList.get(position).getId();
                            String name =modelList.get(position).getName();
                            String location =modelList.get(position).getLocation();
                            String time =modelList.get(position).getTime();
                            Intent intent=new Intent(listActivity,HospitalActivity.class);
                            intent.putExtra("pid",id);
                            intent.putExtra("pname",name);
                            intent.putExtra("plocation",location);
                            intent.putExtra("ptime",time);
                            listActivity.startActivity(intent);

                        }
                        if (i==1){
                            listActivity.deleteData(position);

                        }

                    }
                }).create().show();
            }
        });
        return viewHolderHospital;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHospital viewHolderHospital, int i) {
        viewHolderHospital.mName.setText(modelList.get(i).getName());
        viewHolderHospital.mLocation.setText(modelList.get(i).getLocation());
        viewHolderHospital.mTime.setText(modelList.get(i).getTime());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
