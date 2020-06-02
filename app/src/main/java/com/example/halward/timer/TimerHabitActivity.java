package com.example.halward.timer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.halward.CommonAdapter;
import com.example.halward.model.HabitsDone;
import com.example.halward.R;
import com.example.halward.homePage.HomeActivity;
import com.example.halward.model.Habit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TimerHabitActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar mProgressBar, mProgressBar1;

    private ImageButton buttonStartTime, buttonStopTime, mResetButton;
    private Button mDoneButton;
    private TextView textViewShowTime;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;
    public static long timeDuration;
    private long seconds;
    private int position;
    private int time;

    private CollectionReference collectionReference;
    private FirebaseFirestore db;
    private ArrayList<Habit> mHabits;
    private Habit mHabit;
    private FirebaseUser user;
    private FirebaseAuth mFirebaseAuth;
    public static String userName;
    private CommonAdapter mCommonAdapter;
    private HabitsDone mHabitsDone;
    private HashMap<String, Boolean> mHashMap = new HashMap<>();
    private Map<String, Object> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_habit);
        if (savedInstanceState != null) {
            savedInstanceState.getLong("seconds");
        }
        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        userName = user.getDisplayName();

        collectionReference = db.collection("habits");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mHabits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    mHashMap = habit.getsHabitToday();
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateToString = format.format(date);
                    try {
                        date =  new SimpleDateFormat("yyyy-MM-dd").parse(dateToString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (habit.isActive() && mHashMap.containsKey(dateToString) &&!mHashMap.get(dateToString)) {
                        mHabits.add(habit);
                    }
                }
            }
        });

        // Get position of habit from HomeFragment
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);

        buttonStartTime = (ImageButton) findViewById(R.id.button_timerview_start);
        buttonStopTime = (ImageButton) findViewById(R.id.button_timerview_stop);
        mResetButton = (ImageButton) findViewById(R.id.button_timerview_reset);
        mDoneButton = (Button) findViewById(R.id.done_button);

        textViewShowTime = (TextView)
                findViewById(R.id.textView_timerview_time);

        buttonStartTime.setOnClickListener(this);
        buttonStopTime.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mDoneButton.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_timerview);
        mProgressBar1 = (ProgressBar) findViewById(R.id.progressbar1_timerview);
        if (timeDuration == 0){
            timeDuration = 1800;
        }
        seconds = timeDuration;

    }

    // methods on click for start, stop and reset buttons
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_timerview_start) {

            mProgressBar1.setMax((int) seconds * 1000);
            mProgressBar.setVisibility(View.INVISIBLE);
            startTimer();
            mProgressBar1.setVisibility(View.VISIBLE);

        } else if (v.getId() == R.id.button_timerview_stop) {
            stopTimer();

        } else if (v.getId() == R.id.button_timerview_reset) {
            timerReset();
            mProgressBar1.setMax((int) seconds * 1000);
            //mProgressBar1.setVisibility(View.INVISIBLE);

        } else if (v.getId() == R.id.done_button) {
            ifDone();
        }
    }

    // when the phone turn to landscape view, the timer cant be stopped.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("seconds", seconds);
    }

    //Set for every habit 30 minutes
    private void setTimer() {
        time = (int) seconds;
        totalTimeCountInMilliseconds = time * 1000;
        mProgressBar1.setMax(time * 1000);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(seconds * 1000, 1) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                seconds = leftTimeInMilliseconds / 1000;
                mProgressBar1.setProgress((int) (leftTimeInMilliseconds));
                textViewShowTime.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
            }

            //Set timer 00:00 when it finished
            @Override
            public void onFinish() {
                Toast.makeText(TimerHabitActivity.this, "Well done!", Toast.LENGTH_SHORT).show();
                textViewShowTime.setText("00:00");
                textViewShowTime.setVisibility(View.VISIBLE);
                buttonStartTime.setVisibility(View.VISIBLE);
                buttonStopTime.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar1.setVisibility(View.GONE);
            }
        }.start();
    }

    // Pause the timer
    public void stopTimer() {
        countDownTimer.cancel();
        mProgressBar1.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        //buttonStartTime.setVisibility(View.VISIBLE);
        //buttonStopTime.setVisibility(View.INVISIBLE);
    }

    //Reset the Timer
    private void timerReset() {
        Log.i("Sec", Long.toString(time));
        countDownTimer.cancel();
        textViewShowTime.setText("30:00");
        seconds = timeDuration;
    }

    // When user clicked Done Button in Timer -> Change Habit property Done -> true and save in Firebase.
    private void ifDone() {
        mHabit = mHabits.get(position);
        mHabitsDone = new HabitsDone();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mHabits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    if (habit.getDocumentName().equals(mHabit.getDocumentName())) {
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String dateToString = format.format(date);

                        //save true value in HashMap for exact day Key
                        data.put(dateToString, true);
                        mHashMap = habit.getsHabitToday();
                        mHashMap.computeIfPresent(dateToString, (k, v) -> true);
                        for (Map.Entry entry : mHashMap.entrySet()) {
                            data.put(entry.getKey().toString(), entry.getValue());
                        }
                        document.getReference().update("done", true);
                        document.getReference().update("sHabitToday", data);
                    }
                }
            }
        });

        //Return to the Home Page
        Intent intent = new Intent(TimerHabitActivity.this, HomeActivity.class);
        startActivity(intent);
    }

}

