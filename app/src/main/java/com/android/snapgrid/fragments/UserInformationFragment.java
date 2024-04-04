package com.android.snapgrid.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.snapgrid.MainActivity;
import com.android.snapgrid.R;
import com.android.snapgrid.UserConfigActivity;
import com.android.snapgrid.adapters.MasonryAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInformationFragment extends Fragment {
    TextView textView;
    ArrayList images = new ArrayList<>(Arrays.asList(R.drawable.bgrwelcome, R.drawable.test2, R.drawable.bgrwelcome,
            R.drawable.test, R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome,
            R.drawable.bgrwelcome, R.drawable.appa,R.drawable.bgrwelcome, R.drawable.test2, R.drawable.bgrwelcome,
            R.drawable.test, R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome, R.drawable.bgrwelcome,
            R.drawable.bgrwelcome, R.drawable.appa));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_user_information, container, false);
        Button btnConfigInfor = (Button)rootview.findViewById(R.id.btnConfigInfor);
        btnConfigInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserConfigActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(new MasonryAdapter(images));
        return rootview;

    }
}