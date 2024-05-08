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
    private PostAdapter adapter;
    private EditText editTextSearch;
    private ImageButton imageButtonSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Liên kết RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Khởi tạo danh sách bài viết và adapter
        List<Post> postList = new ArrayList<>();
        adapter = new PostAdapter(view.getContext(), postList);
        recyclerView.setAdapter(adapter);

        // Liên kết nút tìm kiếm và EditText
        editTextSearch = view.findViewById(R.id.editTextSearch);
        imageButtonSearch = view.findViewById(R.id.imageButtonSearch);

        // Thiết lập sự kiện tìm kiếm khi nhấn nút
        imageButtonSearch.setOnClickListener(v -> {
            String query = editTextSearch.getText().toString().trim();
            Log.d("SearchFragment", "Nút tìm kiếm được bấm với từ khóa: " + query);
            performSearch(query); // Gọi hàm tìm kiếm với từ khóa đã nhập
        });

        // Bạn có thể thực hiện truy vấn ban đầu với tất cả dữ liệu
        performSearch(""); // Truy vấn với chuỗi rỗng để hiển thị tất cả bài viết
    }

    // Phương thức để tìm kiếm bài viết dựa trên tiêu đề
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

                // Ghi nhật ký số lượng bài viết tìm thấy
                Log.d("SearchFragment", "Số lượng bài viết tìm thấy: " + searchResults.size());

                adapter.updateData(searchResults);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("SearchFragment", "performSearch:onCancelled", databaseError.toException());
            }
        });
    }



}
