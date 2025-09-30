package com.jackphan.snapgrid.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

        int pos = holder.getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {  // đảm bảo vị trí hợp lệ
            String message = messagesList.get(pos).getMessage();
            String time = messagesList.get(pos).getTime();

            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(time));
            String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

            holder.chatView.setText(message);
            holder.dateView.setText(dateTime);
            try {
                Picasso.get().load(imageUrl).placeholder(R.drawable.loading_img).into(holder.avatar);
            } catch (Exception e) {

            }


            holder.messageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure you want to delete this message");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteMessage(pos);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.create().show();
                }
            });

            if (pos == messagesList.size() - 1) {
                if (messagesList.get(pos).isSeen()) {
                    holder.seenView.setText("Seen");
                } else {
                    holder.seenView.setText("Delivered");
                }
            } else {
                holder.seenView.setVisibility(View.GONE);
            }
        }
    }

    private void deleteMessage(int i) {
        String myUID = FirebaseAuth.getInstance().getUid();
        String msgTimeStamp = messagesList.get(i).getTime();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("time").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.child("sender").getValue().equals(myUID)){
//                        ds.getRef().removeValue();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", "This message was deleted.....");
                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "You can only delete your message", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        ConstraintLayout messageLayout;

        ViewHolder(View itemView) {
            super(itemView);
            chatView = itemView.findViewById(R.id.chat);
            seenView = itemView.findViewById(R.id.isSeen);
            dateView = itemView.findViewById(R.id.timeChat);
            avatar = itemView.findViewById(R.id.userAvatar);
            messageLayout = itemView.findViewById(R.id.messageLayout);
        }
    }
}
