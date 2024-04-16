package com.android.snapgrid.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.snapgrid.MainActivity;
import com.android.snapgrid.R;
import com.android.snapgrid.SettingActivity;
import com.android.snapgrid.UserConfigActivity;
import com.android.snapgrid.adapters.MasonryAdapter;
import com.android.snapgrid.models.Post;
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
    TextView userEmail, txtFollow, flclick;
    ImageView userAvatar;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_user_information, container, false);
        Button btnConfigInfor = (Button) rootview.findViewById(R.id.btnConfigInfor);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        txtFollow = rootview.findViewById(R.id.txtFollowing);
        flclick = rootview.findViewById(R.id.tvfollow);
        userEmail = (TextView) rootview.findViewById(R.id.userEmail);
        userAvatar = rootview.findViewById(R.id.userAvatar);
        Picasso.get().load(currentUser.getPhotoUrl()).placeholder(R.drawable.appa).into(userAvatar);
        userEmail.setText(currentUser.getEmail().toString());
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        DatabaseReference followingRef = mDatabase.child("users").child(currentUserId).child("followings");
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem nút followings có dữ liệu hay không
                if (dataSnapshot.exists()) {
                    // Lấy số lượng người đang theo dõi
                    long followingCount = dataSnapshot.getChildrenCount();
                    String followingCountString = String.valueOf(followingCount);
                    txtFollow.setText(followingCountString);
                    Log.d("FollowingCount", "Number of following users: " + followingCount);

                    // Duyệt qua các child trong nút followings để lấy danh sách người đang theo dõi
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String followedUserId = snapshot.getKey(); // Lấy key của mỗi child là userId của người đang được theo dõi
                        Log.d("FollowingUser", "Following user ID: " + followedUserId);
                        // displayFollowingUserInfo(followedUserId);
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Post> postsList = new ArrayList<>();
                String userId = currentUser.getUid();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (userId.equals(snapshot.child("idUser").getValue())) {
//                        int idPost = Integer.parseInt(snapshot.child("idPost").getValue().toString());
                        String idPost = snapshot.getKey().toString();
                        String idUser = snapshot.child("idUser").getValue().toString();
                        String content = snapshot.child("content").getValue().toString();
                        String datePost = snapshot.child("datePost").getValue().toString();
                        int numberLike = Integer.parseInt(snapshot.child("numberLike").getValue().toString());
                        int numberShare = Integer.parseInt(snapshot.child("numberShare").getValue().toString());
                        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                        String title = snapshot.child("title").getValue().toString();
                        String tag = snapshot.child("tag").getValue().toString();
                        Post post = new Post(idPost, idUser, content, datePost, numberLike, numberShare, imageUrl, title, tag);
                        postsList.add(post);
                        recyclerView.setAdapter(new MasonryAdapter(postsList, getActivity().getSupportFragmentManager()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FragmentManager fragmentManager = getParentFragmentManager();
        flclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    ListFollowFragment fragment = new ListFollowFragment();
                    transaction.replace(R.id.frame_layout, fragment);
                    transaction.addToBackStack(null); // (Optional) Thêm transaction vào back stack
                    transaction.commit();
                } else {
                    Log.e("FragmentManager", "FragmentManager is null");
                    // Xử lý khi fragmentManager là null (nếu cần)
                }
            }
        });
        return rootview;

    }

}