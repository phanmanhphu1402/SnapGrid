package com.android.snapgrid.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.snapgrid.Login;
import com.android.snapgrid.R;
import com.android.snapgrid.adapters.MasonryAdapter;
import com.android.snapgrid.models.Post;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailPostFragment extends Fragment {
    ImageView imageDetail, imgProfile;
    TextView detailPostTitle, detailPostContent, txtNameProfile;
    // Firebase reference
    DatabaseReference mDatabase;
    // Firebase user
    private FirebaseUser currentUser;
    ImageButton btnEditPost;
    DialogFragment dialog;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Button btnFollow;
    ImageButton btnClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_detail_post, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        Bundle bundle = getArguments();
        String image = bundle.getString("dataImage");
        String title = bundle.getString("dataTitle");
        String content = bundle.getString("dataContent");
        String tag = bundle.getString("dataTag");
        String idPost = bundle.getString("dataIdPost");
        String idUser = bundle.getString("dataIdUser");
        Bundle result = new Bundle();
        btnEditPost = rootview.findViewById(R.id.btnEditPost);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        checkFL(currentUserId,idUser);
        btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.show(getChildFragmentManager(), "CustomDialogFragment");
                result.putString("dataImage", image);
                result.putString("dataTitle", title);
                result.putString("dataContent", content);
                result.putString("dataTag", tag);
                result.putString("dataIdPost", idPost);
                result.putString("dataIdUser", idUser);
                customDialogFragment.setArguments(result);
            }
        });
        imageDetail = rootview.findViewById(R.id.detailImage);
        detailPostTitle = rootview.findViewById(R.id.detailPostTitle);
        detailPostContent = rootview.findViewById(R.id.detailPostContent);// Khởi tạo Firebase
        btnFollow = rootview.findViewById(R.id.buttonFollow);
        imgProfile = rootview.findViewById(R.id.imageViewAvatar);
        txtNameProfile = rootview.findViewById(R.id.textViewNameProfile);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            // Người dùng chưa đăng nhập, quay về trang đăng nhập hoặc thực hiện hành động phù hợp
            Intent i = new Intent(getActivity(), Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish(); // Kết thúc Activity chứa Fragment
        }

        if (title != null) {
            detailPostTitle.setText(title);
        } else {
            detailPostTitle.setText("");
        }
        if (content != null) {
            detailPostContent.setText(content);
        } else {
            detailPostContent.setText("");
        }
        Picasso.get().load(image).placeholder(R.drawable.appa).into(imageDetail);
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Post> postsList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    int idPost = Integer.parseInt(snapshot.child("idPost").getValue().toString());
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(idUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userImageUrl = dataSnapshot.child("profile").getValue(String.class);
                    txtNameProfile.setText(userName);
                    Picasso.get().load(userImageUrl).placeholder(R.drawable.appa).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("FirebaseError", "Error fetching user data: " + databaseError.getMessage());
            }
        });
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Boolean> followingsMap = new HashMap<>();
                followingsMap.put(idUser,true);
                mDatabase.child("users").child(currentUserId).child("followings").setValue(followingsMap);
                followUser(currentUserId, idUser);
            }
        });
        return rootview;

    }

    private void checkFL(String currentUserId, String userId) {
        // Kiểm tra xem người dùng hiện tại đã follow người dùng của bài đăng này chưa
        DatabaseReference followingRef = mDatabase.child("users").child(currentUserId).child("followings").child(userId);
        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Nếu đã follow
                    btnFollow.setText("Followed");
                    btnFollow.setEnabled(false); // Tắt nút follow vì đã follow rồi
                } else {
                    // Nếu chưa follow
                    btnFollow.setText("Follow");
                    btnFollow.setEnabled(true); // Cho phép người dùng follow
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void followUser(String userId, String friendId) {
        // Cập nhật data cho người dùng hiện tại
        mDatabase.child("users").child(userId).child("followings").child(friendId).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Cập nhật thành công
                Toast.makeText(getActivity(), "Followed successfully!", Toast.LENGTH_SHORT).show();
                //cập nhật UI
                btnFollow.setText("Followed");
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