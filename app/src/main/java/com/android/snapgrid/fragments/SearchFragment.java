package com.android.snapgrid.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

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
    private EditText editTextSearch;
    private PostAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        List<Post> postList = new ArrayList<>();
        adapter = new PostAdapter(view.getContext(), postList, post -> {
            // Chuyển sang DetailPostFragment
            Bundle bundle = new Bundle();
            bundle.putString("dataImage", post.getImageUrl());
            bundle.putString("dataTitle", post.getTitle());
            bundle.putString("dataContent", post.getContent());
            bundle.putString("dataIdPost", post.getIdPost());
            bundle.putString("dataIdUser", post.getIdUser());

            DetailPostFragment detailPostFragment = new DetailPostFragment();
            detailPostFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, detailPostFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        ImageButton imageButtonSearch = view.findViewById(R.id.imageButtonSearch);

        imageButtonSearch.setOnClickListener(v -> {
            String query = editTextSearch.getText().toString().trim();
            performSearch(query); // Thực hiện tìm kiếm dựa trên tiêu đề
        });

        performSearch(""); // Truy vấn với chuỗi rỗng để hiển thị tất cả bài viết
    }

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

                adapter.updateData(searchResults); // Cập nhật danh sách trong adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("SearchFragment", "performSearch:onCancelled", databaseError.toException());
            }
        });
    }
}

