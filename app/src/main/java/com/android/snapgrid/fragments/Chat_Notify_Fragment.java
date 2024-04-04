package com.android.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.snapgrid.NotifyFragment;
import com.android.snapgrid.R;
import com.android.snapgrid.databinding.ActivityMainBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chat_Notify_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chat_Notify_Fragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_chat__notify_, container, false);
        NotifyFragment childFragment = new NotifyFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, childFragment);
        transaction.commit();
        Button btnNotify = (Button)rootview.findViewById(R.id.btnNotiffy);
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyFragment childFragment = new NotifyFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, childFragment);
                transaction.commit();
            }
        });
        Button btnMessage = (Button)rootview.findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatUsersFragment childFragment = new ChatUsersFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, childFragment);
                transaction.commit();
            }
        });
        return rootview;
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = new FragmentManager() {
            @Override
            public void addFragmentOnAttachListener(@NonNull FragmentOnAttachListener listener) {
                super.addFragmentOnAttachListener(listener);
            }
        };
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}

