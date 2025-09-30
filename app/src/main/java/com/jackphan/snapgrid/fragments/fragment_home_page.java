package com.jackphan.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackphan.snapgrid.adapters.MasonryAdapter;
import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.models.Comments;
import com.jackphan.snapgrid.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home_page#} factory method to
 * create an instance of this fragment.
 */
public class fragment_home_page extends Fragment {
    private TextView textView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home_page, container, false);
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

                    try {
                        String idPost = snapshot.getKey();
                        if (idPost == null) continue;
                        String idUser = snapshot.child("idUser").getValue(String.class);
                        String content = snapshot.child("content").getValue(String.class);
                        String datePost = snapshot.child("datePost").getValue(String.class);
                        Long numberLikeLong = snapshot.child("numberLike").getValue(Long.class);
                        Long numberCommentLong = snapshot.child("numberComment").getValue(Long.class);
                        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                        String title = snapshot.child("title").getValue(String.class);
                        String tag = snapshot.child("tag").getValue(String.class);

                        int numberLike = numberLikeLong != null ? numberLikeLong.intValue() : 0;
                        int numberComment = numberCommentLong != null ? numberCommentLong.intValue() : 0;

                        if (idUser == null || content == null || datePost == null) continue;

                        Post post = new Post(idPost, idUser, content, datePost, numberLike, numberComment, commentsArrayList, imageUrl,
                                title != null ? title : "",
                                tag != null ? tag : ""
                        );

                        postsList.add(post);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (getActivity() != null) {
                        recyclerView.setAdapter(new MasonryAdapter(postsList, getActivity().getSupportFragmentManager()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println("Firebase error: " + databaseError.getMessage());
            }
        });
        return rootview;

    }
}