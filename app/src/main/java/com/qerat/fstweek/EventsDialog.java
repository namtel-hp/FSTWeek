package com.qerat.fstweek;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


public class EventsDialog extends Dialog {
    private EventClass item;
    private Button participateButton, unParticipateButton;
    private Drawable drawable;
    private ImageView speakerImageView, closeImageView;
    private TextView titleTextView, speakerNameTextView, timeTextView, locationTextView, dateTextView;

    public EventsDialog(Context context, EventClass item, Drawable drawable) {
        super(context);
        this.item = item;
        this.drawable = drawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialogue_events);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        titleTextView = findViewById(R.id.titleTextView);
        speakerNameTextView = findViewById(R.id.speakerNameTextView);
        timeTextView = findViewById(R.id.timeTextView);
        locationTextView = findViewById(R.id.locationTextView);
        speakerImageView = findViewById(R.id.speakerImageView);
        dateTextView = findViewById(R.id.dateTextView);
        closeImageView = findViewById(R.id.closeImageView);
        participateButton = findViewById(R.id.participateButton);
        unParticipateButton = findViewById(R.id.unParticipateButton);
        titleTextView.setText(item.getEventTitle());
        speakerNameTextView.setText(item.getSpeakerName());
        timeTextView.setText(item.getEventTime());
        locationTextView.setText(item.getEventLocation());
        dateTextView.setText(item.getEventDate());
        speakerImageView.setImageDrawable(drawable);


        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeDataToFirebase();
            }
        });

        unParticipateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDataFromFirebase();
            }
        });

        if (item.getParticipantsMap() != null) {


            if (item.getParticipantsMap().get(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                unParticipateUI();
            } else {
                participateUI();
            }
        }else {
            participateUI();
        }

    }

    private void participateUI(){

        participateButton.setText("Participate");

        participateButton.setEnabled(true);
        unParticipateButton.setVisibility(View.GONE);
        participateButton.setVisibility(View.VISIBLE);
    }

    private void unParticipateUI(){
        unParticipateButton.setText("Unparticipate");
        unParticipateButton.setEnabled(true);
        participateButton.setVisibility(View.GONE);
        unParticipateButton.setVisibility(View.VISIBLE);

    }

    private void deleteDataFromFirebase() {
        unParticipateButton.setEnabled(false);
        unParticipateButton.setText("Please wait..");

        FirebaseUtilClass.getDatabaseReference().child("Talks").child(item.getDay()).child(item.getPushId()).child("participantsMap").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                participateUI();


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "Failed to unparticipate, Try again", Toast.LENGTH_SHORT).show();
                unParticipateUI();
            }
        });
    }

    public void writeDataToFirebase() {
        participateButton.setEnabled(false);
        participateButton.setText("Please wait..");

        FirebaseUtilClass.getDatabaseReference().child("Talks").child(item.getDay()).child(item.getPushId()).child("participantsMap").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("p").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                unParticipateUI();


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                participateUI();
                Toast.makeText(getContext(), "Failed to participate, Try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
