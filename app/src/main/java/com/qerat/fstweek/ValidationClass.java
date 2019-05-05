package com.qerat.fstweek;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.math.BigInteger;

public class ValidationClass {
    protected EditText nameEditText, ageEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneNoEditText;


    ValidationClass(EditText nameEditText, EditText ageEditText,  EditText phoneEditText,EditText emailEditText, EditText passwordEditText, EditText confirmPasswordEditText) {
        this.nameEditText = nameEditText;
        this.ageEditText = ageEditText;
     this.phoneNoEditText=phoneEditText;
        this.emailEditText = emailEditText;
        this.passwordEditText = passwordEditText;
        this.confirmPasswordEditText = confirmPasswordEditText;

    }

    ValidationClass(EditText emailEditText, EditText passwordEditText) {
        this.emailEditText = emailEditText;
        this.passwordEditText = passwordEditText;
    }


    public boolean nameValidate() {
        String str = nameEditText.getText().toString();
        if (TextUtils.isEmpty(str)) {
            nameEditText.setError("This field is required.");
            nameEditText.requestFocus();
            return false;
        } else if (str.length() < 8) {
            nameEditText.setError("Too short name. Use your full name.");
            nameEditText.requestFocus();
            return false;
        }
        return true;
    }

    public boolean ageValidate() {
        String str = ageEditText.getText().toString();
        if (TextUtils.isEmpty(str)) {
            ageEditText.setError("This field is required.");
            ageEditText.requestFocus();
            return false;
        } else if (str.length() != 2) {
            ageEditText.setError("Invalid age!");
            ageEditText.requestFocus();
            return false;
        }
        try {
            int ag=Integer.valueOf(str);
            ageEditText.setText(String.valueOf(ag));
            return true;
        } catch (Exception e){
            ageEditText.setError("Invalid age!");
            ageEditText.requestFocus();
            return false;
        }

    }







    public boolean validateEmail() {
        String str = emailEditText.getText().toString();
        if (TextUtils.isEmpty(str)) {
            emailEditText.setError("This field is required.");
            emailEditText.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
            emailEditText.setError("Invalid email address. Check again.");
            emailEditText.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validatePhone(){



        return true;
    }

    public boolean validatePasswordConf() {
        String str1 = passwordEditText.getText().toString();
        String str2 = confirmPasswordEditText.getText().toString();
        if (!validatePassword()) {
            return false;
        }
        if (TextUtils.isEmpty(str2)) {
            confirmPasswordEditText.setError("This field is required.");
            confirmPasswordEditText.requestFocus();
            return false;
        }


        if (!str1.equals(str2)) {
            confirmPasswordEditText.setError("Password not matches.");
            confirmPasswordEditText.requestFocus();
            return false;
        }

        return true;
    }

    public boolean validatePassword() {
        String str1 = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(str1)) {
            passwordEditText.setError("This field is required.");
            passwordEditText.requestFocus();
            return false;
        } else if (str1.length() < 6) {
            passwordEditText.setError("Too short password. Min 6 characters allowed.");
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }


}
