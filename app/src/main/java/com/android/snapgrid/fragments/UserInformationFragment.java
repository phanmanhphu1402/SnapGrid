package com.android.snapgrid.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.snapgrid.MainActivity;
import com.android.snapgrid.R;
import com.android.snapgrid.SettingActivity;
import com.android.snapgrid.UserConfigActivity;
import com.android.snapgrid.adapters.MasonryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class UserInformationFragment extends Fragment {
    TextView userEmail;
    ImageView userAvatar;
    ArrayList images;

    FirebaseAuth mAuth;
    FirebaseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_user_information, container, false);
        Button btnConfigInfor = (Button)rootview.findViewById(R.id.btnConfigInfor);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = (TextView) rootview.findViewById(R.id.userEmail);
        userAvatar = rootview.findViewById(R.id.userAvatar);
        Picasso.get().load(currentUser.getPhotoUrl()).placeholder(R.drawable.appa).into(userAvatar);
        userEmail.setText(currentUser.getEmail().toString());
        btnConfigInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserConfigActivity.class);
                startActivity(intent);
            }
        });
        ImageButton btnSetting = rootview.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> images = new ArrayList<>();
                String userId = currentUser.getUid();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(userId.equals(snapshot.child("idUser").getValue())){
                        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                        System.out.println(imageUrl);
                        images.add(imageUrl);
                        recyclerView.setAdapter(new MasonryAdapter(getContext(), images));
                    }
//                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
//                    System.out.println(imageUrl);
//
//                    System.out.println(snapshot.child("idUser").getValue());
//                    images.add(imageUrl);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rootview;

    }

}