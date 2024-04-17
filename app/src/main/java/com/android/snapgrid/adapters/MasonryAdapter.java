package com.android.snapgrid.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.android.snapgrid.R;
import com.android.snapgrid.fragments.CustomDialogFragment;
import com.android.snapgrid.fragments.DetailPostFragment;
import com.android.snapgrid.models.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.ViewHolder> {
    static ArrayList<String> images;

    private static ArrayList<Post> postsList;
    Context context;
    private static FragmentManager fragmentManager;

    // Constructor for initialization
    public MasonryAdapter(Context context, ArrayList images) {
        this.context = context;
        this.images = images;
    }

    public MasonryAdapter(ArrayList<Post> postsList, FragmentManager fragmentManager) {
        this.postsList = postsList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_masonry, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(context).load(image).placeholder(R.drawable.appa).into(holder.imageView);
        Picasso.get().load(postsList.get(position).getImageUrl()).placeholder(R.drawable.appa).into(holder.imageView);

//        int res = (int) images.get(position);
//        holder.imageView.setImageResource(res);
    }



    @Override
    public int getItemCount() {
        return postsList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_masonry);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            DetailPostFragment fragment = new DetailPostFragment();
            CustomDialogFragment customDialogFragment = new CustomDialogFragment();
            Bundle result = new Bundle();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            String image = postsList.get(getAdapterPosition()).getImageUrl();
            String title = postsList.get(getAdapterPosition()).getTitle();
            String content = postsList.get(getAdapterPosition()).getContent();
            String tag = postsList.get(getAdapterPosition()).getTag();
            String idPost = postsList.get(getAdapterPosition()).getIdPost();
            String idUser = postsList.get(getAdapterPosition()).getIdUser();
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
    }
}
