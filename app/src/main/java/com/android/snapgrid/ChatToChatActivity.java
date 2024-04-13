package com.android.snapgrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.snapgrid.adapters.ChatMessageAdapter;
import com.android.snapgrid.adapters.ChatUserAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatToChatActivity extends AppCompatActivity {
    ArrayList chats = new ArrayList<>(Arrays.asList("Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_to_chat);
        RecyclerView recyclerView = findViewById(R.id.recycleMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ChatMessageAdapter(chats));
    }
}