package com.example.myapplication.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogLayoutBinding;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.loadingDialogStyle);
        DialogLayoutBinding binding = DialogLayoutBinding.inflate(LayoutInflater.from(activity), null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        alertDialog = builder.create();

        // Start the ProgressBar (make it visible)
        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        alertDialog.show();
    }

    public void stopLoading() {
        // Dismiss the dialog and hide the ProgressBar
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
