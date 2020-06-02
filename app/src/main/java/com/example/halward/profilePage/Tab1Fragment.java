package com.example.halward.profilePage;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.halward.CommonAdapter;
import com.example.halward.HabitDialog;
import com.example.halward.InitCollapsing;
import com.example.halward.R;
import com.example.halward.model.Habit;
import com.example.halward.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.halward.login.LoginActivity.currentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1Fragment extends Fragment implements ProfileAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private PackageTabAdapter mPackageTabAdapter;
    private ArrayList<Habit> mHabits;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    public static String userName;
    private CollectionReference collectionReference;
    private FirebaseFirestore db;
    private View view;
    private ProfileAdapter mProfileAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public Tab1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_tab1, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userName = mFirebaseUser.getDisplayName();

        // Binding information about user
        currentUser = new User();
        currentUser.setName(userName);
        currentUser.setName(mFirebaseUser.getDisplayName());
        if (mFirebaseUser.getPhotoUrl() == null){
            currentUser.setPhoto("https://firebasestorage.googleapis.com/v0/b/halward-2932c.appspot.com/o/''.jpg?alt=media&token=b5961abc-1f54-43b6-b6d4-4c12c00cabc6");
        } else {
            currentUser.setPhoto(mFirebaseUser.getPhotoUrl().toString());
        }

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("habits");

        // Get All habits from FireBase
        final Tab1Fragment self = this;
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mHabits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    mHabits.add(habit);
                }
                mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_profile);
                //mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                mProfileAdapter = new ProfileAdapter(mContext, mHabits, R.layout.cardview_habit_profile);
                mRecyclerView.setAdapter(mProfileAdapter);
                mProfileAdapter.setClickListener(self);
                mProfileAdapter.notifyDataSetChanged();

            }
        });

        return view;
    }
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // On click -> open Habit Dialog window
    @Override
    public void onItemClick(View view, int position) {
        HabitDialog habitDialog = new HabitDialog(position);
        habitDialog.show(getActivity().getSupportFragmentManager(), "Activate a habit");
    }

}
