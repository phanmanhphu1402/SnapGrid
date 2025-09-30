package com.jackphan.snapgrid.adapters;

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
import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.fragments.CustomDialogFragment;
import com.jackphan.snapgrid.fragments.DetailPostFragment;
import com.jackphan.snapgrid.models.Post;
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
        // Bind ảnh trước
        Picasso.get()
                .load(postsList.get(position).getImageUrl())
                .placeholder(R.drawable.loading_img)
                .into(holder.imageView);

        // Set click listener
        holder.itemView.setOnClickListener( view ->{
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {  // đảm bảo vị trí hợp lệ
                    DetailPostFragment fragment = new DetailPostFragment();
                    CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                    Bundle result = new Bundle();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    // Lấy dữ liệu từ post
                    String image = postsList.get(pos).getImageUrl();
                    String title = postsList.get(pos).getTitle();
                    String content = postsList.get(pos).getContent();
                    String tag = postsList.get(pos).getTag();
                    String idPost = postsList.get(pos).getIdPost();
                    String idUser = postsList.get(pos).getIdUser();

                    // Put vào bundle
                    result.putString("dataImage", image);
                    result.putString("dataTitle", title);
                    result.putString("dataContent", content);
                    result.putString("dataTag", tag);
                    result.putString("dataIdPost", idPost);
                    result.putString("dataIdUser", idUser);

                    fragment.setArguments(result);
                    customDialogFragment.setArguments(result);

                    // Thay fragment
                    transaction.replace(R.id.frame_layout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        );
    }




    @Override
    public int getItemCount() {
        return postsList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_masonry);
        }
    }
}
