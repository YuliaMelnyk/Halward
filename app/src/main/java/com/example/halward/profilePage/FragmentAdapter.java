package com.example.halward.profilePage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * @author yuliiamelnyk on 25/05/2020
 * @project Halward
 */
public class FragmentAdapter extends FragmentStateAdapter {

    Context mContext;
    ArrayList<Fragment> mFragments;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Context context, ArrayList<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        this.mContext = context;
        this.mFragments = fragments;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
