package com.example.halward.homePage;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.halward.CommonAdapter;
import com.example.halward.InitCollapsing;
import com.example.halward.R;
import com.example.halward.SwipeToDeleteCallback;
import com.example.halward.login.LoginActivity;
import com.example.halward.model.Habit;
import com.example.halward.model.User;
import com.example.halward.timer.TimerHabitActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
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

import static android.content.ContentValues.TAG;
import static com.example.halward.login.LoginActivity.currentUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, InitCollapsing, CommonAdapter.ItemClickListener {

    private ArrayList<Habit> mHabits;
    private RecyclerView mRecyclerView;
    private CommonAdapter mCommonAdapter;
    private View view;
    private TextView mHelloText;
    private Habit item;
    private CoordinatorLayout mCoordinatorLayout;
    private CollectionReference collectionReference;
    private FirebaseFirestore db;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FirebaseUser user;
    private FirebaseAuth mFirebaseAuth;
    public static String userName;
    public static User currentUser;
    private Context mContext;
    private Spinner mSpinner;
    private Resources res;
    private String[] tags;
    private HashMap<String, Boolean> mHashMap = new HashMap<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TestFailed", "OnCreateView");

        view = inflater.inflate(R.layout.fragment_home, container, false);

        currentUser = new User();
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        userName = user.getDisplayName();

        if (user.getPhotoUrl() == null) {
            currentUser.setPhoto("/Users/serhiimelnyk/material-components-android-codelabs/java/Halward/app/src/main/res/drawable/images.png");
        } else {
            currentUser.setPhoto(user.getPhotoUrl().toString());
        }
        currentUser.setName(user.getDisplayName());


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            initCollapsingToolbar();
        }

        try {
            Glide.with(this).load(R.drawable.caver_11).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHelloText = (TextView) view.findViewById(R.id.hello);
        mHelloText.setText("Hello " + userName + "!");

        res = getResources();
        tags = res.getStringArray(R.array.tags);


        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("habits");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mHabits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    mHashMap = habit.getsHabitToday();
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateToString = format.format(date);
                    if (habit.isActive() && mHashMap.containsKey(dateToString) && !mHashMap.get(dateToString)) {
                        mHabits.add(habit);
                    }
                }

                mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                mCoordinatorLayout = view.findViewById(R.id.home_habit);
                // mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mCommonAdapter = new CommonAdapter(getContext(), mHabits);
                mRecyclerView.setAdapter(mCommonAdapter);
                mCommonAdapter.notifyDataSetChanged();

                enableSwipeToDeleteAndUndo();
            }
        });

        return view;
    }

    //Delete habit with Swipe Left

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                item = mCommonAdapter.getData().get(position);
                mCommonAdapter.removeItem(position);

                // delete habit from Firebase
                CollectionReference habits = db.collection("habits");

                habits.document(item.getDocumentName()).delete();
                mHabits.remove(item);

                Snackbar snackbar = Snackbar
                        .make(mCoordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mCommonAdapter.restoreItem(item, position);
                        mRecyclerView.scrollToPosition(position);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);
    }

    // takes habits from Firebase and fill them in the Home PAge

    public void fillHabits() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("habits");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mHabits = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Habit habit = document.toObject(Habit.class);
                        mHabits.add(habit);
                    }
                    //mHomeAdapter.notifyDataSetChanged();
                } else {
                    Log.w(TAG, "Failed to read value.");
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        fillHabits();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), TimerHabitActivity.class);
        intent.putExtra("position", position);
        getActivity().startActivity(intent);
    }
}