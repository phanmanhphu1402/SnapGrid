package com.android.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.snapgrid.R;
import com.android.snapgrid.adapters.ChatUserAdapter;
import com.android.snapgrid.adapters.NotifyAdapter;
import com.android.snapgrid.models.Notify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifyFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseUser currentUser = mAuth.getCurrentUser();

    String currentUserId = currentUser.getUid();

    ArrayList<Notify> notifyArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_notify, container, false);

        RecyclerView recyclerView = rootview.findViewById(R.id.notifyRecycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference notifiesRef = FirebaseDatabase.getInstance().getReference("Notifies");

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("followings");
        notifiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String idPost = snapshot.child("idPost").getValue().toString();
                    String idUser = snapshot.child("idUser").getValue().toString();
                    String content = snapshot.child("content").getValue().toString();
                    String datePost = snapshot.child("date").getValue().toString();
                    Notify notify = new Notify(idUser, content, datePost, idPost);
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            for(DataSnapshot snapshot2 : dataSnapshot2.getChildren()){
                                if(snapshot2.getKey().equals(idUser)){
                                    System.out.println(snapshot2.getKey());
                                    notifyArrayList.add(notify);
                                    NotifyAdapter adapter = new NotifyAdapter(getContext(), notifyArrayList, getActivity().getSupportFragmentManager());
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return rootview;
    }
}