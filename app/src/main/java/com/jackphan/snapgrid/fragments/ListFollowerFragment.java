package com.jackphan.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.adapters.FollowingUserAdapter;
import com.jackphan.snapgrid.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFollowerFragment extends Fragment {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ImageView userAvatar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_listfollow, container, false);

        ArrayList<User> listUser = new ArrayList<>();
        RecyclerView recyclerView = rootview.findViewById(R.id.listFollowUser);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        DatabaseReference followingRef = mDatabase.child("Users").child(currentUserId).child("followers");
        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String followedUserId = snapshot.getKey();
                        DatabaseReference userRef = mDatabase.child("Users").child(followedUserId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    String id = dataSnapshot.child("id").getValue(String.class);
                                    String imageUrl = dataSnapshot.child("profile").getValue(String.class);
                                    User user = new User();
                                    user.setName(name);
                                    user.setId(id);
                                    user.setAvatar(imageUrl);
                                    listUser.add(user);
                                    FollowingUserAdapter adapter = new FollowingUserAdapter(getContext(), listUser, getActivity().getSupportFragmentManager());
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
//                        followingUserIds.add(followedUserId);
                    }
                } else {
                    Log.d("Follower List", "No follower users.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("FetchFollowerError", "Error fetching follower users: " + databaseError.getMessage());
            }
        });
        return rootview;
    }
}