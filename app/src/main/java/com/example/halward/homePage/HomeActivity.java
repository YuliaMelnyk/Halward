package com.example.halward.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.halward.Statistic.StatisticFragment;
import com.example.halward.profilePage.ProfileActivity;
import com.example.halward.R;
import com.example.halward.addActivity.AddHabitActivity;
import com.example.halward.addActivity.AddHabitFragment;
import com.example.halward.calendarPage.CalendarFragment;
import com.example.halward.profilePage.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;

    Fragment currentFragment = null;
    FragmentTransaction ft;
    List<Fragment> mFragments;
    private int lastIndex;
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initData();

        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new HomeFragment();
        ft.replace(R.id.content, currentFragment);
        ft.commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }
    public void initData() {

        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new CalendarFragment());
        mFragments.add(new AddHabitFragment());
        mFragments.add(new StatisticFragment());
        mFragments.add(new ProfileFragment());

        setFragmentPosition(0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    setFragmentPosition(0);
                    return true;
                case R.id.nav_calendar:
                    setFragmentPosition(1);
                    return true;
                case R.id.nav_create:
                    Intent myIntent = new Intent(HomeActivity.this, AddHabitActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent);
                    return true;
                case R.id.nav_progress:
                    setFragmentPosition(3);
                    return true;
                case R.id.nav_profile:
                    Intent myIntent2 = new Intent(HomeActivity.this, ProfileActivity.class);
                    //myIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent2);
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