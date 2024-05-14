package com.android.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.snapgrid.R;
import com.android.snapgrid.adapters.ChatUserAdapter;
import com.android.snapgrid.models.Message;
import com.android.snapgrid.models.User;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatUsersFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    DatabaseReference UserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_chat_users, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> userList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String name = snapshot.child("name").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    String imageUrl = snapshot.child("profile").getValue(String.class);
                    User user = new User();
                    user.setName(name);
                    user.setId(id);
                    user.setAvatar(imageUrl);
                    if(user.getId().equals(currentUserId) == false){
                        userList.add(user);
                    }
                }
                RecyclerView recyclerView = rootview.findViewById(R.id.recycleviewChatUser);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new ChatUserAdapter(ChatUsersFragment.this, userList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        callApiGetListUser();

        return  rootview;
    }

    private void callApiGetListUser(){


        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firestore.googleapis.com/v1/projects/snapgrid-44c83/databases/(default)/documents/") // or Firestore URL
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);

        service.getUsers().enqueue(new Callback<Map<String, User>>() {
            @Override
            public void onResponse(Call<Map<String, User>> call, Response<Map<String, User>> response) {
                if (response.isSuccessful()) {
                    Map<String, User> userMap = response.body();
                    List<User> userList = new ArrayList<>(userMap.values());
                    for (User user : userList){
                        Log.e("PostData", "Success: " + user.getName());
                    }
                    // Process user list

                } else {
                    // Handle error
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Map<String, User>> call, Throwable t) {
                // Handle failure
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + t.getMessage());
            }
        });
    }
}