package com.example.halward.calendarPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.halward.R;
import com.example.halward.homePage.HomeActivity;
import com.example.halward.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CalendarActivity extends AppCompatActivity {

    public static final String EXTRA_CALENDAR_ID = "com.example.halward.calendar_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

    }
    public static Intent newIntent(Context packageContext, String email){
        Intent intent = new Intent(packageContext, CalendarActivity.class);
        intent.putExtra(EXTRA_CALENDAR_ID, email);
        return intent;
    }


    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut(); // logout
        Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
