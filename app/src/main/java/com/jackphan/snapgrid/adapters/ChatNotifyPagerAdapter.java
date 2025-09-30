package com.jackphan.snapgrid.adapters;

import android.support.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jackphan.snapgrid.fragments.ChatUsersFragment;
import com.jackphan.snapgrid.fragments.NotifyFragment;

public class ChatNotifyPagerAdapter extends FragmentStateAdapter {

    public ChatNotifyPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new NotifyFragment();
        } else {
            return new ChatUsersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // 2 tab
    }
}
