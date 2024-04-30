package com.android.snapgrid.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.R;
import com.android.snapgrid.adapters.PostAdapter;
import com.android.snapgrid.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Khởi tạo danh sách bài viết và adapter
        List<Post> postList = new ArrayList<>();
        adapter = new PostAdapter(view.getContext(), postList);
        recyclerView.setAdapter(adapter);

        // Tiến hành truy vấn dữ liệu từ Firebase và cập nhật adapter
        performSearch(""); // Gọi hàm performSearch hoặc tương tự để lấy dữ liệu từ Firebase

    }

    // Các phương thức khác như performSearch...
    // Định nghĩa performSearch ở đây, ngoài phương thức onViewCreated
    private void performSearch(String query) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Posts");
        Query searchQuery = databaseRef.orderByChild("title").startAt(query).endAt(query + "\uf8ff");

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> searchResults = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    searchResults.add(post);
                }
                adapter.updateData(searchResults);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("SearchFragment", "performSearch:onCancelled", databaseError.toException());
            }
        });
    }

}
