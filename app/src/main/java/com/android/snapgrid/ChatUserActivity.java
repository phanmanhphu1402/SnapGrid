package com.android.snapgrid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.adapters.ChaterAdapter;
import com.android.snapgrid.models.ChatUserModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatUserActivity extends AppCompatActivity {

    ChaterAdapter adapter;
    List<ChatUserModel> list;
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
    }

    void fetchUserData(){
        CollectionReference reference = FirebaseFirestore.getInstance().collection( "Messages");
//        reference.whereArrayContains();
    }
}
