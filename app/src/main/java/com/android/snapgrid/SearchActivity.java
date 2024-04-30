package com.android.snapgrid;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.snapgrid.adapters.PostAdapter; // Make sure this import is correct
import com.android.snapgrid.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private RecyclerView recyclerViewSearchResults;
    private ArrayList<Post> searchResults;
    private PostAdapter adapter; // Sử dụng PostsAdapter thay vì YourAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);
        ImageButton imageButtonSearch = findViewById(R.id.imageButtonSearch);

        // Khởi tạo danh sách tìm kiếm và adapter
        searchResults = new ArrayList<>();
        adapter = new PostAdapter(this, searchResults);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setAdapter(adapter);

        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch(editTextSearch.getText().toString().trim());
            }
        });
    }

    private void performSearch(String searchText) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        searchResults.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            searchResults.add(post);
                        }
                        adapter.notifyDataSetChanged(); // Thông báo cho adapter dữ liệu đã thay đổi
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi ở đây
                    }
                });
    }
}
