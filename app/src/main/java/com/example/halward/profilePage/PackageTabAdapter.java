package com.example.halward.profilePage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * @author yuliiamelnyk on 27/05/2020
 * @project Halward
 */
public class PackageTabAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private ArrayList<Fragment> mFragments;

    public PackageTabAdapter(@NonNull FragmentManager fm, Context context, ArrayList<Fragment> fragments) {
        super(fm);
        this.mContext = context;
        this.mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new Tab1Fragment();
        }
        else if (position == 1)
        {
            fragment = new Tab2Fragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "All habits";
        }
        else if (position == 1)
        {
            title = "Settings";
        }
        return title;
    }
}
