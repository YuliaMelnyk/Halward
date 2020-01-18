package com.example.halward.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halward.HomeActivity;
import com.example.halward.R;
import com.example.halward.ValidateUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ImageView mFacebook, mGoogle;
    private EditText mEmail, mPassword;
    private Button mLoginBtn;
    private TextView mSignUp, mResetPassword;

    View focusView = null;
    private ValidateUser mValidateUser;

    FirebaseAuth mFirebaseAuth;

    // Sing In with Goodle+

    //    Request code used to invoke sign in user interactions for Google+
    public static final int RC_GOOGLE_LOGIN = 1;

    //Client used to interact with Google APIs.
    private GoogleApiClient mGoogleApiClient;

    //    A flag indicating that a PendingIntent is in progress and prevents us from starting further intents.
    private boolean mGoogleIntentInProgress;

    //Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in without waiting.
    private boolean mGoogleLoginClicked;


    //    The login button for Google
    private ImageView mGoogleLogin;
//
//    static String personName, gmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFacebook = (ImageView) findViewById(R.id.login_face);
        mGoogle = (ImageView) findViewById(R.id.login_google);
        mEmail = (EditText) findViewById(R.id.log_email);
        mPassword = (EditText) findViewById(R.id.log_password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mSignUp = (TextView) findViewById(R.id.signup_label);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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
                } else if (!mValidateUser.isPasswordValid(password)) {
                    mPassword.setError(getString(R.string.error_invalid_password));
                    mPassword.requestFocus();
                    return;
                }

                // authenticate the user

                mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged is successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword(mEmail.getText().toString().trim());
            }
        });
    }

    private void resetPassword(String emailAddress) {
        mFirebaseAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email sent.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
