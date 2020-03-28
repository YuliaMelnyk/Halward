package com.example.halward.profilePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.halward.InitCollapsing;
import com.example.halward.R;
import com.example.halward.databinding.FragmentProfileBinding;
import com.example.halward.homePage.HomeActivity;
import com.example.halward.login.LoginActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.halward.login.LoginActivity.*;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * @author yuliiamelnyk on 2020-02-10
 * @project Halward
 */
public class ProfileFragment extends Fragment {

    private MyClickHandlers handlers;
    private View view;
    private TextView mLogout;
    private ImageButton mImageButton;
    private ImageView mImageView;

    private Menu collapsedMenu;

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentProfileBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        binding.setUser(currentUser);
        view = binding.getRoot();

        handlers = new MyClickHandlers(getContext());

        mLogout = (TextView) view.findViewById(R.id.logout);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_profile);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_profile_toolbar);
            collapsingToolbarLayout.setTitle("");
            //collapsingToolbarLayout.setExpandedTitleMarginStart(250);
            AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.app_profile_bar);
            appBarLayout.setExpanded(true);

            toolbar.inflateMenu(R.menu.menu_profile);
            Menu menu = toolbar.getMenu();
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back24));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                }
            });
        }


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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
        }
    }


    // udapting the Toolbar Menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        collapsedMenu = menu;
    }


    public class MyClickHandlers {

        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        /**
         * Demonstrating updating bind data
         * Profile name, number of posts and profile image
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


        public void onFollowersClicked(View view) {
            Toast.makeText(context, "Followers is clicked!", Toast.LENGTH_SHORT).show();
        }

        public void onFollowingClicked(View view) {
            Toast.makeText(context, "Following is clicked!", Toast.LENGTH_SHORT).show();
        }

        public void onPostsClicked(View view) {
            Toast.makeText(context, "Posts is clicked!", Toast.LENGTH_SHORT).show();
        }
    }

}