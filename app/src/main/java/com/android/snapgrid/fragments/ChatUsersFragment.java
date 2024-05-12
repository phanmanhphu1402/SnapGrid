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
import com.android.snapgrid.models.ChatResponse;
import com.android.snapgrid.models.Comments;
import com.android.snapgrid.models.Message;
import com.android.snapgrid.models.Post;
import com.android.snapgrid.models.User;
import com.android.snapgrid.service.APIService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                    RecyclerView recyclerView = rootview.findViewById(R.id.recycleviewChatUser);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(new ChatUserAdapter(ChatUsersFragment.this, userList));
                } else {
                    Log.e("Error", "Error getting documents: ", task.getException());
                }
            }
        });
        System.out.println("Active: Call");
//        callApiGetListUser();

        return  rootview;
    }
}