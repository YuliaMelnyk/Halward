package com.example.halward.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.halward.HomeActivity;
import com.example.halward.R;
import com.example.halward.ValidateUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmail, mName, mPassword, mRePassword;
    private Button mRegisterBtn;
    FirebaseAuth mFirebaseAuth;
    View focusView = null;
    ValidateUser mValidateUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = (EditText) findViewById(R.id.reg_email);
        mName = (EditText) findViewById(R.id.reg_name);
        mPassword = (EditText) findViewById(R.id.reg_password);
        mRePassword = (EditText) findViewById(R.id.reg_repassword);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);

        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String repassword = mRePassword.getText().toString().trim();

                mValidateUser = new ValidateUser();
                // Check for a valid email address.
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError(getString(R.string.error_field_required));
                    mEmail.requestFocus();
                   return;
                } else if (!mValidateUser.isEmailValid(email)) {
                    mEmail.setError(getString(R.string.error_invalid_email));
                    mEmail.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    mPassword.setError(getString(R.string.error_field_required));
                    mPassword.requestFocus();
                    return;
                } else if (password.length() < 6) {
                    mPassword.setError(getString(R.string.error_invalid_password));
                    mPassword.requestFocus();
                    return;
                }else if (!repassword.equals(password)) {
                    mRePassword.setError(getString(R.string.error_invalid_password));
                    mRePassword.requestFocus();
                }
                //register the user in firebase

                mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


}
