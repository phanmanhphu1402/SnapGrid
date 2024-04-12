package com.android.snapgrid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.snapgrid.PostAddingActivity;
import com.android.snapgrid.R;

public class CustomDialogFragment extends DialogFragment {
    Button btnEdit, btnCopyLink, btnDownload, btnClose;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the custom layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_pop_up_edit_post, null);
        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        // Create the dialog object
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;  // Set the dialog position to the bottom
        window.setAttributes(params);
        btnClose = view.findViewById(R.id.btnCloseRed);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostAddingActivity activity = new PostAddingActivity();
                Bundle bundle = getArguments();
                String image = bundle.getString("dataImage");
                String title = bundle.getString("dataTitle");
                String content = bundle.getString("dataContent");

                Intent intent = new Intent(getActivity(), PostAddingActivity.class);
                intent.putExtra("dataImage", image);
                intent.putExtra("dataTitle", title);
                intent.putExtra("dataContent", content);
                System.out.println("Custom");
                System.out.println(image);
                System.out.println(title);
                System.out.println(content);
                startActivity(intent);
            }
        });


        return dialog;
    }

}