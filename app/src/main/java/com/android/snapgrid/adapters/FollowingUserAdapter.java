package com.android.snapgrid.adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.ChatToChatActivity;
import com.android.snapgrid.R;
import com.android.snapgrid.fragments.CustomDialogFragment;
import com.android.snapgrid.fragments.DetailPostFragment;
import com.android.snapgrid.models.User;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class FollowingUserAdapter extends RecyclerView.Adapter<FollowingUserAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<User> users;

    public FollowingUserAdapter(Context mContext, ArrayList<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_following, viewGroup, false);
        return new FollowingUserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(users.get(i).getAvatar().isEmpty()){
            Drawable drawable = mContext.getDrawable(R.drawable.user_default);
            viewHolder.imageUserFollow.setImageDrawable(drawable);
        }else{
            Picasso.get().load(users.get(i).getAvatar()).placeholder(R.drawable.user_default).into(viewHolder.imageUserFollow);
        }
        String name = users.get(i).getName();
        String id = users.get(i).getId();
        viewHolder.userFollowName.setText(name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatToChatActivity.class);
                intent.putExtra("hisName", name);
                intent.putExtra("hisUid", id);
                intent.putExtra("hisImage", id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageUserFollow;

        TextView userFollowName;
        ViewHolder(View itemView) {
            super(itemView);
            imageUserFollow = itemView.findViewById(R.id.imageUserFollow);
            userFollowName = itemView.findViewById(R.id.nameUserFollow);
        }
    }
}
