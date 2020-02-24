package com.example.halward;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


//TODO 1) In Login Activity: implements with View.onClick... and make onClick method with switch case.
// https://github.com/andrebts/login-basics/blob/master/app/src/main/java/bscorp/appbase/LoginActivity.java
// 2) Check if all works. Add functionality - forgot password.
// 3) Look for the sessions, delete user's data on destroy method


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
