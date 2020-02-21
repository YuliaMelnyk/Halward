package com.example.halward;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.halward.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

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

    }


}
