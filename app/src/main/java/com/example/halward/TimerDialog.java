package com.example.halward;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.halward.profilePage.Tab2Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.halward.login.LoginActivity.currentUser;
import static com.example.halward.timer.TimerHabitActivity.timeDuration;

/**
 * @author yuliiamelnyk on 29/05/2020
 * @project Halward
 */
public class TimerDialog extends DialogFragment implements SendingData {

    private static final String TAG = "TimerDialog";

    private CommonAdapter mCommonAdapter;

    // database var
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth mFirebaseAuth;

    private TimerDialog mTimerDialog;
    private SendingData mSendingData;


    public static TimerDialog getInstance() {
        TimerDialog timerDialog = new TimerDialog();
        return timerDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_timer, container, false);

        return view;
    }

   @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle(R.string.timerHabitTittle);

        //get current User
        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();

        // add a list
        String[] minutes = {"20", "30", "40", "50", "60"};

        //save time in variable and to the DataBase.
        builder.setItems(minutes, (dialog, which) -> {
            switch (which) {
                case 0:
                    timeDuration = 20 * 60;
                    currentUser.setTime(minutes[0]);
                    sendResult(minutes[0]);
                    break;
                case 1:
                    timeDuration = 30 * 60;
                    currentUser.setTime(minutes[1]);
                    sendResult(minutes[1]);
                    break;
                case 2:
                    timeDuration = 40 * 60;
                    currentUser.setTime(minutes[2]);
                    sendResult(minutes[2]);
                    break;
                case 3:
                    timeDuration = 50 * 60;
                    currentUser.setTime(minutes[3]);
                    sendResult(minutes[3]);
                    break;
                case 4:
                    timeDuration = 60 * 60;
                    currentUser.setTime(minutes[4]);
                    sendResult(minutes[4]);
                    break;
            }
        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_timer, null);

// create and show the alert dialog
        return builder.create();
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onAttach(@NonNull Context context) {
        try {
            mSendingData = (SendingData) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastExeption: " + e.getMessage());
        }
        super.onAttach(context);
    }

    @Override
    public void passData(long data) {
        data = timeDuration;
    }

    private void sendResult(String message) {
        if( getTargetFragment() == null ) {
            return;
        }
        Intent intent = Tab2Fragment.newIntent(message);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}