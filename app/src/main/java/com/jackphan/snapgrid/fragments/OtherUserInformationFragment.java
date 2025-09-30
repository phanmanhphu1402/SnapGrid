package com.jackphan.snapgrid.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jackphan.snapgrid.ChatToChatActivity;
import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.adapters.MasonryAdapter;
import com.jackphan.snapgrid.models.Comments;
import com.jackphan.snapgrid.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class OtherUserInformationFragment extends Fragment {

    TextView txtUserEmail, txtUserDescription, txtFollow, flclick, txtUsername, txtOrtherFLer;
    ImageView userAvatar;

    Button btnFollow, btnChat;

    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_other_user_infor, container, false);

        Bundle bundle = getArguments();
        String otherUser = bundle.getString("dataIdUser");

        txtFollow = rootview.findViewById(R.id.txtFollowing);
        flclick = rootview.findViewById(R.id.tvfollow);
        txtUserEmail = rootview.findViewById(R.id.userEmail);
        txtUserDescription = rootview.findViewById(R.id.userDescription);
        userAvatar = rootview.findViewById(R.id.userAvatar);
        txtUsername = rootview.findViewById(R.id.nameprofile);
        btnChat = rootview.findViewById(R.id.btnChatToChat);
        btnFollow = rootview.findViewById(R.id.btnFollow);
        txtOrtherFLer = rootview.findViewById(R.id.txtPeopleFollower);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatToChatActivity.class);
                intent.putExtra("hisUid", otherUser);
                startActivity(intent);
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(otherUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userEmail = dataSnapshot.child("email").getValue(String.class);
                    String userImageUrl = dataSnapshot.child("profile").getValue(String.class);
                    String userDescription = dataSnapshot.child("description").getValue(String.class);
                    Map<String, Object> userFollowings = (Map<String, Object>) dataSnapshot.child("followings").getValue();
                    Map<String, Object> userFollowers = (Map<String, Object>) dataSnapshot.child("followers").getValue();
                    if (userFollowings == null) {
                        txtFollow.setText(0 + "");
                    } else {
                        txtFollow.setText(userFollowings.size() + "");
                    }
                    if (userFollowers == null) {
                        txtOrtherFLer.setText(0 + "");
                    } else {
                        txtOrtherFLer.setText(userFollowers.size() + "");
                    }
                    txtUserDescription.setText(userDescription);
                    txtUserEmail.setText(userEmail);
                    txtUsername.setText(userName);
                    if (userImageUrl.isEmpty()) {
                        Drawable drawable = getResources().getDrawable(R.drawable.user_default);
                        userAvatar.setImageDrawable(drawable);
                    } else {
                        Picasso.get().load(userImageUrl).placeholder(R.drawable.user_default).into(userAvatar);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("FirebaseError", "Error fetching user data: " + databaseError.getMessage());
            }
        });

        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Post> postsList = new ArrayList<>();
                ArrayList<Comments> commentsArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        int idPost = Integer.parseInt(snapshot.child("idPost").getValue().toString());
                    String idPost = snapshot.getKey().toString();
                    String idUser = snapshot.child("idUser").getValue().toString();
                    String content = snapshot.child("content").getValue().toString();
                    String datePost = snapshot.child("datePost").getValue().toString();
                    int numberLike = Integer.parseInt(snapshot.child("numberLike").getValue().toString());
                    int numberComment = Integer.parseInt(snapshot.child("numberComment").getValue().toString());
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String title = snapshot.child("title").getValue().toString();
                    String tag = snapshot.child("tag").getValue().toString();
                    Post post = new Post(idPost, idUser, content, datePost, numberLike, numberComment, commentsArrayList, imageUrl, title, tag);
                    if (otherUser.equals(snapshot.child("idUser").getValue())) {
                        postsList.add(post);
                        recyclerView.setAdapter(new MasonryAdapter(postsList, getActivity().getSupportFragmentManager()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database = FirebaseDatabase.getInstance();
        btnFollow.setOnClickListener(v -> {
            String buttonText = btnFollow.getText().toString();
            if (buttonText.equals("Follow")) {
                followUser(currentUserId, otherUser);
            } else {
                unfollowUser(currentUserId, otherUser);
            }
        });
        checkFL(currentUserId, otherUser);
        return rootview;
    }

    private void checkFL(String currentUserId, String userId) {
        // Kiểm tra xem người dùng hiện tại đã follow người dùng khác chưa
        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("followings").child(userId);
        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Nếu đã follow
                    btnFollow.setText("Followed");
                    btnFollow.setTextColor(Color.WHITE);
                } else {
                    // Nếu chưa follow
                    btnFollow.setText("Follow");
                    btnFollow.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void unfollowUser(String userId, String friendId) {
        // xóa followings
        mDatabase.child("Users").child(userId).child("followings").child(friendId).removeValue();
        // xóa followers
        mDatabase.child("Users").child(friendId).child("followers").child(userId).removeValue();

        btnFollow.setText("Follow");
        btnFollow.setTextColor(Color.BLACK);
        Toast.makeText(getActivity(), "Unfollowed!", Toast.LENGTH_SHORT).show();
    }

    private void followUser(String userId, String friendId) {
        // Cập nhật data cho người dùng hiện tại
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("followings").child(friendId).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(friendId)
                        .child("followers")
                        .child(userId)
                        .setValue(true);
                // Cập nhật thành công
                Toast.makeText(getActivity(), "Followed successfully!", Toast.LENGTH_SHORT).show();
                //cập nhật UI
                btnFollow.setText("Followed");
                btnFollow.setTextColor(Color.WHITE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý lỗi
                Toast.makeText(getActivity(), "Failed to follow. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}