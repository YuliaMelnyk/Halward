package com.example.halward.calendarPage;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.CalendarView;

import com.example.halward.R;


public class CalendarActivity extends AppCompatActivity {


    public static final String EXTRA_CALENDAR_ID = "com.example.halward.calendar_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


    }
/*    public static Intent newIntent(Context packageContext, String email){
        Intent intent = new Intent(packageContext, CalendarActivity.class);
        intent.putExtra(EXTRA_CALENDAR_ID, email);
        return intent;
    }*/

}
