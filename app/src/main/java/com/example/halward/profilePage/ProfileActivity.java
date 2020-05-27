package com.example.halward.profilePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.halward.HabitDialog;
import com.example.halward.InitCollapsing;
import com.example.halward.R;
import com.example.halward.databinding.ActivityProfileBinding;
import com.example.halward.CommonAdapter;
import com.example.halward.homePage.HomeActivity;
import com.example.halward.model.Habit;
import com.example.halward.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.halward.login.LoginActivity.currentUser;

/**
 * @author yuliiamelnyk on 2020-02-20
 * @project Halward
 */
public class ProfileActivity extends AppCompatActivity implements CommonAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener, InitCollapsing {

    private MyClickHandlers handlers;

    private TextView mLogout;
    private ImageButton mImageButton;
    private ImageView mImageView;

    private Menu collapsedMenu;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private CollectionReference collectionReference;
    private FirebaseFirestore db;
    public static String userName;
    private ArrayList<Habit> mHabits;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private ProfileAdapter mProfileAdapter;
    public static TabLayout mTabLayout;
    public static ViewPager viewPager;
    private PackageTabAdapter adapter;
    public ArrayList<Fragment> mFragments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userName = mFirebaseUser.getDisplayName();

        // Binding information about user
        currentUser = new User();
        currentUser.setName(userName);
        currentUser.setName(mFirebaseUser.getDisplayName());
        currentUser.setPhoto(mFirebaseUser.getPhotoUrl().toString());

        binding.setUser(currentUser);

        handlers = new ProfileActivity.MyClickHandlers(mContext);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
            setSupportActionBar(toolbar);

            viewPager = (ViewPager) findViewById(R.id.profile_viewPager);
            viewPager.setOffscreenPageLimit(2);

            // Attach to the TabLayout title of tabs
            mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
            mTabLayout.addTab(mTabLayout.newTab().setText(R.string.habits_tab));
            mTabLayout.addTab(mTabLayout.newTab().setText(R.string.settings_tab));

            mFragments = new ArrayList<>();
            mFragments.add(new Tab1Fragment());
            mFragments.add(new Tab2Fragment());

            createTabFragment();

            initCollapsingToolbar();
            try {
                Glide.with(this).load(R.drawable.caver_21).into((ImageView) findViewById(R.id.backdrop_1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //set toolbar
            toolbar.inflateMenu(R.menu.menu_profile);
            Menu menu = toolbar.getMenu();
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back24));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });
        }



      /*  db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("habits");

        // Get All habits from FireBase
        final ProfileActivity self = this;
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mHabits = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Habit habit = document.toObject(Habit.class);
                    mHabits.add(habit);
                }
                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_profile);
                //mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                mProfileAdapter = new ProfileAdapter(mContext, mHabits, R.layout.cardview_habit_profile);
                mRecyclerView.setAdapter(mProfileAdapter);
                mProfileAdapter.setClickListener(self);
                mProfileAdapter.notifyDataSetChanged();

            }
        });*/
/*
        mImageButton = (ImageButton) view.findViewById(R.id.back_home);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
            }
        });
*/

/*        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); // logout
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
            }
        });*/

    }
    private void createTabFragment(){
        adapter = new PackageTabAdapter(getSupportFragmentManager(), getApplicationContext(), mFragments);
        viewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewPager);

        mTabLayout.getTabAt(0).setText(R.string.habits_tab);
        mTabLayout.getTabAt(1).setText(R.string.settings_tab);
    }

    @Override
    public void initCollapsingToolbar() {
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_profile_toolbar);
        collapsingToolbarLayout.setTitle("");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_profile_bar);
        appBarLayout.setExpanded(true);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // On click -> open Habit Dialog window
    @Override
    public void onItemClick(View view, int position) {
        HabitDialog habitDialog = new HabitDialog(position);
        habitDialog.show(getSupportFragmentManager(), "Activate a habit");
    }


    public class MyClickHandlers {

        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        /**
         * Demonstrating updating bind data
         * profile image
         * will be updated on Fab click
         */
        public void onProfileFabClicked(View view) {
            currentUser.setName("Sir David Attenborough");
            //currentUser.setPhoto("https://api.androidhive.info/images/nature/david1.jpg");

        }

        public boolean onProfileImageLongPressed(View view) {
            Toast.makeText(getApplicationContext(), "Profile image long pressed!", Toast.LENGTH_LONG).show();
            return false;
        }


    }


}
