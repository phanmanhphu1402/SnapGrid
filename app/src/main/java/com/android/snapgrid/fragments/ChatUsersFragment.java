package com.android.snapgrid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.snapgrid.MainActivity;
import com.android.snapgrid.R;
import com.android.snapgrid.adapters.ChatUserAdapter;
import com.android.snapgrid.adapters.MasonryAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatUsersFragment extends Fragment {

    ArrayList images = new ArrayList<>(Arrays.asList(R.drawable.bgrwelcome, R.drawable.test2, R.drawable.test, R.drawable.appa, R.drawable.bgrwelcome, R.drawable.test2, R.drawable.test, R.drawable.appa));
    ArrayList names = new ArrayList<>(Arrays.asList("Thi Vo", "Manh Phu", "Buu Trung", "Quang truong", "Xuan Truong", "Manh Phu", "Buu Trung", "Quang truong"));
    ArrayList chats = new ArrayList<>(Arrays.asList("Mai di hoc", "Day choi game", "???:D", "Let's him cook", "Mai di hoc", "Day choi game", "???:D", "Let's him cook"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_chat_users, container, false);
        RecyclerView recyclerView = rootview.findViewById(R.id.recycleviewChatUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ChatUserAdapter(this, images, names, chats));
        return  rootview;
    }
}