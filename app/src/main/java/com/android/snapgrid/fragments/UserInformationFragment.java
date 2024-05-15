package com.android.snapgrid.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.snapgrid.R;
import com.android.snapgrid.SettingActivity;
import com.android.snapgrid.UserConfigActivity;
import com.android.snapgrid.adapters.MasonryAdapter;
import com.android.snapgrid.models.Comments;
import com.android.snapgrid.models.Post;
import com.android.snapgrid.models.PostSaved;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserInformationFragment extends Fragment {
    TextView txtUserEmail, txtFollow, flclick, txtUsername;
    ImageView userAvatar;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;

    private Button showPostsSaved;

    private ArrayList<Post> globalPostsList = new ArrayList<>();


    private ArrayList<String> savePostIdsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_user_information, container, false);
        Button btnConfigInfor = (Button) rootview.findViewById(R.id.btnConfigInfor);
        txtFollow = rootview.findViewById(R.id.txtFollowing);
        flclick = rootview.findViewById(R.id.tvfollow);
        txtUserEmail = rootview.findViewById(R.id.userDescription);
        userAvatar = rootview.findViewById(R.id.userAvatar);
        txtUsername = rootview.findViewById(R.id.nameprofile);
        showPostsSaved = rootview.findViewById(R.id.btnChatToChat);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userEmail = dataSnapshot.child("email").getValue(String.class);
                    String userImageUrl = dataSnapshot.child("profile").getValue(String.class);
                    Map<String, Object> userFollowings = (Map<String, Object>) dataSnapshot.child("followings").getValue();
                    if(userFollowings==null){
                        txtFollow.setText(0+"");
                    }else{
                        txtFollow.setText(userFollowings.size()+"");
                    }
                    txtUserEmail.setText(userEmail);
                    txtUsername.setText(userName);
                    if(userImageUrl.isEmpty()){
                        Drawable drawable = getResources().getDrawable(R.drawable.user_default);
                        userAvatar.setImageDrawable(drawable);
                    }else{
                        Picasso.get().load(userImageUrl).placeholder(R.drawable.appa).into(userAvatar);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("FirebaseError", "Error fetching user data: " + databaseError.getMessage());
            }
        });

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

        //Đổi màu icon khi bật darkmode
        if(isNight()){
            btnSetting.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
        }

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
                ArrayList<Comments> commentsArrayList = new ArrayList<>();
                globalPostsList.clear();
                String userId = currentUser.getUid();
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
                    if (userId.equals(snapshot.child("idUser").getValue())) {
                        postsList.add(post);
                        recyclerView.setAdapter(new MasonryAdapter(postsList, getActivity().getSupportFragmentManager()));
                    }
                    globalPostsList.add(post);
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


        FirebaseDatabase.getInstance().getReference("posts_saved").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savePostIdsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ss : snapshot.getChildren()) {
                        PostSaved postSaved = ss.getValue(PostSaved.class);
                        assert postSaved != null;
                        if (postSaved.isSaved()) {
                            savePostIdsList.add(postSaved.postId);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showPostsSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Post> showList = new ArrayList<>();
                for (Post post : globalPostsList) {
                    if (postInListId(post, savePostIdsList)) {
                        showList.add(post);
                    }
                }
                View postsSavedDialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_show_posts_saved, null);
                BottomSheetDialog postsSavedDialog = new BottomSheetDialog(requireContext());
                postsSavedDialog.setContentView(postsSavedDialogView);
                postsSavedDialog.setCanceledOnTouchOutside(true);
                postsSavedDialog.setDismissWithAnimation(true);
                RecyclerView postsSavedDialogRecyclerView = postsSavedDialogView.findViewById(R.id.rv_posts_saved);
                StaggeredGridLayoutManager postsSavedDialogStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                postsSavedDialogRecyclerView.setLayoutManager(postsSavedDialogStaggeredGridLayoutManager);
                postsSavedDialogRecyclerView.setAdapter(new MasonryAdapter(showList, getActivity().getSupportFragmentManager()));
                postsSavedDialogRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postsSavedDialog.cancel();
                    }
                });
                postsSavedDialog.show();
            }
        });

        return rootview;
    }

    //check nightmode
    private boolean isNight(){
        int config = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isNightMode = Configuration.UI_MODE_NIGHT_YES == config;
        return isNightMode;
    }

    private boolean postInListId(Post post, List<String> ids) {
        for (int idx = 0; idx < ids.size(); idx++) {
            if (ids.get(idx).equals(post.getIdPost())) {
                return true;
            }
        }
        return false;
    }

}