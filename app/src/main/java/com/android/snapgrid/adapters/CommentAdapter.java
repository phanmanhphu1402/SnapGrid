package com.android.snapgrid.adapters;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.R;
import com.android.snapgrid.fragments.CustomDialogFragment;
import com.android.snapgrid.fragments.DetailPostFragment;
import com.android.snapgrid.models.Comments;
import com.android.snapgrid.models.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<Comments> commentsList = new ArrayList<>();
    private static FragmentManager fragmentManager;

    public CommentAdapter(ArrayList<Comments> commentsList, FragmentManager fragmentManager) {
        this.commentsList = commentsList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_comment, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String content = commentsList.get(i).getContent();
        String time = commentsList.get(i).getDateComment();
        String image = commentsList.get(i).getImageUser();
        String name = commentsList.get(i).getNameUser();

        viewHolder.commentView.setText(content);
        viewHolder.dateView.setText(time);
        viewHolder.nameView.setText(name);
        try{
            Picasso.get().load(image).placeholder(R.drawable.appa).into(viewHolder.imageView);
        }catch(Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameView, commentView, dateView;
        ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameUser);
            commentView = itemView.findViewById(R.id.commentUser);
            dateView = itemView.findViewById(R.id.dateCommentUser);
            imageView = itemView.findViewById(R.id.imageUser);
        }

    }
}
