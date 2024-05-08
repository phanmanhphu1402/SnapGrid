package com.android.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.snapgrid.MainActivity;
import com.android.snapgrid.R;
import com.android.snapgrid.adapters.ChatUserAdapter;
import com.android.snapgrid.adapters.MasonryAdapter;
import com.android.snapgrid.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatUsersFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_chat_users, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference usersRef = db.collection("User");
        String currentUserId = currentUser.getUid();
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> userList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        // Convert each document to a User object
                        String name = (String) document.get("FullName");
                        String id = (String) document.get("ID");
                        User user = new User();
                        user.setName(name);
                        user.setId(id);
                        if(user.getId().equals(currentUserId) == false){
                            userList.add(user);
                        }

                    }
                    // Now you have a list of User objects
                    // Do whatever you want with the userList
                    RecyclerView recyclerView = rootview.findViewById(R.id.recycleviewChatUser);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(new ChatUserAdapter(ChatUsersFragment.this, userList));
                } else {
                    Log.e("Error", "Error getting documents: ", task.getException());
                }
            }
        });


        return  rootview;
    }
}