package com.android.snapgrid.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.R;
import com.android.snapgrid.models.ChatUserModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChaterAdapter extends RecyclerView.Adapter<ChaterAdapter.ChatUserHolder>{

    Activity context;
    List<ChatUserModel> list;

    public OnStarChat starChat;

    public ChaterAdapter(Activity context, List<ChatUserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_items, parent, false);

        return new ChatUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserHolder holder, int position) {

        fetchImageUrl(list.get(position).getUid(), holder);

//        holder.time.setText(list.get(position).getTime().toString());

        holder.itemView.setOnClickListener(v->{

            starChat.clicked(position, list.get(position).getUid());
        });

    }

    void fetchImageUrl(List<String> uids, ChatUserHolder holder){
        String oppsiteUID;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(!uids.get(0).equalsIgnoreCase(user.getUid())){
            oppsiteUID = uids.get(0);
        } else {
            oppsiteUID = uids.get(1);
        }

        FirebaseFirestore.getInstance().collection("Users").document(oppsiteUID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            Glide.with(context.getApplicationContext()).load(snapshot.getString("profileImage")).into(holder.imageView);
                            holder.name.setText(snapshot.getString("name"));
                        } else {
                            assert task.getException() != null;
                            Toast.makeText(context, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ChatUserHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name;

        public ChatUserHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.nameTV);
        }
    }

    public interface OnStarChat{
        void clicked(int position, List<String> uids);
    }
    public void OnStarChat(OnStarChat starChat){
        this.starChat = starChat;
    }

}
