package com.android.snapgrid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.snapgrid.adapters.MasonryAdapter;
import com.android.snapgrid.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home_page#} factory method to
 * create an instance of this fragment.
 */
public class fragment_home_page extends Fragment {
    TextView textView;
    ArrayList images = new ArrayList<>(Arrays.asList(R.drawable.bgrwelcome, R.drawable.test2, R.drawable.bgrwelcome,
            R.drawable.test, R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome,
            R.drawable.bgrwelcome, R.drawable.appa));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_home_page, container, false);
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        MasonryAdapter masonryAdapter = new MasonryAdapter(images, new MasonryAdapter.OnItemClickListener({
//
//        }))
        recyclerView.setAdapter(new MasonryAdapter(images));
        return rootview;
    }
}