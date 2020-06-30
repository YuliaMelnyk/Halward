package com.example.halward.Statistic;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.halward.ConvertDataTypes;
import com.example.halward.R;
import com.example.halward.model.Habit;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;


public class StatisticFragment extends Fragment {

    //widgets
    private View view;
    private PieChart mPieChart;
    private ProgressBar horizProgressBar, circleProgress, monday_pr, tuesday_pr, wednesday_pr, thursday_pr, friday_pr, saturday_pr, sunday_pr;
    private TextView textCount, dailyPercent, mondayText, tuesdayText, wednesdayText, thursdayText, fridayText, saturdayText, sundayText, weeklyPercent;

    private Context mContext;
    private ArrayList<Habit> mHabits = new ArrayList<>();
    private ArrayList<Habit> mHabitsDone = new ArrayList<>();
    private ArrayList<Habit> mHabitsWork = new ArrayList<>();
    private ArrayList<Habit> mHabitsStudy = new ArrayList<>();
    private ArrayList<Habit> mHabitsHealth = new ArrayList<>();
    private ArrayList<Habit> mHabitsBody = new ArrayList<>();
    private ArrayList<Habit> mHabitsMind = new ArrayList<>();
    private ArrayList<Habit> mHabitsOther = new ArrayList<>();
    private ArrayList<Habit> mHabitTMP = new ArrayList<>();
    private HashMap<String, Boolean> mHashMap = new HashMap<>();
    private ArrayList<String> mMondayList = new ArrayList<>();
    private ArrayList<String> mTuesdayList = new ArrayList<>();
    private ArrayList<String> mWednesdayList = new ArrayList<>();
    private ArrayList<String> mThursdayList = new ArrayList<>();
    private ArrayList<String> mFridayList = new ArrayList<>();
    private ArrayList<String> mSaturdayList = new ArrayList<>();
    private ArrayList<String> mSundayList = new ArrayList<>();
    private ArrayList<String> mMondayDone = new ArrayList<>();
    private ArrayList<String> mTuesdayDone = new ArrayList<>();
    private ArrayList<String> mWednesdayDone = new ArrayList<>();
    private ArrayList<String> mThursdayDone = new ArrayList<>();
    private ArrayList<String> mFridayDone = new ArrayList<>();
    private ArrayList<String> mSaturdayDone = new ArrayList<>();
    private ArrayList<String> mSundayDone = new ArrayList<>();


    //for Date
    private ArrayList<String> weekdays = new ArrayList<>();
    private Date date;
    private String dateToString;
    private SimpleDateFormat format;

    //DataBase
    private CollectionReference collectionReference;
    private FirebaseFirestore db;

    private float fwork, fhealth, fstudy, fbody, fmind, fother;

    public StatisticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_statistic, container, false);

        // widgets Progress Bar
        horizProgressBar = (ProgressBar) view.findViewById(R.id.habit_duration);
        circleProgress = (ProgressBar) view.findViewById(R.id.circle_daily);
        monday_pr = (ProgressBar) view.findViewById(R.id.monday_progress);
        tuesday_pr = (ProgressBar) view.findViewById(R.id.tuesday_progress);
        wednesday_pr = (ProgressBar) view.findViewById(R.id.wednesday_progress);
        thursday_pr = (ProgressBar) view.findViewById(R.id.thursday_progress);
        friday_pr = (ProgressBar) view.findViewById(R.id.friday_progress);
        saturday_pr = (ProgressBar) view.findViewById(R.id.saturday_progress);
        sunday_pr = (ProgressBar) view.findViewById(R.id.sunday_progress);

        mondayText = (TextView) view.findViewById(R.id.monday_habits);
        tuesdayText = (TextView) view.findViewById(R.id.tuesday_habits);
        wednesdayText = (TextView) view.findViewById(R.id.wednesday_habits);
        thursdayText = (TextView) view.findViewById(R.id.thursday_habits);
        fridayText = (TextView) view.findViewById(R.id.friday_habits);
        saturdayText = (TextView) view.findViewById(R.id.saturday_habits);
        sundayText = (TextView) view.findViewById(R.id.sunday_habits);

        textCount = (TextView) view.findViewById(R.id.text_count_habit);
        weeklyPercent = (TextView) view.findViewById(R.id.percentWeekly);
        dailyPercent = (TextView) view.findViewById(R.id.minutes);

        mPieChart = (PieChart) view.findViewById(R.id.piechart_tags);

        // Format actual date
        date = new Date();
        format = new SimpleDateFormat("yyyy-MM-dd");
        dateToString = format.format(date);
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateToString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int dayOfWeek = getDayNumberOld(date);
        try {
            getWeeksDaysArray(dayOfWeek);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("habits");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    Habit habit = document.toObject(Habit.class);
                    mHashMap = habit.getsHabitToday();

                    if (habit.isActive() && mHashMap.containsKey(weekdays.get(0))) {
                        mMondayList.add(weekdays.get(0));
                        if (mHashMap.get(weekdays.get(0).toString())) {
                            mMondayDone.add(weekdays.get(0));
                        }
                    }
                    if (habit.isActive() && habit.getsHabitToday().containsKey(weekdays.get(1))) {
                        mTuesdayList.add(weekdays.get(1));
                        if (mHashMap.get(weekdays.get(1).toString())) {
                            mTuesdayDone.add(weekdays.get(1));
                        }
                    }
                    if (habit.isActive() && habit.getsHabitToday().containsKey(weekdays.get(2))) {
                        mWednesdayList.add(weekdays.get(2));
                        if (mHashMap.get(weekdays.get(2).toString())) {
                            mWednesdayDone.add(weekdays.get(2));
                        }
                    }
                    if (habit.isActive() && habit.getsHabitToday().containsKey(weekdays.get(3))) {
                        mThursdayList.add(weekdays.get(3));
                        if (mHashMap.get(weekdays.get(3).toString())) {
                            mThursdayDone.add(weekdays.get(3));
                        }
                    }
                    if (habit.isActive() && habit.getsHabitToday().containsKey(weekdays.get(4))) {
                        mFridayList.add(weekdays.get(4));
                        if (mHashMap.get(weekdays.get(4).toString())) {
                            mFridayDone.add(weekdays.get(4));
                        }
                    }
                    if (habit.isActive() && habit.getsHabitToday().containsKey(weekdays.get(5))) {
                        mSaturdayList.add(weekdays.get(5));
                        if (mHashMap.get(weekdays.get(5).toString())) {
                            mSaturdayDone.add(weekdays.get(5));
                        }
                    }
                    if (habit.isActive() && habit.getsHabitToday().containsKey(weekdays.get(6))) {
                        mSundayList.add(weekdays.get(6));
                        if (mHashMap.get(weekdays.get(6).toString())) {
                            mSundayDone.add(weekdays.get(6));
                        }
                    }

                    if (habit.isActive() && habit.getsHabitToday().containsKey(dateToString)) {
                        mHabits.add(habit);
                    }
                    if (mHashMap.containsKey(dateToString) && mHashMap.get(dateToString)) {
                        mHabitsDone.add(habit);
                    }
                }

                mondayText.setText(String.valueOf(mMondayDone.size()));
                tuesdayText.setText(String.valueOf(mTuesdayDone.size()));
                wednesdayText.setText(String.valueOf(mWednesdayDone.size()));
                thursdayText.setText(String.valueOf(mThursdayDone.size()));
                fridayText.setText(String.valueOf(mFridayDone.size()));
                saturdayText.setText(String.valueOf(mSaturdayDone.size()));
                sundayText.setText(String.valueOf(mSundayDone.size()));

                monday_pr.setMax(mMondayList.size());
                monday_pr.setProgress(mMondayDone.size());
                tuesday_pr.setMax(mTuesdayList.size());
                tuesday_pr.setProgress(mTuesdayDone.size());
                wednesday_pr.setMax(mWednesdayList.size());
                wednesday_pr.setProgress(mWednesdayDone.size());
                thursday_pr.setMax(mThursdayList.size());
                thursday_pr.setProgress(mThursdayDone.size());
                friday_pr.setMax(mFridayList.size());
                friday_pr.setProgress(mFridayDone.size());
                saturday_pr.setMax(mSaturdayList.size());
                saturday_pr.setProgress(mSaturdayDone.size());
                sunday_pr.setMax(mSundayList.size());
                sunday_pr.setProgress(mSundayDone.size());

                double percentWeek = (double) (mMondayDone.size() + mTuesdayDone.size() + mWednesdayDone.size() + mThursdayDone.size() + mFridayDone.size() + mSaturdayDone.size() + mSundayDone.size()) /
                        (mMondayList.size() + mTuesdayDone.size() + mWednesdayList.size() + mThursdayList.size() + mFridayList.size() + mSaturdayList.size() + mSundayList.size());
                weeklyPercent.setText(round(percentWeek*100, 2) + "% achieved this week");
                // Set value of habits in progress
                textCount.setText(mHabitsDone.size() + "/" + mHabits.size());
                double percent = (double) (mHabitsDone.size() * 100 / mHabits.size() * 100);
                int percent2 = (int) percent / 100;
                dailyPercent.setText(percent2 + "% achieved for today.");
                horizProgressBar.setMax(mHabits.size());
                horizProgressBar.setProgress(mHabitsDone.size());
                circleProgress.setProgress(mHabitsDone.size());
                circleProgress.setMax(mHabits.size());

                //put values into tagsMap for PieChart
                for (int i = 0; i < mHabitsDone.size(); i++) {
                    if (mHabitsDone.get(i).getTag().equals("Mind")) {
                        mHabitsMind.add(mHabitsDone.get(i));
                    }
                    if (mHabitsDone.get(i).getTag().equals("Study")) {
                        mHabitsStudy.add(mHabitsDone.get(i));
                    }
                    if (mHabitsDone.get(i).getTag().equals("Work")) {
                        mHabitsWork.add(mHabitsDone.get(i));
                    }
                    if (mHabitsDone.get(i).getTag().equals("Health")) {
                        mHabitsHealth.add(mHabitsDone.get(i));
                    }
                    if (mHabitsDone.get(i).getTag().equals("Other")) {
                        mHabitsOther.add(mHabitsDone.get(i));
                    }
                    if (mHabitsDone.get(i).getTag().equals("Body")) {
                        mHabitsBody.add(mHabitsDone.get(i));
                    }
                }

                fbody = ((float) mHabitsBody.size() / (float) mHabitsDone.size()) * 100;
                fhealth = ((float) mHabitsHealth.size() / (float) mHabitsDone.size()) * 100;
                fmind = ((float) mHabitsMind.size() / (float) mHabitsDone.size()) * 100;
                fother = ((float) mHabitsOther.size() / (float) mHabitsDone.size()) * 100;
                fstudy = ((float) mHabitsStudy.size() / (float) mHabitsDone.size()) * 100;
                fwork = ((float) mHabitsWork.size() / (float) mHabitsDone.size()) * 100;

                mPieChart.setUsePercentValues(true);
                ArrayList<PieEntry> xvalues = new ArrayList<>();

                xvalues.add(new PieEntry(fbody, "Body"));
                xvalues.add(new PieEntry(fhealth, "Health"));
                xvalues.add(new PieEntry(fmind, "Mind"));
                xvalues.add(new PieEntry(fother, "Other"));
                xvalues.add(new PieEntry(fstudy, "Study"));
                xvalues.add(new PieEntry(fwork, "Work"));

                PieDataSet dataSet = new PieDataSet(xvalues, "");
                dataSet.setSliceSpace(2f);
                dataSet.setValueTextColor(Color.WHITE);
                dataSet.setValueTextSize(12f);
                //dataSet.setDrawValues(false);
                mPieChart.setDrawEntryLabels(false);
                // add a lot of colors
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(getResources().getColor(R.color.tag2));
                colors.add(getResources().getColor(R.color.tag1));
                colors.add(getResources().getColor(R.color.tag3));
                colors.add(getResources().getColor(R.color.tag4));
                colors.add(getResources().getColor(R.color.tag5));
                colors.add(getResources().getColor(R.color.tag6));

                colors.add(ColorTemplate.getHoloBlue());

                dataSet.setColors(colors);

                Legend legend = mPieChart.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                legend.setDrawInside(false);

                mPieChart.getDescription().setEnabled(false);

                PieData pieData = new PieData(dataSet);
                mPieChart.setData(pieData);
            }
        });
        return view;
    }

    // Get number of Week Day for week statistics
    public static int getDayNumberOld(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    //round double percent week to 2 decimal
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    //set days in ArrayList depend of the actual day of the week
    public void getWeeksDaysArray(int dayOfWeek) throws ParseException {

        String monday;
        String tuesday;
        String wednesday;
        String thursday;
        String friday;
        String saturday;
        String sunday;

        String dateString;

        if (dayOfWeek == 1) {
            monday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(6, TimeUnit.DAYS)));
            weekdays.add(monday);
            tuesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(5, TimeUnit.DAYS)));
            weekdays.add(tuesday);
            wednesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS)));
            weekdays.add(wednesday);
            thursday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)));
            weekdays.add(thursday);
            friday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(friday);
            saturday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(saturday);
            sunday = format.format(new Date());
            weekdays.add(sunday);
        }

        if (dayOfWeek == 2) {
            monday = format.format(new Date());
            weekdays.add(monday);
            tuesday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(tuesday);
            wednesday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(wednesday);
            thursday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)));
            weekdays.add(thursday);
            friday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS)));
            weekdays.add(friday);
            saturday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(5, TimeUnit.DAYS)));
            weekdays.add(saturday);
            sunday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(6, TimeUnit.DAYS)));
            weekdays.add(sunday);
        }

        if (dayOfWeek == 3) {
            monday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(monday);
            tuesday = format.format(new Date());
            weekdays.add(tuesday);
            wednesday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(wednesday);
            thursday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(thursday);
            friday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)));
            weekdays.add(friday);
            saturday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS)));
            weekdays.add(saturday);
            sunday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(5, TimeUnit.DAYS)));
            weekdays.add(sunday);
        }

        if (dayOfWeek == 4) {
            monday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(monday);
            tuesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(tuesday);
            wednesday = format.format(new Date());
            weekdays.add(wednesday);
            thursday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(thursday);
            friday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(friday);
            saturday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)));
            weekdays.add(saturday);
            sunday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS)));
            weekdays.add(sunday);
        }
        if (dayOfWeek == 5) {
            monday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)));
            weekdays.add(monday);
            tuesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(tuesday);
            wednesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(wednesday);
            thursday = format.format(new Date());
            weekdays.add(thursday);
            friday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(friday);
            saturday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(saturday);
            sunday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)));
            weekdays.add(sunday);
        }

        if (dayOfWeek == 6) {
            monday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS)));
            weekdays.add(monday);
            tuesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)));
            weekdays.add(tuesday);
            wednesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(wednesday);
            thursday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(thursday);
            friday = format.format(new Date());
            weekdays.add(friday);
            saturday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(saturday);
            sunday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(sunday);
        }

        if (dayOfWeek == 7) {
            monday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(5, TimeUnit.DAYS)));
            weekdays.add(monday);
            tuesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS)));
            weekdays.add(tuesday);
            wednesday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)));
            weekdays.add(wednesday);
            thursday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS)));
            weekdays.add(thursday);
            friday = format.format(new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(friday);
            saturday = format.format(new Date());
            weekdays.add(saturday);
            sunday = format.format(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
            weekdays.add(sunday);
        }
    }
}
