package com.android.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.snapgrid.adapters.MasonryAdapter;
import com.android.snapgrid.R;
import com.android.snapgrid.inter.ItemClickListener;
import com.android.snapgrid.models.Post;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home_page#} factory method to
 * create an instance of this fragment.
 */
public class fragment_home_page extends Fragment{
    private TextView textView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);

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

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int idPost = Integer.parseInt(snapshot.child("idPost").getValue().toString());
                    String idUser = snapshot.child("idUser").getValue().toString();
                    String content = snapshot.child("content").getValue().toString();
                    String datePost = snapshot.child("datePost").getValue().toString();
                    int numberLike = Integer.parseInt(snapshot.child("numberLike").getValue().toString());
                    int numberShare = Integer.parseInt(snapshot.child("numberShare").getValue().toString());
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String title = snapshot.child("title").getValue().toString();
                    Post post = new Post(idPost, idUser, content, datePost, numberLike, numberShare, imageUrl, title);
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

//    Override
//    public void onItemClick(Post post) {
//        // Forward the item data to Fragment B
//        DetailPostFragment fragmentB = new DetailPostFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("itemData", (Serializable) post);
//        fragmentB.setArguments(bundle);
//        // Replace or add Fragment B to the FragmentManager
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame_layout, fragmentB)
//                .commit();
//    }
}