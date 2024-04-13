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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder>{
    ArrayList images;
    ArrayList names;
    ArrayList chats;

    private static Fragment fragment;

    public ChatUserAdapter(Fragment fragment, ArrayList images, ArrayList names, ArrayList chats) {
        this.fragment = fragment;
        this.images = images;
        this.names = names;
        this.chats = chats;
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
        int res = (int) images.get(position);
        holder.imageView.setImageResource(res);
        String name = (String) names.get(position);
        holder.nameView.setText(name);
        String chat = (String) chats.get(position);
        holder.chatView.setText(chat);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView nameView;
        TextView chatView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_avatar_chat);
            nameView = itemView.findViewById(R.id.name_chat);
            chatView = itemView.findViewById(R.id.message);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(fragment.getActivity(), ChatToChatActivity.class);
            fragment.startActivity(intent);
        }
    }
}
