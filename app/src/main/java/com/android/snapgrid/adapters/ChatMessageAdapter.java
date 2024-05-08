package com.android.snapgrid.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.R;
import com.android.snapgrid.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>{
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;

    ArrayList<Message> messagesList = new ArrayList<>();

    String imageUrl;

    FirebaseUser firebaseUser;

    public ChatMessageAdapter(Context context, ArrayList<Message> messagesList, String imageUrl) {
        this.context = context;
        this.messagesList = messagesList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_messages, parent, false);
            return new ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message = messagesList.get(position).getMessage();
        String time = messagesList.get(position).getTime();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(time));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        holder.chatView.setText(message);
        holder.dateView.setText(dateTime);
        try{
            Picasso.get().load(imageUrl).placeholder(R.drawable.appa).into(holder.avatar);
        }catch(Exception e){

        }

        if (position==messagesList.size()-1){
            if(messagesList.get(position).isSeen()){
                holder.seenView.setText("Seen");
            }else{
                holder.seenView.setText("Delivered");
            }
        }else{
            holder.seenView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(messagesList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chatView, seenView, dateView;
        ImageView avatar;

        ViewHolder(View itemView) {
            super(itemView);
            chatView = itemView.findViewById(R.id.chat);
            seenView = itemView.findViewById(R.id.isSeen);
            dateView = itemView.findViewById(R.id.timeChat);
            avatar = itemView.findViewById(R.id.userAvatar);
        }
    }
}
