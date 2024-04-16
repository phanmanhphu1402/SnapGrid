package com.android.snapgrid.adapters;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.snapgrid.R;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class FollowingUserAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mUserIds;
    public FollowingUserAdapter(Context context, ArrayList<String> userIds) {
        super(context, R.layout.item_following, userIds);
        this.mContext = context;
        this.mUserIds = userIds;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View listItemView = inflater.inflate(R.layout.item_following, parent, false);

        // Ánh xạ các view trong item_user.xml
        ShapeableImageView imageView = listItemView.findViewById(R.id.imageAVT);
        TextView textViewName = listItemView.findViewById(R.id.textName);

        // Lấy userId tại vị trí position
        String userId = mUserIds.get(position);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userImageUrl = dataSnapshot.child("profile").getValue(String.class);
                    textViewName.setText(userName);
                    Glide.with(mContext).load(userImageUrl).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("FirebaseError", "Error fetching user data: " + databaseError.getMessage());
            }
        });
        return listItemView;
    }

}
