package com.example.halward;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.halward.addActivity.AddHabitActivity;
import com.example.halward.addActivity.AddHabitFragment;
import com.example.halward.calendarPage.CalendarActivity;
import com.example.halward.calendarPage.CalendarFragment;
import com.example.halward.homePage.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yuliiamelnyk on 21/01/2020
 * @project Halward
 */
public class ProgressActivity extends AppCompatActivity {

    Fragment currentFragment = null;
    FragmentTransaction ft;
    List<Fragment> mFragments;
    private int lastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        initData();
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new ProgressFragment();
        ft.replace(R.id.content, currentFragment);
        ft.commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void initData() {

        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new CalendarFragment());
        mFragments.add(new AddHabitFragment());
        mFragments.add(new ProgressFragment());
        mFragments.add(new ProgileFragment());

        setFragmentPosition(0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    setFragmentPosition(0);
                case R.id.nav_calendar:
                    setFragmentPosition(1);
                case R.id.nav_create:
                    setFragmentPosition(2);
                case R.id.nav_progress:
                    setFragmentPosition(3);
                case R.id.nav_profile:
                    setFragmentPosition(4);
                default:
                    break;
            }
            return false;
        }
    };


    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.content, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }
}
