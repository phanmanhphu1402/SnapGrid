package com.android.snapgrid.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.snapgrid.ChatToChatActivity;
import com.android.snapgrid.Login;
import com.android.snapgrid.R;
import com.android.snapgrid.adapters.ChatMessageAdapter;
import com.android.snapgrid.adapters.ChatUserAdapter;
import com.android.snapgrid.adapters.CommentAdapter;
import com.android.snapgrid.adapters.MasonryAdapter;
import com.android.snapgrid.models.Comments;
import com.android.snapgrid.models.Post;
import com.android.snapgrid.models.PostSaved;
import com.android.snapgrid.models.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;



public class DetailPostFragment extends Fragment {
    ImageView imageDetail, imgProfile;
    TextView detailPostTitle, detailPostContent, txtNameProfile;
    // Firebase reference
    DatabaseReference mDatabase;
    // Firebase user
    private FirebaseUser currentUser;
    ImageButton btnEditPost, savePostBtn;
    DialogFragment dialog;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Button btnFollow, btnShare;
    ImageButton btnClose;

    String currentUserImage, currentUserName;
    private FirebaseFirestore db;

    private FirebaseFirestore store;

    private PostSaved postSaved = null;

    ArrayList<Comments> commentsArrayList;
    CommentAdapter commentAdapter;

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
        store = FirebaseFirestore.getInstance();
        EditText commentEdt = rootview.findViewById(R.id.editTextText);
        db = FirebaseFirestore.getInstance();


        checkFL(currentUserId, idUser);
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
        btnShare = rootview.findViewById(R.id.btnShare);
        imgProfile = rootview.findViewById(R.id.imageViewAvatar);
        txtNameProfile = rootview.findViewById(R.id.textViewNameProfile);
        savePostBtn = rootview.findViewById(R.id.savePostBtn);
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
                ArrayList<Comments> commentsArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    int idPost = Integer.parseInt(snapshot.child("idPost").getValue().toString());
                    String idPost = snapshot.getKey().toString();
                    String idUser = snapshot.child("idUser").getValue().toString();
                    String content = snapshot.child("content").getValue().toString();
                    String datePost = snapshot.child("datePost").getValue().toString();
                    int numberLike = Integer.parseInt(snapshot.child("numberLike").getValue().toString());
                    for(DataSnapshot itemSnapshot : snapshot.getChildren()){
//                        String idComment = itemSnapshot.child("")
                        Comments comment = new Comments();
                    }
                    int numberComment = Integer.parseInt(snapshot.child("numberComment").getValue().toString());
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String title = snapshot.child("title").getValue().toString();
                    String tag = snapshot.child("tag").getValue().toString();
                    Post post = new Post(idPost, idUser, content, datePost, numberLike, numberComment, commentsArrayList, imageUrl, title, tag);
                    postsList.add(post);
                    recyclerView.setAdapter(new MasonryAdapter(postsList, getActivity().getSupportFragmentManager()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        CollectionReference usersRef = db.collection("User");
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        // Convert each document to a User object
                        String name = (String) document.get("FullName");
                        String image = (String) document.get("Avatar");
                        if(document.get("ID").equals(currentUserId)){
                            currentUserImage = image;
                            currentUserName = name;
                        }

                    }
                } else {
                    Log.e("Error", "Error getting documents: ", task.getException());
                }
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
                followingsMap.put(idUser, true);
                mDatabase.child("users").child(currentUserId).child("followings").setValue(followingsMap);
                followUser(currentUserId, idUser);
            }
        });

        // store.collection(String.format("PostSaved/%s",idUser)).document().
        DatabaseReference postSavedRef = FirebaseDatabase.getInstance().getReference("posts_saved");

        postSavedRef.child(currentUserId).child(Objects.requireNonNull(idPost)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    postSaved = snapshot.getValue(PostSaved.class);
                }
                if (postSaved != null) {
                    if (postSaved.isSaved()) {
                        savePostBtn.setImageResource(R.drawable.share_yellow_color);
                    } else {
                        savePostBtn.setImageResource(R.drawable.share_black_color);
                    }
                } else {
                    savePostBtn.setImageResource(R.drawable.share_black_color);
                }
                savePostBtn.setClickable(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        savePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(DetailPostFragment.class.getName(), "Test");
                savePostBtn.setClickable(false);
                if (postSaved == null) {
                    postSaved = new PostSaved(idPost, new Date(), false);
                }
                boolean currentSaved = postSaved.isSaved();
                postSaved.setSaved(!currentSaved);
                postSavedRef.child(currentUserId).child(Objects.requireNonNull(idPost)).setValue(postSaved, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        String action = "save";
                        if (!postSaved.isSaved()) {
                            action = "remove";
                        }
                        String status = "successfully";
                        if (error != null) {
                            status = "failed";
                            Log.d(DetailPostFragment.class.getName(), error.getMessage());
                            // Rollback
                            postSaved.setSaved(currentSaved);
                        }
                        Toast.makeText(requireContext(), String.format("%s post is %s", action, status), Toast.LENGTH_SHORT).show();
                        savePostBtn.setClickable(true);
                    }
                });
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageDetail.getDrawable();
                if(bitmapDrawable == null){

                }else{
                    Bitmap bitmap = bitmapDrawable.getBitmap();
//                    String title = bundle.getString("dataTitle");
//                    String content = bundle.getString("dataContent");
                    sharePost(title, content, bitmap);
                }
            }
        });

        FirebaseUser finalCurrentUser = currentUser;
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("Posts").child(idPost).child("Comments");

        commentEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View postsCommentDialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_comments, null);
                Button btnComment = postsCommentDialogView.findViewById(R.id.btnAddComment);
                EditText editComment = postsCommentDialogView.findViewById(R.id.editComment);


                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = currentDate.format(dateFormatter);
                String dateComment = formattedDate;

                BottomSheetDialog postsCommentDialog = new BottomSheetDialog(requireContext());
                postsCommentDialog.setContentView(postsCommentDialogView);
                postsCommentDialog.setCanceledOnTouchOutside(true);
                postsCommentDialog.setDismissWithAnimation(true);
                RecyclerView postComments = postsCommentDialogView.findViewById(R.id.commentsList);
                postComments.setLayoutManager(new LinearLayoutManager(getContext()));
                postComments.setHasFixedSize(true);
//                postComments.setAdapter(new CommentAdapter(commentsArrayList, getActivity().getSupportFragmentManager()));
                btnComment.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String key = commentRef.push().getKey();
                        String commentText = editComment.getText().toString();
                        if(TextUtils.isEmpty(commentText)){
                            Toast.makeText(getContext(), "Cant send empty comment", Toast.LENGTH_SHORT).show();
                        }else {
                            System.out.println(commentText);
                            Comments comment = new Comments(key, currentUserId, commentText, dateComment, currentUserImage, currentUserName);
                            commentRef.child(key).setValue(comment);
                            editComment.setText("");
                        }
                    }
                });
                commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        commentsArrayList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String idComment = snapshot.getKey().toString();
                            String idUser = snapshot.child("idUser").getValue().toString();
                            String commentText = snapshot.child("content").getValue().toString();
                            String dateComment = snapshot.child("dateComment").getValue().toString();
                            String imageUser = snapshot.child("imageUser").getValue().toString();
                            String nameUser = snapshot.child("nameUser").getValue().toString();
                            Comments comment = new Comments(idComment, idUser, commentText, dateComment, imageUser, nameUser);
                            System.out.println("Username:"+ nameUser);
                            System.out.println("Username:"+ idUser);
                            System.out.println("Username:"+ imageUser);
                            System.out.println("Username:"+ idComment);
                            System.out.println("Username:"+ dateComment);
                            System.out.println("Username:"+ commentText);
                            commentsArrayList.add(comment);
                            commentAdapter = new CommentAdapter(commentsArrayList, getActivity().getSupportFragmentManager());
                            commentAdapter.notifyDataSetChanged();
                            postComments.setAdapter(commentAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                postsCommentDialog.show();
            }
        });

        return rootview;

    }

    private void sharePost(String pTitle, String pDescription, Bitmap bitmap){
        String shareBody = pTitle +"\n"+ pDescription;
        Uri uri = saveImageToShare(bitmap);
        Intent sIntent = new Intent(Intent.ACTION_SEND);
        sIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sIntent.setType("images/png");
        getContext().startActivity(Intent.createChooser(sIntent, "Share Via"));
    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder = new File(getContext().getCacheDir(), "images");
        Uri uri = null;
        try{
            imageFolder.mkdirs();
            File file = new File(imageFolder, "share_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(getContext(), "com.android.snapgrid.fileprovider", file);
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
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