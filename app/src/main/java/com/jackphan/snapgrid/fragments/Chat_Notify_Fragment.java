package com.jackphan.snapgrid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackphan.snapgrid.R;
import com.jackphan.snapgrid.adapters.ChatNotifyPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

        TabLayout tabLayout = rootview.findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = rootview.findViewById(R.id.viewPager);

        ChatNotifyPagerAdapter adapter = new ChatNotifyPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("Thông Báo");
            } else {
                tab.setText("Nhắn Tin");
            }
        }).attach();
        return rootview;
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = new FragmentManager() {
            @Override
            public void addFragmentOnAttachListener(@NonNull FragmentOnAttachListener listener) {
                super.addFragmentOnAttachListener(listener);
            }
        };
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

