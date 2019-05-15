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
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ConferenceInfoActivity extends AppCompatActivity {
    private Spinner tierSpinner, jobSpinner;
    private TextInputLayout tierTextInput, jobTextInput, areaTextInput;
    private CheckBox climateCheckBox, dataCheckBox, energyCheckBox,healthCheckBox, cannabisCheckBox;
    private Button okButton;
    private LinearLayout loadLinearLayout, inputLinearLayout;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_tier);

        tierSpinner=findViewById(R.id.spinner_tier);
        jobSpinner=findViewById(R.id.spinner_job);
        climateCheckBox=findViewById(R.id.checkbox_climate);
        dataCheckBox=findViewById(R.id.checkbox_data);
        energyCheckBox=findViewById(R.id.checkbox_energy);
        healthCheckBox=findViewById(R.id.checkbox_health);
        cannabisCheckBox=findViewById(R.id.checkbox_cannabis);
        okButton=findViewById(R.id.button_save);
        loadLinearLayout=findViewById(R.id.loadingLayout);
        inputLinearLayout=findViewById(R.id.inputLayout);
        tierTextInput=findViewById(R.id.textinputlayout_tier);
        jobTextInput=findViewById(R.id.textinputlayout_job);
        areaTextInput=findViewById(R.id.textinputlayout_area);

        auth=FirebaseAuth.getInstance();

        tierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tierTextInput.setErrorEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobTextInput.setErrorEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                areaTextInput.setErrorEnabled(false);
            }
        };

        climateCheckBox.setOnCheckedChangeListener(listener);
        dataCheckBox.setOnCheckedChangeListener(listener);
        energyCheckBox.setOnCheckedChangeListener(listener);
        healthCheckBox.setOnCheckedChangeListener(listener);
        cannabisCheckBox.setOnCheckedChangeListener(listener);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tierValidate()){
                    tierTextInput.setError("Select a tier");
                    return;
                }
                if(!jobValidate()){
                    jobTextInput.setError("Select a job");
                    return;
                }
                if(!areaValidate()){
                    areaTextInput.setError("Select at least one interest");
                    return;
                }

                updateData();
            }
        });
    }

    private void loadingSate(){
        loadLinearLayout.setVisibility(View.VISIBLE);
        inputLinearLayout.setVisibility(View.INVISIBLE);
    }
    private void inputState(){
        loadLinearLayout.setVisibility(View.GONE);
        inputLinearLayout.setVisibility(View.VISIBLE);
    }

    private boolean tierValidate(){
        return !(tierSpinner.getSelectedItemPosition()==0);
    }

    private void updateData(){
        loadingSate();

        String tierStr=tierSpinner.getSelectedItem().toString();
        String jobStr=jobSpinner.getSelectedItem().toString();
        ArrayList<String> arrayList=new ArrayList<>();

        if(climateCheckBox.isChecked()){
            arrayList.add(getResources().getString(R.string.conf_checkitem1));
        }
        if(dataCheckBox.isChecked()){
            arrayList.add(getResources().getString(R.string.conf_checkitem2));
        }
        if(energyCheckBox.isChecked()){
            arrayList.add(getResources().getString(R.string.conf_checkitem3));
        }
        if(healthCheckBox.isChecked()){
            arrayList.add(getResources().getString(R.string.conf_checkitem4));
        }
        if(cannabisCheckBox.isChecked()){
            arrayList.add(getResources().getString(R.string.conf_checkitem5));
        }

        FirebaseUtilClass.getDatabaseReference().child("Users").child(auth.getCurrentUser().getUid()).child("conferenceInformation").setValue(new ConferenceInformation(tierStr,jobStr,arrayList)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SharedPreferences sharedPref = ConferenceInfoActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(SignInActivity.POSITION_KEY, SignInActivity.MENT_INFO);
                editor.commit();
                Intent intent = new Intent(ConferenceInfoActivity.this, MentorshipInfoActivity.class);
                startActivity(intent);
                finish();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                inputState();
            }
        });
    }

    private boolean jobValidate(){
        return !(jobSpinner.getSelectedItemPosition()==0);
    }

    private boolean areaValidate(){
        return climateCheckBox.isChecked() || dataCheckBox.isChecked() || energyCheckBox.isChecked() || healthCheckBox.isChecked() || cannabisCheckBox.isChecked();
    }
}
