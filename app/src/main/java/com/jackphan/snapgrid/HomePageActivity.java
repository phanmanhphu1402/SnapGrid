package com.jackphan.snapgrid;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.jackphan.snapgrid.adapters.MasonryAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class HomePageActivity extends AppCompatActivity {

    TextView textView;
    ArrayList<Integer> images = new ArrayList<>(Arrays.asList(R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome,
            R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome,
            R.drawable.bgrwelcome, R.drawable.bgrwelcome));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView = findViewById(R.id.userName);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(new MasonryAdapter(HomePageActivity.this,images));
    }


}