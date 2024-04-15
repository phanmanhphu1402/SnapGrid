package com.android.snapgrid.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.R;
import com.android.snapgrid.models.ChatUserModel;
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
//                            Glide.with(context.getApplicationContext()).load(snapshot.getString("profileImage"));
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

}
