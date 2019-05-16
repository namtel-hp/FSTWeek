package com.qerat.fstweek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MentorshipInfoActivity extends AppCompatActivity {
    private Spinner purposeSpinner;
    private Switch mentorshipSwitch, crossSwitch;
    private RadioButton lifeCheckBox, chemCheckBox, phyCheckBox, mathCheckBox, compCheckBox;
    private TextInputLayout areaTextInputLayout, purposeTextInputLayout;
    private Button saveButton;
    private FirebaseAuth auth;
    private LinearLayout loadingLinearLayout, inputLinearLayout, mentorshipInfoLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentorship);

        purposeSpinner = findViewById(R.id.spinner_purpose);
        mentorshipSwitch = findViewById(R.id.switch_mentorship);
        crossSwitch = findViewById(R.id.switch_cross);
        lifeCheckBox = findViewById(R.id.checkbox_life);
        chemCheckBox = findViewById(R.id.checkbox_chem);
        phyCheckBox = findViewById(R.id.checkbox_phy);
        mathCheckBox = findViewById(R.id.checkbox_math);
        compCheckBox = findViewById(R.id.checkbox_comp);
        purposeTextInputLayout = findViewById(R.id.textinputlayout_purpose);
        areaTextInputLayout = findViewById(R.id.textinputlayout_area);
        saveButton = findViewById(R.id.button_save);
        loadingLinearLayout = findViewById(R.id.loadingLayout);
        inputLinearLayout = findViewById(R.id.inputLayout);
        mentorshipInfoLinearLayout = findViewById(R.id.mentorshipInfoLayout);

        auth = FirebaseAuth.getInstance();

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                areaTextInputLayout.setErrorEnabled(false);
            }
        };

        lifeCheckBox.setOnCheckedChangeListener(listener);
        chemCheckBox.setOnCheckedChangeListener(listener);
        phyCheckBox.setOnCheckedChangeListener(listener);
        mathCheckBox.setOnCheckedChangeListener(listener);
        compCheckBox.setOnCheckedChangeListener(listener);

        purposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                purposeTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mentorshipSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mentorshipInfoLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    mentorshipInfoLinearLayout.setVisibility(View.INVISIBLE);

                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!purposeValidate()) {
                    purposeTextInputLayout.setError("Select a tier");
                    return;
                }

                if (!areaValidate()) {
                    areaTextInputLayout.setError("Select at least one interest");
                    return;
                }

                updateData();
            }
        });


    }

    private void loadingSate() {
        loadingLinearLayout.setVisibility(View.VISIBLE);
        inputLinearLayout.setVisibility(View.INVISIBLE);
    }

    private void inputState() {
        loadingLinearLayout.setVisibility(View.GONE);
        inputLinearLayout.setVisibility(View.VISIBLE);
    }

    private boolean purposeValidate() {
        return !(purposeSpinner.getSelectedItemPosition() == 0) || !mentorshipSwitch.isChecked();
    }

    private boolean areaValidate() {
        return (lifeCheckBox.isChecked() || chemCheckBox.isChecked() || phyCheckBox.isChecked() || mathCheckBox.isChecked() || compCheckBox.isChecked()) || !mentorshipSwitch.isChecked();
    }

    private void updateData() {
        loadingSate();

        final String purposeStr = purposeSpinner.getSelectedItem().toString();
        final Boolean mentorshipBool = mentorshipSwitch.isChecked();
        Boolean crossBool = crossSwitch.isChecked();
        String areaStr = "";

        if (lifeCheckBox.isChecked()) {
            areaStr = getResources().getString(R.string.ment_checkitem1);
        }
        if (chemCheckBox.isChecked()) {
            areaStr = getResources().getString(R.string.ment_checkitem2);
        }
        if (phyCheckBox.isChecked()) {
            areaStr = getResources().getString(R.string.ment_checkitem3);
        }
        if (mathCheckBox.isChecked()) {
            areaStr = getResources().getString(R.string.ment_checkitem4);
        }
        if (compCheckBox.isChecked()) {
            areaStr = getResources().getString(R.string.ment_checkitem5);
        }
        final String areaFinal = areaStr;

        FirebaseUtilClass.getDatabaseReference().child("Users").child(auth.getCurrentUser().getUid()).child("mentorshipInformation").setValue(new MentorshipInformation(mentorshipBool, purposeStr, crossBool, areaFinal)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if (mentorshipBool) {
                    FirebaseUtilClass.getDatabaseReference().child("MentorshipGroups").child(purposeStr + "_" + areaFinal).child("members").push().setValue(auth.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            SharedPreferences sharedPref = MentorshipInfoActivity.this.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(SignInActivity.POSITION_KEY, SignInActivity.MAIN_ACTV);
                            editor.commit();
                            Intent intent = new Intent(MentorshipInfoActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            inputState();
                        }
                    });
                } else {
                    SharedPreferences sharedPref = MentorshipInfoActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(SignInActivity.POSITION_KEY, SignInActivity.MAIN_ACTV);
                    editor.commit();
                    Intent intent = new Intent(MentorshipInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                inputState();
            }
        });
    }
}
