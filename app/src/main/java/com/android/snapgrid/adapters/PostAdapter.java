package com.android.snapgrid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.R;
import com.android.snapgrid.models.Post;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private LayoutInflater mInflater;

    public PostAdapter(Context context, List<Post> postList) {
        mInflater = LayoutInflater.from(context);
        this.postList = postList;
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
        holder.postTitle.setText(currentPost.getTitle());
        holder.postContent.setText(currentPost.getContent());
        holder.postDate.setText(currentPost.getDatePost());
        // Thêm mã để hiển thị hình ảnh sử dụng Glide hoặc Picasso ở đây

        // Sử dụng Glide để hiển thị hình ảnh
        Glide.with(holder.itemView.getContext())
                .load(currentPost.getImageUrl())
                .centerCrop() // hoặc transform(new CenterCrop(), new RoundedCorners(16)) nếu bạn muốn bo góc ảnh
                .into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        final ImageView postImage;
        final TextView postTitle;
        final TextView postContent;
        final TextView postDate;

        public PostViewHolder(View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            postTitle = itemView.findViewById(R.id.post_title);
            postContent = itemView.findViewById(R.id.post_content);
            postDate = itemView.findViewById(R.id.post_date);
        }
    }

    // Phương thức để cập nhật dữ liệu của Adapter
    public void updateData(List<Post> newPosts) {
        postList.clear();
        postList.addAll(newPosts);
        notifyDataSetChanged();
    }
}
