package com.android.snapgrid.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.R;
import com.android.snapgrid.fragments.CustomDialogFragment;
import com.android.snapgrid.fragments.DetailPostFragment;
import com.android.snapgrid.models.Notify;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Notify> notifies;

    private static FragmentManager fragmentManager;

    public NotifyAdapter(Context mContext, ArrayList<Notify> notifies, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.notifies = notifies;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_notify, viewGroup, false);
        return new NotifyAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts").child(notifies.get(i).getIdPost());
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String imageUrlPost = dataSnapshot.child("imageUrl").getValue().toString();
                    Picasso.get().load(imageUrlPost).placeholder(R.drawable.user_default).into(viewHolder.imagePost);
                    String datePublicPost = dataSnapshot.child("datePost").getValue().toString();
                    viewHolder.datePost.setText(datePublicPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(notifies.get(i).getIdUser());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String nameUserPost = dataSnapshot.child("name").getValue().toString();
                    viewHolder.contentPost.setText(nameUserPost+" Đã đăng bài viết");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DetailPostFragment fragment = new DetailPostFragment();
                        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                        Bundle result = new Bundle();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        String image = dataSnapshot.child("imageUrl").getValue().toString();
                        String title = dataSnapshot.child("title").getValue().toString();
                        String content = dataSnapshot.child("content").getValue().toString();
                        String tag = dataSnapshot.child("tag").getValue().toString();
                        String idPost = dataSnapshot.child("idPost").getValue().toString();
                        String idUser = dataSnapshot.child("idUser").getValue().toString();
                        result.putString("dataImage", image);
                        result.putString("dataTitle", title);
                        result.putString("dataContent", content);
                        result.putString("dataTag", tag);
                        result.putString("dataIdPost", idPost);
                        result.putString("dataIdUser", idUser);
                        fragment.setArguments(result);
                        customDialogFragment.setArguments(result);
                        transaction.replace(R.id.frame_layout, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imagePost;

        TextView contentPost, datePost;
        ViewHolder(View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.imagePost);
            contentPost = itemView.findViewById(R.id.contentPost);
            datePost = itemView.findViewById(R.id.datePost);
        }
    }
}
