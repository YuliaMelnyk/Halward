package com.example.halward.profilePage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.halward.R;
import com.example.halward.SendingData;
import com.example.halward.TimerDialog;
import com.example.halward.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.halward.timer.TimerHabitActivity.timeDuration;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2Fragment extends Fragment implements SendingData {

    private static final String TAG = "PostFragment";
    private static final int TARGET_FRAGMENT_REQUEST_CODE = 1;
    private static final String EXTRA_GREETING_MESSAGE = "message";

    //widgets
    private ImageButton mMinutesButton;
    private View view;
    private TextView timer;
    private Button logoutButton;



    @Override
    public void passData(long data) {
        Log.d(TAG, "getImagePath: setting the data time");
        timeDuration = data;
        timer.setText(Long.toString(timeDuration / 60));
        //assign to global variable
    }

    public static Tab2Fragment getInstance() {
        Tab2Fragment fragment = new Tab2Fragment();
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab2, container, false);

        // click on logoutButton
        logoutButton = (Button) view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); // logout
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
            }
        });

        timer = (TextView) view.findViewById(R.id.minutes_count);
        timer.setText(Long.toString(timeDuration / 60));
        mMinutesButton = (ImageButton) view.findViewById(R.id.downButton);
        mMinutesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerDialog timerDialog = new TimerDialog().getInstance();
                timerDialog.setTargetFragment(Tab2Fragment.this, TARGET_FRAGMENT_REQUEST_CODE);
                timerDialog.show(getFragmentManager(), "Choose the time");
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode != Activity.RESULT_OK ) {
            return;
        }
        if( requestCode == TARGET_FRAGMENT_REQUEST_CODE ) {
            String greeting = data.getStringExtra(EXTRA_GREETING_MESSAGE);
            timer.setText(greeting);
        }
    }
    public static Intent newIntent(String message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_GREETING_MESSAGE, message);
        return intent;
    }



}
