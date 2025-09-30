package com.jackphan.snapgrid.adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.fragments.OtherUserInformationFragment;
import com.jackphan.snapgrid.models.User;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class FollowingUserAdapter extends RecyclerView.Adapter<FollowingUserAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<User> users;

    private static FragmentManager fragmentManager;

    public FollowingUserAdapter(Context mContext, ArrayList<User> users, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
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
            Drawable drawable = AppCompatResources.getDrawable(mContext, R.drawable.profile);
            viewHolder.imageUserFollow.setImageDrawable(drawable);
        }else{
            Picasso.get().load(users.get(i).getAvatar()).placeholder(R.drawable.loading_img).into(viewHolder.imageUserFollow);
        }
        String name = users.get(i).getName();
        String id = users.get(i).getId();
        viewHolder.userFollowName.setText(name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtherUserInformationFragment childFragment = new OtherUserInformationFragment();
                Bundle result = new Bundle();
                result.putString("dataIdUser", id);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                childFragment.setArguments(result);
                transaction.replace(R.id.frame_layout, childFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
