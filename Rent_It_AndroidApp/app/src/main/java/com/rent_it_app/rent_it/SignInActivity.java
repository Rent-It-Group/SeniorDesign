package com.rent_it_app.rent_it;

/**
 * Created by Mizz on 1/10/17.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rent_it_app.rent_it.firebase.DeleteTokenService;
import com.rent_it_app.rent_it.json_models.ChatUser;
import com.rent_it_app.rent_it.views.ChatListFragment;

import java.io.IOException;
import java.util.ArrayList;

public class SignInActivity extends BaseActivity {

    private static final String TAG = "SignIn";

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mResetButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mResetButton = (Button) findViewById(R.id.btn_forgot_password);




        mAuth = FirebaseAuth.getInstance();
        //if user haven't varified her email address, send her another email and sign her out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    //originally here
                    // Only send if email is not confirmed.
                    if (!user.isEmailVerified()) {
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignInActivity.this, "Please varify your email address", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "Email sent");
                                        }
                                    }
                                });
                        mAuth.signOut();
                        user = null;
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                hideProgressDialog();
                if (user != null) {
                    //if logged in
                    /*ArrayList<String> defaultRoom = new ArrayList<String>();
                    defaultRoom.add("home");*/
                    /*ChatListFragment.user = new ChatUser(user.getUid(), user.getDisplayName(),
                            user.getEmail(),true,defaultRoom);*/

                    Intent deleteTokenIntent = new Intent(SignInActivity.this, DeleteTokenService.class);
                    //msgIntent.putExtra(DeleteTokenService.PARAM_IN_MSG, strInputMsg);
                    startService(deleteTokenIntent);

                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


        //showProgressDialog();
        showProgressDialogForSignIn();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            Log.d(TAG, "signInWithEmail:failed:exception:", task.getException());
                            try{
                                Log.d(TAG, "signInWithEmail:failed:exception:getCause:", e.getCause());
                            }
                            catch(Exception e1){
                                Log.d(TAG, "signInWithEmail:failed:exception:getCause:failed", e1);
                            }
                            Log.d(TAG, "signInWithEmail:failed:exception:getMessage:" + e.getMessage());
                            Toast.makeText(SignInActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else{

                        }


                        hideProgressDialog();

                    }
                });

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } /*else if(!isEmailValid(email)){
            mEmailField.setError("Enter Valid Email Address");
            valid = false;
        }*/ else {
            mEmailField.setError(null);
        }


        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }


    /*private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            //if logged in
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }*/


    public void logIn(View view){
        signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

    public void goToRestPasswordActivity(View view) {
        Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void goToSignUpActivity(View view) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

}