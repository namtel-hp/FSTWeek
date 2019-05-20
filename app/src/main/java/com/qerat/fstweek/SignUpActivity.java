package com.qerat.fstweek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout fullNameTextInputLayout, emailTextInputLayout, ageTextInputLayout, passwordTextInputLayout, confirmPasswordTextInputLayout, phoneNoTextInputLayout, levelTextInputLayout;
    private Spinner levelSpinner;
    private Button signUpButton, signInButton, okButton;

    private FirebaseAuth auth;
    private LinearLayout parent, loadingLayout, inputLayout, successLayout;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        fullNameTextInputLayout = findViewById(R.id.textinputlayout_fullname);
        emailTextInputLayout = findViewById(R.id.textinputlayout_email);
        ageTextInputLayout = findViewById(R.id.textinputlayout_age);
        passwordTextInputLayout = findViewById(R.id.textinputlayout_password);
        confirmPasswordTextInputLayout = findViewById(R.id.textinputlayout_confirmpassword);
        phoneNoTextInputLayout = findViewById(R.id.textinputlayout_phoneno);
        levelTextInputLayout = findViewById(R.id.textinputlayout_level);
        levelSpinner = findViewById(R.id.spinner_level);
        signInButton = findViewById(R.id.button_signin);
        signUpButton = findViewById(R.id.button_signup);
        parent = findViewById(R.id.parent);

        loadingLayout = findViewById(R.id.loadingLayout);
        inputLayout = findViewById(R.id.inputLayout);
        successLayout = findViewById(R.id.successLayout);
        okButton = findViewById(R.id.button_signupsuccess);

        auth = FirebaseAuth.getInstance();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, ConferenceInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fullNameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fullNameTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ageTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ageTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPasswordTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPasswordTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneNoTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNoTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                levelTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String fullname = fullNameTextInputLayout.getEditText().getText().toString();
                String email = emailTextInputLayout.getEditText().getText().toString().replaceAll("\\s+", "");
                String age = ageTextInputLayout.getEditText().getText().toString().replaceAll("\\s+", "");
                String phnNo = phoneNoTextInputLayout.getEditText().getText().toString().replaceAll("\\s+", "");
                String selectedLevel = levelSpinner.getSelectedItem().toString();
                String password = passwordTextInputLayout.getEditText().getText().toString();
                String confirmmPassword = confirmPasswordTextInputLayout.getEditText().getText().toString();


                if (!validateFullName(fullname)) {
                    fullNameTextInputLayout.setError("Not a valid full name!");
                    return;
                }
                if (!validateAge(age)) {
                    ageTextInputLayout.setError("Not a valid age");
                    return;
                }
                if (!validatePhone(phnNo)) {
                    phoneNoTextInputLayout.setError("Not a valid phone no");
                    return;
                }

                if (!validateLevel()) {
                    levelTextInputLayout.setError("Select a level");
                    return;
                }
                if (!validateEmail(email)) {
                    emailTextInputLayout.setError("Not a valid email");
                    return;
                }
                if (!validatePassword(password)) {
                    passwordTextInputLayout.setError("Use at least 6 characters");
                    return;
                }
                if (!validateConfirmPassword(password, confirmmPassword)) {
                    confirmPasswordTextInputLayout.setError("Password not match");
                    return;
                }

                signMeUp(fullname, age, phnNo, selectedLevel, email, password);


            }
        });

    }

    private void loadingState() {
        loadingLayout.setVisibility(View.VISIBLE);
        successLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.INVISIBLE);
    }

    private void inputState() {
        loadingLayout.setVisibility(View.GONE);
        successLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.VISIBLE);
    }

    private void successState() {
        loadingLayout.setVisibility(View.GONE);
        successLayout.setVisibility(View.VISIBLE);
        inputLayout.setVisibility(View.INVISIBLE);
    }

    private void putInformation(UserDetails user) {
        FirebaseUtilClass.getDatabaseReference().child("Users").child(auth.getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                SharedPreferences pref = getSharedPreferences(MyService.PREFERENCE_NAME, Activity.MODE_PRIVATE);
                String s = pref.getString("UserToken", "null");



                FirebaseUtilClass.getDatabaseReference().child("UserTokens").child(s).setValue(true);


                successState();
                SharedPreferences sharedPref = SignUpActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(SignInActivity.POSITION_KEY, SignInActivity.CONF_INFO);
                editor.commit();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                inputState();
            }
        });
    }

    private void signMeUp(final String fullname, final String age, final String phnNo, final String selectedLevel, final String email, final String password) {
        loadingState();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(parent, task.getException().getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    inputState();
                } else {

                    putInformation(new UserDetails(fullname, age, phnNo, selectedLevel, email));


                }


            }
        });
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validateEmail(String email) {


        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validateLevel() {

        return !(levelSpinner.getSelectedItemPosition() == 0);
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

    public boolean validateConfirmPassword(String password, String password2) {
        return password.equals(password2) && password2.length() > 5;
    }

    public boolean validateFullName(String name) {
        return name.length() > 5;
    }

    public boolean validateAge(String age) {
        try {
            int i = Integer.parseInt(age);
            if (i < 10 || i > 120) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validatePhone(String phoneNo) {
        if (phoneNo.length() < 4) {
            return false;
        }
        return true;
    }
}
