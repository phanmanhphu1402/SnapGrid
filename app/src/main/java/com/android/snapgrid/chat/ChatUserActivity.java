package com.android.snapgrid.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.R;
import com.android.snapgrid.adapters.ChaterAdapter;
import com.android.snapgrid.models.ChatUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatUserActivity extends AppCompatActivity {

    ChaterAdapter adapter;
    List<ChatUserModel> list;
    FirebaseUser user;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat_users);

        init();
        fetchUserData();

        clickListener();
    }

    void init(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        adapter = new ChaterAdapter(this, list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    void fetchUserData(){
        CollectionReference reference = FirebaseFirestore.getInstance().collection( "Messages");
        reference.whereArrayContains( "uid", user.getUid()).addSnapshotListener((value, error) -> {
            if(error != null) return;
            if(value.isEmpty()) return;
            list.clear();
            for(QueryDocumentSnapshot snapshot : value){
                ChatUserModel model = snapshot.toObject(ChatUserModel.class);
                list.add(model);
            }
        });
    }

    void clickListener(){
        adapter.OnStarChat(new ChaterAdapter.OnStarChat() {
            @Override
            public void clicked(int position, List<String> uids) {

                String oppositeUID;
                if(!uids.get(0).equalsIgnoreCase(user.getUid())){
                    oppositeUID = uids.get(0);
                } else {
                    oppositeUID = uids.get(1);
                }

                Intent intent = new Intent(ChatUserActivity.this, ChatActivity.class);
                intent.putExtra("uid", oppositeUID);
                startActivity(intent);

            }
        });
    }

}
