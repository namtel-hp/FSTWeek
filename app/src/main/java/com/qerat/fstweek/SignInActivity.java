package com.qerat.fstweek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
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
    RelativeLayout  secondRelativeLayout;
    LinearLayout initRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_in);
        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null) {
            checkEmailVerified();
        }


        initRelativeLayout = findViewById(R.id.initialLayout);
        secondRelativeLayout = findViewById(R.id.loadingSignIn);





        Button signUpBtn = findViewById(R.id.goto_sign_up_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        final EditText emailEditText = findViewById(R.id.email_textview), passwordEditText = findViewById(R.id.password_textview);


        Button btn = findViewById(R.id.sign_in_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidationClass validationClass = new ValidationClass(emailEditText, passwordEditText);
                if (validationClass.validateEmail() && validationClass.validatePassword()) {
                    secondState();
                    String email = emailEditText.getText().toString(), password = passwordEditText.getText().toString();
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Snackbar snackbar = Snackbar.make(initRelativeLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                initialState();
                            } else {
                                checkEmailVerified();
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {

            final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseUtilClass.getDatabaseReference().child("TempUsers").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Map<String, String> map = (HashMap) dataSnapshot.getValue();
                    if (map != null) {
                        final String batchName = map.get("Batch"),
                                rollName = map.get("Roll"),
                                deptName = map.get("Department");

                        FirebaseUtilClass.getDatabaseReference().child("TempUsers").child(UID).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUtilClass.getDatabaseReference().child("Users").child(UID).setValue(map);
                                    FirebaseUtilClass.getDatabaseReference().child("UsersTree").child(batchName).child(deptName).child(rollName).setValue(UID);

                                    finish();
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {

                                }
                            }
                        });
                    } else {
                        finish();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.

            startActivity(new Intent(this, NotVerifiedEmailActivity.class));
            initialState();
            //restart this activity

        }
    }

    private void initialState() {
        initRelativeLayout.setVisibility(View.VISIBLE);
        secondRelativeLayout.setVisibility(View.GONE);
    }

    private void secondState() {
        initRelativeLayout.setVisibility(View.GONE);
        secondRelativeLayout.setVisibility(View.VISIBLE);
    }
}
