package com.android.snapgrid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.snapgrid.R;
import com.android.snapgrid.adapters.MasonryAdapter;
import com.android.snapgrid.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailPostFragment extends Fragment {
    ImageView imageDetail;
    TextView detailPostTitle, detailPostContent;
    ImageButton btnEditPost;
    DialogFragment dialog;

    ImageButton btnClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_detail_post, container, false);
        Bundle bundle = getArguments();
        String image = bundle.getString("dataImage");
        String title = bundle.getString("dataTitle");
        String content = bundle.getString("dataContent");
        Bundle result = new Bundle();

        btnEditPost = rootview.findViewById(R.id.btnEditPost);
        btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.show(getChildFragmentManager(),"CustomDialogFragment");
                result.putString("dataImage", image);
                result.putString("dataTitle", title);
                result.putString("dataContent", content);
                customDialogFragment.setArguments(result);
            }
        });
        imageDetail = rootview.findViewById(R.id.detailImage);
        detailPostTitle = rootview.findViewById(R.id.detailPostTitle);
        detailPostContent = rootview.findViewById(R.id.detailPostContent);
        if(title != null){
            detailPostTitle.setText(title);
        }else{
            detailPostTitle.setText("");
        }
        if(content != null){
            detailPostContent.setText(content);
        }else{
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
                    int idPost = Integer.parseInt(snapshot.child("idPost").getValue().toString());
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
        return rootview;

    }
}