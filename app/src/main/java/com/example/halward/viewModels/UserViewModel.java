package com.example.halward.viewModels;

import androidx.databinding.BaseObservable;

import com.google.firebase.auth.FirebaseUser;

/**
 * @author yuliiamelnyk on 03/03/2020
 * @project Halward
 */
public class UserViewModel {
    private FirebaseUser user;


    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

}
