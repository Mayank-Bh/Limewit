package com.example.bhotika.limewit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.bhotika.limewit.*;

public class Login extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    public static FirebaseAuth mAuth;
    Button signin;
    //Button logout;
    //private TextView mStatusTextView;
    //private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    // [END declare_auth]


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin = findViewById(R.id.patient_sign_in);
        // logout = v.findViewById(R.id.logout);

        //  mStatusTextView = v.findViewById(R.id.status);
        //  mDetailTextView = v.findViewById(R.id.result);
        mEmailField = findViewById(R.id.patient_signin_email);
        mPasswordField = findViewById(R.id.patient_signin_password);
        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();

//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(Login.this, MainActivity.class));
//            finish();
//        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });

    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //mStatusTextView.setText(getString(R.string.b, user.getEmail(), user.isEmailVerified()));
                            //mDetailTextView.setText(getString(R.string.a, user.getUid()));

                            startActivity(new Intent(Login.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "AuthenticationFailed", Toast.LENGTH_SHORT).show();
                            //mStatusTextView.setText("Signed out");
                            //mDetailTextView.setText(null);

                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText("Authentication Failed");
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        //mStatusTextView.setText("Signed out");
        //mDetailTextView.setText(null);
        //updateUI(null);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            //    mStatusTextView.setText("Signed out");
            //  mDetailTextView.setText(null);
        } else {
            // mStatusTextView.setText(getString(R.string.b, currentUser.getEmail(), currentUser.isEmailVerified()));
            // mDetailTextView.setText(getString(R.string.a, currentUser.getUid()));
        }
    }
}
