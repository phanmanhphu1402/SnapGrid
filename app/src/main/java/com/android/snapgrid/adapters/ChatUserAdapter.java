package com.android.snapgrid.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.ChatToChatActivity;
import com.android.snapgrid.R;
import com.android.snapgrid.models.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder>{
    private ArrayList<User> users;

    private static Fragment fragment;

    public ChatUserAdapter(Fragment fragment, ArrayList users) {
        this.fragment = fragment;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_users, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(users.get(position).getAvatar()).placeholder(R.drawable.appa).into(holder.imageView);
        String name = users.get(position).getName();
        String id = users.get(position).getId();
        holder.nameView.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragment.getActivity(), ChatToChatActivity.class);
                intent.putExtra("hisName", name);
                intent.putExtra("hisUid", id);
                intent.putExtra("hisImage", id);
                fragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_avatar_chat);
            nameView = itemView.findViewById(R.id.name_chat);

        }
    }
}
