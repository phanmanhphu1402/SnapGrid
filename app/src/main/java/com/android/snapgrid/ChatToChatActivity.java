package com.android.snapgrid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.snapgrid.adapters.ChatMessageAdapter;
import com.android.snapgrid.adapters.ChatUserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.android.snapgrid.models.Message;

public class ChatToChatActivity extends AppCompatActivity {
    TextView receiverName;

    ImageButton btnSendMessage;

    EditText editMessage;

    RecyclerView recyclerView;

    FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore db;
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    ArrayList<Message> messageList;
    ChatMessageAdapter chatMessageAdapter;

    String currentUserId;
    String id;
    String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_to_chat);
        receiverName = (TextView) findViewById(R.id.receiverName);
        btnSendMessage = (android.widget.ImageButton) findViewById(R.id.btnSendMessage);
        editMessage = (EditText) findViewById(R.id.input_chat);
        Intent intent = getIntent();
        String name = intent.getStringExtra("hisName");
        image = intent.getStringExtra("hisImage");
        id = intent.getStringExtra("hisUid");
        receiverName.setText(name);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
        CollectionReference usersRef = db.collection("User");
        Query query = usersRef.whereEqualTo("ID", id);
        recyclerView = findViewById(R.id.recycleMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Handle the found user document
                        String userId = document.getString("ID");
                        System.out.println(userId);
                        // You can retrieve other user data here
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(ChatToChatActivity.this, "Cant send empty message", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(message);
                }
            }
        });

        readMessage();

        seenMessage();


    }

    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String chat = ds.child("message").getValue().toString();
                    String sender = ds.child("sender").getValue().toString();
                    String receiver = ds.child("receiver").getValue().toString();
                    Boolean isSeen = (Boolean)ds.child("isSeen").getValue();
                    String time =  ds.child("time").getValue().toString();
                    Message message = new Message(chat, sender, receiver, time, isSeen);
                    if(message.getReceiver().equals(currentUserId) && message.getSender().equals(id)){
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage() {
        messageList = new ArrayList<>();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Chats");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String chat = ds.child("message").getValue().toString();
                    String sender = ds.child("sender").getValue().toString();
                    String receiver = ds.child("receiver").getValue().toString();
                    Boolean isSeen = (Boolean)ds.child("isSeen").getValue();
                    String time =  ds.child("time").getValue().toString();
                    System.out.println(time);
                    Message message = new Message(chat, sender, receiver, time, isSeen);
                    if(message.getReceiver().equals(currentUserId) && message.getSender().equals(id) ||
                            message.getReceiver().equals(id) && message.getSender().equals(currentUserId)){
                        messageList.add(message);
                    }

                    chatMessageAdapter = new ChatMessageAdapter(ChatToChatActivity.this, messageList, image);
                    chatMessageAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(chatMessageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timeSend = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", currentUserId);
        hashMap.put("receiver", id);
        hashMap.put("message", message);
        hashMap.put("time", timeSend);
        hashMap.put("isSeen", false);
        databaseReference.child("Chats").push().setValue(hashMap);
        editMessage.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }
}