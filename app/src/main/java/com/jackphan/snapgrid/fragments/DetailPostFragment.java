package com.jackphan.snapgrid.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.jackphan.snapgrid.Login;
import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.adapters.CommentAdapter;
import com.jackphan.snapgrid.adapters.MasonryAdapter;
import com.jackphan.snapgrid.models.Comments;
import com.jackphan.snapgrid.models.Post;
import com.jackphan.snapgrid.models.PostSaved;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


public class DetailPostFragment extends Fragment {
    ImageView imageDetail, imgProfile;
    TextView detailPostTitle, detailPostContent, txtNameProfile, txtFollowerCount, txtCommentCount, txtComment;
    // Firebase reference
    DatabaseReference mDatabase;
    DatabaseReference PostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
    ;
    // Firebase user
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    ;
    ImageButton btnEditPost;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    String currentUserId = currentUser.getUid();
    Button btnFollow, btnShare, savePostBtn;

    String currentUserImage, currentUserName;

    private FirebaseFirestore store;

    private PostSaved postSaved = null;

    ArrayList<Comments> commentsArrayList;
    CommentAdapter commentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_detail_post, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getArguments();
        String image = bundle.getString("dataImage");
        String title = bundle.getString("dataTitle");
        String content = bundle.getString("dataContent");
        String tag = bundle.getString("dataTag");
        String idPost = bundle.getString("dataIdPost");
        String idUser = bundle.getString("dataIdUser");
        Bundle result = new Bundle();
        btnEditPost = rootview.findViewById(R.id.btnEditPost);
        txtFollowerCount = rootview.findViewById(R.id.followerCountText);
        imageDetail = rootview.findViewById(R.id.detailImage);
        detailPostTitle = rootview.findViewById(R.id.detailPostTitle);
        detailPostContent = rootview.findViewById(R.id.detailPostContent);// Khởi tạo Firebase
        btnFollow = rootview.findViewById(R.id.buttonFollow);
        btnShare = rootview.findViewById(R.id.btnShare);
        imgProfile = rootview.findViewById(R.id.imageViewAvatar);
        txtNameProfile = rootview.findViewById(R.id.textViewNameProfile);
        savePostBtn = rootview.findViewById(R.id.savePostBtn);
        txtCommentCount = rootview.findViewById(R.id.txtCommentCount);
        txtComment = rootview.findViewById(R.id.comment);
        if (currentUserId.equals(idUser)) {
            btnFollow.setVisibility(View.GONE);
        }

        checkFL(currentUserId, idUser);
        System.out.println(currentUserId);
        System.out.println(idUser);
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

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtherUserInformationFragment childFragment = new OtherUserInformationFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                Bundle result = new Bundle();
                result.putString("dataIdUser", idUser);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                childFragment.setArguments(result);
                transaction.replace(R.id.frame_layout, childFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        PostDatabase.child(idPost).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int numberComment = 0;
                    String postTitle = dataSnapshot.child("title").getValue(String.class);
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    String datePost = dataSnapshot.child("datePost").getValue(String.class);
                    int numberLike = dataSnapshot.child("numberLike").getValue(Integer.class);
                    String tag = dataSnapshot.child("tag").getValue(String.class);
                    Map<String, Object> comments = (Map<String, Object>) dataSnapshot.child("Comments").getValue();
                    if (comments == null) {
                        numberComment = 0;
                    } else {
                        numberComment = comments.size();
                    }

                    detailPostTitle.setText(postTitle);
                    detailPostContent.setText(content);
                    txtCommentCount.setText(numberComment + "");
                    Picasso.get().load(imageUrl).placeholder(R.drawable.user_default).into(imageDetail);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (currentUser == null) {
            // Người dùng chưa đăng nhập, quay về trang đăng nhập hoặc thực hiện hành động phù hợp
            Intent i = new Intent(getActivity(), Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish(); // Kết thúc Activity chứa Fragment
        }
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
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
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
        database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.child(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Convert each document to a User object
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String image = dataSnapshot.child("profile").getValue(String.class);
                    currentUserImage = image;
                    currentUserName = name;
                }
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(idUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userImageUrl = dataSnapshot.child("profile").getValue(String.class);
                    Map<String, Object> userFollowings = (Map<String, Object>) dataSnapshot.child("followers").getValue();
                    if (userFollowings == null) {
                        txtFollowerCount.setText(0 + " Người theo dõi");
                    } else {
                        txtFollowerCount.setText(userFollowings.size() + " Người theo dõi");
                    }

                    txtNameProfile.setText(userName);
                    if (userImageUrl.isEmpty()) {
                        Drawable drawable = getResources().getDrawable(R.drawable.user_default);
                        imgProfile.setImageDrawable(drawable);
                    } else {
                        Picasso.get().load(userImageUrl).placeholder(R.drawable.loading_img).into(imgProfile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("FirebaseError", "Error fetching user data: " + databaseError.getMessage());
            }
        });
        btnFollow.setOnClickListener(v -> {
            String buttonText = btnFollow.getText().toString();
            if (buttonText.equals("Follow")) {
                followUser(currentUserId, idUser);
            } else {
                unfollowUser(currentUserId, idUser);
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
                        savePostBtn.setText("Saved");
                        savePostBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.main_text_color));
                        savePostBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_color));
                    } else {
                        savePostBtn.setText("Save");
                        savePostBtn.setTextColor(Color.WHITE);
                        savePostBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary_color));
                    }
                } else {
                    savePostBtn.setText("Save");
                    savePostBtn.setTextColor(Color.WHITE);
                    savePostBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary_color));
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
                if (bitmapDrawable == null) {
                    shareTextOnly(title, content);

                } else {
                    Bitmap bitmap = bitmapDrawable.getBitmap();
//                    String title = bundle.getString("dataTitle");
//                    String content = bundle.getString("dataContent");
                    sharePost(title, content, bitmap);
                }
            }
        });

        FirebaseUser finalCurrentUser = currentUser;
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("Posts").child(idPost).child("Comments");
        DatabaseReference PostRef = FirebaseDatabase.getInstance().getReference("Posts").child(idPost);
        txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View postsCommentDialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_comments, null);
                Button btnComment = postsCommentDialogView.findViewById(R.id.btnAddComment);
                TextView txtCountComment = postsCommentDialogView.findViewById(R.id.txtCommentCount);
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
                        if (TextUtils.isEmpty(commentText)) {
                            Toast.makeText(getContext(), "Cant send empty comment", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println(commentText);
                            Comments comment = new Comments(key, currentUserId, commentText, dateComment, currentUserImage, currentUserName);
                            commentRef.child(key).setValue(comment);
                            editComment.setText("");

                            commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int numberComment = 0;
                                    commentsArrayList = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String idComment = snapshot.getKey().toString();
                                        String idUser = snapshot.child("idUser").getValue().toString();
                                        String commentText = snapshot.child("content").getValue().toString();
                                        String dateComment = snapshot.child("dateComment").getValue().toString();
                                        String imageUser = snapshot.child("imageUser").getValue().toString();
                                        String nameUser = snapshot.child("nameUser").getValue().toString();
                                        Comments comment = new Comments(idComment, idUser, commentText, dateComment, imageUser, nameUser);
                                        commentsArrayList.add(comment);

                                        commentAdapter = new CommentAdapter(commentsArrayList, getActivity().getSupportFragmentManager());
                                        commentAdapter.notifyDataSetChanged();
                                        postComments.setAdapter(commentAdapter);
                                        numberComment++;
                                        txtCommentCount.setText(numberComment + "");
                                        txtCountComment.setText(numberComment + "");
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                });
                commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        commentsArrayList = new ArrayList<>();
                        int numberComment = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String idComment = snapshot.getKey().toString();
                            String idUser = snapshot.child("idUser").getValue().toString();
                            String commentText = snapshot.child("content").getValue().toString();
                            String dateComment = snapshot.child("dateComment").getValue().toString();
                            String imageUser = snapshot.child("imageUser").getValue().toString();
                            String nameUser = snapshot.child("nameUser").getValue().toString();
                            Comments comment = new Comments(idComment, idUser, commentText, dateComment, imageUser, nameUser);
                            commentsArrayList.add(comment);
                            commentAdapter = new CommentAdapter(commentsArrayList, getActivity().getSupportFragmentManager());
                            commentAdapter.notifyDataSetChanged();
                            postComments.setAdapter(commentAdapter);
                            numberComment++;
                            txtCommentCount.setText(numberComment + "");
                            txtCountComment.setText(numberComment + "");
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

    private void unfollowUser(String userId, String friendId) {
        // xóa followings
        mDatabase.child("Users").child(userId).child("followings").child(friendId).removeValue();
        // xóa followers
        mDatabase.child("Users").child(friendId).child("followers").child(userId).removeValue();

        btnFollow.setText("Follow");
        btnFollow.setTextColor(Color.BLACK);
        Toast.makeText(getActivity(), "Unfollowed!", Toast.LENGTH_SHORT).show();
    }

    private void shareTextOnly(String title, String content) {
        String shareBody = title + "\n" + content;
        Intent sIntent = new Intent(Intent.ACTION_SEND);
        sIntent.setType("text/plain");
        sIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        getContext().startActivity(Intent.createChooser(sIntent, "Share Via"));

    }

    private void sharePost(String pTitle, String pDescription, Bitmap bitmap) {
        String shareBody = pTitle + "\n" + pDescription;
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
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "share_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(getContext(), "com.jackphan.snapgrid.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    private void checkFL(String currentUserId, String userId) {

        // Kiểm tra xem người dùng hiện tại đã follow người dùng của bài đăng này chưa
        DatabaseReference followingRef = mDatabase.child("Users").child(currentUserId).child("followings").child(userId);
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