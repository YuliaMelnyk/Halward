package com.example.halward;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.halward.model.Habit;
import com.example.halward.profilePage.ProfileActivity;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author yuliiamelnyk on 25/03/2020
 * @project Halward
 */
public class HabitDialog extends AppCompatDialogFragment implements ConvertDataTypes {

    private EditText mEditTextDate;
    private EditText mEditTextDuration;
    private CommonAdapter mCommonAdapter;
    private CollectionReference collectionReference;
    private FirebaseFirestore db;
    private ArrayList<Habit> mHabits;
    private int position;
    private int duration;
    private Habit mHabit;
    private Habit item;
    private FirebaseUser user;
    private FirebaseAuth mFirebaseAuth;
    public static String userName;
    private HashMap<String, Boolean> mHashMap = new HashMap<>();
    private Map<String, Object> mMap = new HashMap<>();

    public HabitDialog(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_habit, null);

        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        userName = user.getDisplayName();

        // Get Habits from FireBase
        collectionReference = db.collection("habits");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mHabits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    mHabits.add(habit);
                }
            }
        });


        mEditTextDuration = (EditText) view.findViewById(R.id.durationET);

        // On click Terminate button
        builder.setView(view)
                .setTitle(R.string.dialog_title)
                .setNegativeButton(R.string.terminate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        clickedTerminate(position);
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        //intent.putExtra("position", position);
                        startActivity(intent);
                        dismiss();
                    }
                })
                //On click Activate button
                .setPositiveButton(R.string.activate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mEditTextDuration.getText().equals("") || mEditTextDuration.getText().equals(null)) {
                            duration = 0;
                        } else {
                            duration = (Integer.parseInt(mEditTextDuration.getText().toString()));
                        }
                        clickedActivate(position, duration);

                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        startActivity(intent);
                    }
                });


        return builder.create();
    }

    // Change data in Data Base if clicked Terminate
    private void clickedTerminate(int position) {

        item = mHabits.get(position);
        // take habit from Firebase

        // Save in FireBAse active -> false and endDate -> yesterday
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mHabits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    if (habit.getDocumentName().equals(item.getDocumentName())) {
                        habit.setActive(false);
                        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String dateToString = format.format(date);
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateToString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        habit.setEndDate(date);
                        document.getReference().update("active", false);
                        document.getReference().update("endDate", date);
                    }
                }
            }
        });
    }

    // Change data in Data Base if clicked Activate
    private void clickedActivate(int position, final int duration) {

        item = mHabits.get(position);
        // take habit from Firebase

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mHabits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    if (habit.getDocumentName().equals(item.getDocumentName())) {
                        habit.setActive(true);
                        habit.setDone(false);
                        habit.setStartDate(new Date());
                        habit.setDuration(duration);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        // Getting current date
                        Calendar cal = Calendar.getInstance();
                        // Number of Days to add
                        cal.add(Calendar.DAY_OF_MONTH, duration);
                        //Date after adding the days to the current date
                        String endDate = sdf.format(cal.getTime());
                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        habit.setEndDate(date1);


                        Date date = habit.getStartDate();

                        //formatting Local Date
                        LocalDate localDate = convertToLocalDateViaMilisecond(date);

                        // formatting Date
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        String startDay = format.format(new Date());
                        Date start = new Date();
                        try {
                            start = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Add in HashMap new dates and done false
                        try {
                            for (Date d = format.parse(startDay) ; d.before(habit.getEndDate()); localDate.plusDays(1)) {
                                d = convertToDateViaInstant(localDate);
                                String dateToString = format.format(d);
                                localDate = localDate.plusDays(1);
                                mHashMap.put(dateToString, false);
                                mMap.put(dateToString, false);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        habit.setsHabitToday(mHashMap);
                        // Saved all data in document Firebase
                        document.getReference().update("active", true);
                        document.getReference().update("duration", duration);
                        document.getReference().update("startDate", start);
                        document.getReference().update("endDate", date1);
                        document.getReference().update("sHabitToday", mMap);
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
