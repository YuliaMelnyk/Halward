package com.example.halward.homePage;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.halward.R;
import com.example.halward.SinglFragmentActivity;

public class HomeActivity extends SinglFragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        }


    @Override
    protected Fragment createFragment() {
        return new HomeFragment().newInstance();
    }

/*    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut(); // logout
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }*/



}
