package com.example.halward.calendarPage;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.halward.R;
import com.example.halward.model.Habit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<Habit> mHabits;
    private List<Date> habitsToday;
    private RecyclerView mRecyclerView;
    private CalendarAdapter mCalendarAdapter;
    CalendarView mCalendarView;
    View view;
    String selectedDate;
    public static String userName;
    private Date date;


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseUser user;
    private FirebaseAuth mFirebaseAuth;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        userName = user.getDisplayName();

        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        selectedDate = sdf.format(new Date(mCalendarView.getDate()));

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month,
                                            int dayOfMonth) {

                final Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);

                // Get habits from FireBase and set depend of date.
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("habits");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mHabits = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Habit habit = document.toObject(Habit.class);
                            if (cal.getTime().compareTo(habit.getStartDate()) == 0
                                    || cal.getTime().compareTo(habit.getEndDate()) == 0
                                    || (cal.getTime().after(habit.getStartDate()) && cal.getTime().before(habit.getEndDate()))) {
                                mHabits.add(habit);
                            }
                        }
                        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_calendar);
                        // mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mCalendarAdapter = new CalendarAdapter(getContext(),mHabits,  R.layout.fragment_habit_today);
                        mRecyclerView.setAdapter(mCalendarAdapter);
                        mCalendarAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(false);
    }


}
