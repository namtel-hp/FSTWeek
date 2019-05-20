package com.qerat.fstweek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth auth;
    public static final int CONF_INFO=1,MENT_INFO=2,MAIN_ACTV=3, SIGN_IN=4;
    public static final String POSITION_KEY="pos";
    LinearLayout initRelativeLayout,secondLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_in);
        auth = FirebaseAuth.getInstance();





        initRelativeLayout = findViewById(R.id.initialLayout);
        secondLinearLayout= findViewById(R.id.loadingSignIn);


        Button signUpBtn = findViewById(R.id.goto_sign_up_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        final EditText emailEditText = findViewById(R.id.email_textview), passwordEditText = findViewById(R.id.password_textview);


        Button btn = findViewById(R.id.sign_in_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                ValidationClass validationClass = new ValidationClass(emailEditText, passwordEditText);
                if (validationClass.validateEmail() && validationClass.validatePassword()) {
                    secondState();
                    String email = emailEditText.getText().toString().replaceAll("\\s+",""), password = passwordEditText.getText().toString();
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Snackbar snackbar = Snackbar.make(initRelativeLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                initialState();
                            } else {
                                SharedPreferences pref = getSharedPreferences(MyService.PREFERENCE_NAME, Activity.MODE_PRIVATE);
                                String s = pref.getString("UserToken", "null");


                                FirebaseUtilClass.getDatabaseReference().child("UserTokens").child(s).setValue(true);

                                FirebaseUtilClass.getDatabaseReference().child("Users").child(auth.getCurrentUser().getUid()).child("conferenceInformation").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            Intent intent = new Intent(SignInActivity.this, ConferenceInfoActivity.class);
                                            startActivity(intent);

                                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putInt(POSITION_KEY, CONF_INFO);
                                            editor.apply();
                                            SignInActivity.this.finish();

                                        } else {
                                            FirebaseUtilClass.getDatabaseReference().child("Users").child(auth.getCurrentUser().getUid()).child("mentorshipInformation").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    if (!snapshot.exists()) {
                                                        Intent intent = new Intent(SignInActivity.this, MentorshipInfoActivity.class);
                                                        startActivity(intent);
                                                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                        editor.putInt(POSITION_KEY, MENT_INFO);
                                                        editor.commit();
                                                        SignInActivity.this.finish();
                                                    } else {
                                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                        editor.putInt(POSITION_KEY, MAIN_ACTV);
                                                        editor.commit();
                                                        SignInActivity.this.finish();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    initialState();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        initialState();
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onStart(){
        super.onStart();
        if (auth.getCurrentUser() != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


            int pur = sharedPref.getInt(POSITION_KEY, MAIN_ACTV);
            if(pur==MAIN_ACTV) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else if(pur==CONF_INFO){
                Intent intent = new Intent(this, ConferenceInfoActivity.class);
                startActivity(intent);
            }else if(pur==MENT_INFO){
                Intent intent = new Intent(this, ConferenceInfoActivity.class);
                startActivity(intent);
            }
            this.finish();
        }
    }


    private void initialState() {
        initRelativeLayout.setVisibility(View.VISIBLE);
        secondLinearLayout.setVisibility(View.GONE);
    }

    private void secondState() {
        initRelativeLayout.setVisibility(View.INVISIBLE);
        secondLinearLayout.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
