package com.android.snapgrid.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.models.ChatUserModel;

import java.util.List;

public class ChaterAdapter extends RecyclerView.Adapter<ChaterAdapter.ChatUserHolder>{

    Activity context;
    List<ChatUserModel> list;

    public ChaterAdapter(Activity context, List<ChatUserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ChatUserHolder extends RecyclerView.ViewHolder{
        public ChatUserHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
