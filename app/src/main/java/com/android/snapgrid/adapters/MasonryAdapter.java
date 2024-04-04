package com.android.snapgrid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.android.snapgrid.R;
import com.android.snapgrid.fragments.DetailPostFragment;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.ViewHolder> {
    static ArrayList<String> images;

    private ArrayList<String> imageUrlList;
    Context context;
    private static FragmentManager fragmentManager;
    private OnItemClickListener mListener;

    // Constructor for initialization
    public MasonryAdapter(Context context, ArrayList images) {
        this.context = context;
        this.images = images;
    }

    public MasonryAdapter(ArrayList images, OnItemClickListener listener) {
        this.images = images;
        this.mListener = listener;
    }

    public MasonryAdapter(ArrayList images) {
        this.images = images;
    }
    public MasonryAdapter(ArrayList<String> images, FragmentManager fragmentManager) {
        this.images = images;
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
        String image = images.get(position);
        Glide.with(context).load(image).placeholder(R.drawable.appa).into(holder.imageView);
//        int res = (int) images.get(position);
//        holder.imageView.setImageResource(res);
    }



    @Override
    public int getItemCount() {
        return images.size();
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
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            try {
                transaction.replace(R.id.frame_layout, DetailPostFragment.class.newInstance());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
