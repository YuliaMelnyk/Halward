package com.example.halward;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


//TODO 1) In Login Activity: implements with View.onClick... and make onClick method with switch case.
// https://github.com/andrebts/login-basics/blob/master/app/src/main/java/bscorp/appbase/LoginActivity.java
// 2) Check if all works. Add functionality - forgot password.
// 3) Create SplashActivity  https://android.jlelse.eu/right-way-to-create-splash-screen-on-android-e7f1709ba154
// https://github.com/rumaan/AcademApp/blob/master/app/src/main/java/com/rumaan/academapp/activities/SplashPageActivity.java
// 4) Look for the sessions, delete user's data on destroy method
// 5) Set default picture Add Habit.(when click button Create)


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
