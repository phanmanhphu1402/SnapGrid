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
import com.android.snapgrid.service.APIService;
import com.android.snapgrid.service.RetrofitClient;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.snapgrid.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    String userName;
    String image;

    String otherUserToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_to_chat);
        receiverName = (TextView) findViewById(R.id.receiverName);
        btnSendMessage = (android.widget.ImageButton) findViewById(R.id.btnSendMessage);
        editMessage = (EditText) findViewById(R.id.input_chat);
        Intent intent = getIntent();
//        String name = intent.getStringExtra("hisName");
//        image = intent.getStringExtra("hisImage");
        id = intent.getStringExtra("hisUid");

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
                        id = document.getString("ID");
                        userName = document.getString("FullName");
                        otherUserToken = document.getString("fcmToken");
                        // You can retrieve other user data here
                        receiverName.setText(userName);
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
//                    sendMessage(message);
                    sendMessageRetrofit(message);
                    sendNotification(message);
                }
            }
        });
        callApiGetListMessage();
//        readMessage();
        seenMessage();


    }
    private void sendNotification(String message){
        CollectionReference usersRef = db.collection("User");
        Query query = usersRef.whereEqualTo("ID", currentUserId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String currentId = document.getString("ID");
                        String currentUserName = document.getString("FullName");
                        try {
                            JSONObject jsonObject = new JSONObject();

                            JSONObject notificationObj = new JSONObject();
                            notificationObj.put("title", currentUserName);
                            notificationObj.put("body", message);

                            JSONObject dataObj = new JSONObject();
                            dataObj.put("userId", currentId);

                            jsonObject.put("notification", notificationObj);
                            jsonObject.put("data", dataObj);
                            jsonObject.put("to", otherUserToken);

                            callApi(jsonObject);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAPFxchkg:APA91bFEUdq2umjc1q0KnG2sVE_PWICJbb3YR19u7F3yfgNKtz1u3zVj0-cmxSmv5FQcW2HmqqTt5Yondql3BdXZNhgxRVc1RBBneSk9YBKdRMJTMNA9Hqkbt0twKm_DOwH2lL3wvRy0")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
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
        callApiGetListMessage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

    private void callApiGetListMessage(){
        messageList = new ArrayList<>();


        // Create service
        APIService service = RetrofitClient.getClient().create(APIService.class);

        // Call API to fetch list of users
        retrofit2.Call<Map<String, Message>> call = service.getMessages();
        call.enqueue(new retrofit2.Callback<Map<String, Message>>() {
            @Override
            public void onResponse(retrofit2.Call<Map<String, Message>> call, retrofit2.Response<Map<String, Message>> response) {
                if (response.isSuccessful()) {
                    messageList.clear();
                    Map<String, Message> messageMap = response.body();
                    List<Message> messages = new ArrayList<>(messageMap.values());
                    // Handle list of users
                    for (Message message : messages) {
                        if(message.getReceiver().equals(currentUserId) && message.getSender().equals(id) ||
                                message.getReceiver().equals(id) && message.getSender().equals(currentUserId)){
                            messageList.add(message);
                        }
                        chatMessageAdapter = new ChatMessageAdapter(ChatToChatActivity.this, messageList, image);
                        chatMessageAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(chatMessageAdapter);
                    }

                } else {
                    // Handle error
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Map<String, Message>> call, Throwable t) {
                // Handle failure
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + t.getMessage());
            }
        });
    }

    private void sendMessageRetrofit(String message){
        String timeSend = String.valueOf(System.currentTimeMillis());
        Message messageObj = new Message();
        messageObj.setMessage(message);
        messageObj.setSeen(false);
        messageObj.setReceiver(id);
        messageObj.setTime(timeSend);
        messageObj.setSender(currentUserId);
        editMessage.setText("");
        // Create service
        APIService service = RetrofitClient.getClient().create(APIService.class);

        // Call API to fetch list of users
        retrofit2.Call<Void> call = service.createMessage(messageObj);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("Active: Successs");
                    callApiGetListMessage();

                }else{
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable throwable) {
                Log.e("PostFailureData", "Failure: " + throwable.getMessage());
            }
        });
    }
}