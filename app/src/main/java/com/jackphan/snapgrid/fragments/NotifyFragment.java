package com.jackphan.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.adapters.NotifyAdapter;
import com.jackphan.snapgrid.models.Notify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifyFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_notify, container, false);
        RecyclerView recyclerView = rootview.findViewById(R.id.notifyRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Kiểm tra user đã đăng nhập chưa
        if (currentUser == null) {
            Log.e("NotifyFragment", "User not logged in!");
            return rootview;
        }

        ArrayList<Notify> notifyArrayList = new ArrayList<>();
        NotifyAdapter adapter = new NotifyAdapter(getContext(), notifyArrayList, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(adapter);

        String currentUserId = currentUser.getUid();
        DatabaseReference notifiesRef = FirebaseDatabase.getInstance().getReference("Notifies");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("followings");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot followingsSnapshot) {
                HashSet<String> followingIds = new HashSet<>();
                for (DataSnapshot snapshot2 : followingsSnapshot.getChildren()) {
                    followingIds.add(snapshot2.getKey());
                }

                notifiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notifyArrayList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String idPost = snapshot.child("idPost").getValue(String.class);
                            String idUser = snapshot.child("idUser").getValue(String.class);
                            String content = snapshot.child("content").getValue(String.class);
                            String datePost = snapshot.child("date").getValue(String.class);

                            if (idUser != null && followingIds.contains(idUser)) {
                                notifyArrayList.add(new Notify(idUser, content, datePost, idPost));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("NotifyFragment", "Load notifies cancelled", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotifyFragment", "Load notifies cancelled", error.toException());
            }
        });

        return rootview;
    }
}