package com.example.halward.homePage;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.halward.R;
import com.example.halward.addActivity.AddHabitActivity;
import com.example.halward.calendarPage.CalendarActivity;
import com.example.halward.login.LoginActivity;
import com.example.halward.model.Habit;
import com.example.halward.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private List<Habit> mHabits;
    private RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private View view;
    private TextView mHelloText;
    private LoginActivity mLoginActivity;
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;
    private FirebaseAuth mFirebaseAuth;
    private StorageReference mStorageRef;
    public static String userName;


    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TestFailed", "OnCreateView");


        view = inflater.inflate(R.layout.fragment_home, container, false);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        user = mFirebaseAuth.getCurrentUser();
        userName = user.getDisplayName();

        BottomNavigationView navView = view.findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            initCollapsingToolbar();
        }

        try {
            Glide.with(this).load(R.drawable.cover3).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHelloText = (TextView) view.findViewById(R.id.hello);
        mHelloText.setText("Hello " + userName+"!");


        fillHabits();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomeAdapter = new HomeAdapter(mHabits);
        mRecyclerView.setAdapter(mHomeAdapter);
        mHomeAdapter.notifyDataSetChanged();

        return view;

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    return true;
                case R.id.nav_calendar:
                    Intent myIntent = new Intent(getActivity(), CalendarActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(myIntent);
                    return true;
                case R.id.nav_create:
                    Intent intent = new Intent(getActivity(), AddHabitActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                    return true;
                case R.id.nav_progress:
                    break;
                case R.id.nav_profile:
                    break;
                default:
                    break;
            }
            return false;
        }
    };


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


private void fillHabits(){
        mHabits = new ArrayList<>();

/*        mFirestore.collection("habits").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (documentSnapshots.isEmpty()) {
                        Log.d(TAG, "onSuccess: LIST EMPTY");
                        return;
                    } else {
                        // Convert the whole Query Snapshot to a list
                        // of objects directly! No need to fetch each
                        // document.
                        List<Habit> types = documentSnapshots.toObjects(Habit.class);

                        // Add all to your list
                        mHabits.addAll(types);
                        Log.d(TAG, "onSuccess: " + mHabits);
                    }
                });

}*/
}

}
