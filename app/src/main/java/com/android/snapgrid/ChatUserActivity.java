package com.android.snapgrid;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.adapters.ChaterAdapter;
import com.android.snapgrid.models.ChatUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
}
