package com.android.snapgrid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.snapgrid.PostAddingActivity;
import com.android.snapgrid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class CustomDialogFragment extends DialogFragment {
    Button btnEdit, btnCopyLink, btnDownload, btnClose;
    FirebaseAuth mAuth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the custom layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_pop_up_edit_post, null);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        // Create the dialog object
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;  // Set the dialog position to the bottom
        window.setAttributes(params);
        Bundle bundle = getArguments();
        btnClose = view.findViewById(R.id.btnCloseRed);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btnEdit = view.findViewById(R.id.btnEdit);
        String idUser = bundle.getString("dataIdUser");
        System.out.println("THUY GGG FFF");
        System.out.println(currentUser.getUid());
        System.out.println(idUser);
        if (!currentUser.getUid().equals(idUser)) {
            btnEdit.setVisibility(View.INVISIBLE);
        }
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostAddingActivity activity = new PostAddingActivity();

                String image = bundle.getString("dataImage");
                String title = bundle.getString("dataTitle");
                String content = bundle.getString("dataContent");
                String tag = bundle.getString("dataTag");
                String idPost = bundle.getString("dataIdPost");
                Intent intent = new Intent(getActivity(), PostAddingActivity.class);
                intent.putExtra("dataImage", image);
                intent.putExtra("dataTitle", title);
                intent.putExtra("dataContent", content);
                intent.putExtra("dataTag", tag);
                intent.putExtra("dataIdPost", idPost);
                System.out.println("Custom");
                System.out.println(image);
                System.out.println(title);
                System.out.println(content);
                startActivity(intent);
            }
        });
        Log.d(CustomDialogFragment.class.getName(), Objects.requireNonNull(bundle.getString("dataImage")) + "");
        btnDownload = view.findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageUrl = bundle.getString("dataImage");
                Picasso picasso = Picasso.get();
                picasso.setIndicatorsEnabled(true);
                picasso.setLoggingEnabled(true);
                picasso.load(imageUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, bundle.getString("dataTitle"), bundle.getString("dataContent"));
                        Toast.makeText(getContext(), "Download image is success!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Toast.makeText(getContext(), "Download image is failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Toast.makeText(getContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return dialog;
    }

}