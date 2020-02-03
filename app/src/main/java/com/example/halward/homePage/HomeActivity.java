package com.example.halward.homePage;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.halward.R;
import com.example.halward.SinglFragmentActivity;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends SinglFragmentActivity {

    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected Fragment createFragment() {
        return new HomeFragment().newInstance();
    }
}
