package com.jackphan.snapgrid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.models.Post;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private LayoutInflater mInflater;
    private OnPostClickListener onPostClickListener;

    // Giao diện để xử lý sự kiện nhấp chuột
    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    // Constructor để khởi tạo adapter
    public PostAdapter(Context context, List<Post> postList, OnPostClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.postList = postList;
        this.onPostClickListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_search, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post currentPost = postList.get(position);

        // Sử dụng Glide để hiển thị hình ảnh
        Glide.with(holder.itemView.getContext())
                .load(currentPost.getImageUrl())
                .centerCrop()
                .into(holder.postImage);

        // Gán sự kiện nhấp chuột
        holder.itemView.setOnClickListener(v -> {
            if (onPostClickListener != null) {
                onPostClickListener.onPostClick(currentPost);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        final ShapeableImageView postImage;

        public PostViewHolder(View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
        }
    }

    // Phương thức để cập nhật dữ liệu của Adapter
    public void updateData(List<Post> newPosts) {
        postList.clear();
        postList.addAll(newPosts);
        notifyDataSetChanged();
    }
}
