package com.android.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.snapgrid.R;
import com.android.snapgrid.adapters.FollowingUserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFollowFragment extends Fragment {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ImageView userAvatar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_listfollow, container, false);
        ListView listView = rootview.findViewById(R.id.listView_followings);

        ArrayList<String> followingUserIds = new ArrayList<>();
        FollowingUserAdapter adapter = new FollowingUserAdapter(requireContext(), followingUserIds);
        listView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        DatabaseReference followingRef = mDatabase.child("users").child(currentUserId).child("followings");
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Duyệt qua các child trong nút followings để lấy danh sách người đang theo dõi
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String followedUserId = snapshot.getKey(); // Lấy key của mỗi child là userId của người đang được theo dõi
                        Log.d("FollowingUser", "Following user ID: " + followedUserId);
                        // displayFollowingUserInfo(followedUserId);
                        followingUserIds.add(followedUserId);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("FollowingList", "No following users.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("FetchFollowingError", "Error fetching following users: " + databaseError.getMessage());
            }
        });
        return rootview;
    }
}