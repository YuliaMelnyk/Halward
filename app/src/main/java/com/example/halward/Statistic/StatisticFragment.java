package com.example.halward.Statistic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;


public class StatisticFragment extends Fragment {

    //private Spinner mSpinner;
    private View view;
    private Context mContext;
    private ProgressBar horizProgressBar, circleProgress;
    private TextView textCount, dailyPercent;
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
    private CollectionReference collectionReference;
    private FirebaseFirestore db;
    private String[] tags;
    private PieChart mPieChart;
    private float fwork, fhealth, fstudy, fbody, fmind, fother;


    public StatisticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_statistic, container, false);

        horizProgressBar = (ProgressBar) view.findViewById(R.id.habit_duration);
        circleProgress = (ProgressBar) view.findViewById(R.id.circle_daily);
        textCount = (TextView) view.findViewById(R.id.text_count_habit);
        dailyPercent = (TextView) view.findViewById(R.id.minutes);

        //get reference to the spinner from the XML layout
        //mSpinner = (Spinner) view.findViewById(R.id.appCompatSpinner);

        tags = getResources().getStringArray(R.array.tags);

        ArrayAdapter<String> aa = new ArrayAdapter(getApplicationContext(),
                R.layout.spinner_layout, R.id.text, tags);

        //mSpinner.setAdapter(aa);

        mPieChart = (PieChart) view.findViewById(R.id.piechart_tags);


        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("habits");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    if (habit.isActive()) {
                        mHabits.add(habit);
                        mHashMap = habit.getsHabitToday();
                    }
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateToString = format.format(date);
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(dateToString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (mHashMap.get(dateToString) == true) {
                        mHabitsDone.add(habit);
                    }
                }
                // Set value of habits in progress
                textCount.setText(mHabitsDone.size() + "/" + mHabits.size());
                double percent = (double) (mHabitsDone.size() * 100 / mHabits.size() * 100);
                int percent2 = (int) percent / 100;
                dailyPercent.setText(percent2 + "% achived for today.");
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

                fbody = ((float) mHabitsBody.size() / (float)mHabitsDone.size()) * 100;
                fhealth = ((float) mHabitsHealth.size() / (float)mHabitsDone.size()) * 100;
                fmind = ((float) mHabitsMind.size() / (float)mHabitsDone.size()) * 100;
                fother = ((float) mHabitsOther.size() / (float)mHabitsDone.size()) * 100;
                fstudy = ((float) mHabitsStudy.size() / (float)mHabitsDone.size()) * 100;
                fwork = ((float) mHabitsWork.size() / (float)mHabitsDone.size()) * 100;

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


        // Onclick the spinner value
/*        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Study")) {
                    //do nothing.
                } else {
                    // write code on what you want to do with the item selection
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


        return view;
    }
}
