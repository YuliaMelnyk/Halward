package com.example.halward.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.halward.R;

public class RegisterActivity extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mImageView = (ImageView) findViewById(R.id.facebook);

    }


}
