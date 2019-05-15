package com.qerat.fstweek;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class KeynoteSpeakerItemFragment extends Fragment {

    private SpeakerClass item;
    private TextView speakerNameTextView, speakerDetailsTextView, dayOfTalkTextView, keynoteTextView;
    private ImageView speakerImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        item = (SpeakerClass) getArguments().getSerializable("item");
        return inflater.inflate(R.layout.fragment_speaker_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        speakerNameTextView = view.findViewById(R.id.speakerNameTextView);
        speakerDetailsTextView = view.findViewById(R.id.speakerDescTextView);
        dayOfTalkTextView = view.findViewById(R.id.dayOfTalkTextView);
        keynoteTextView = view.findViewById(R.id.keyNoteTextView);
        speakerImageView = view.findViewById(R.id.speakerImage);

        speakerNameTextView.setText(item.getSpeakerName());
        speakerDetailsTextView.setText(item.getSpeakerDetails());
        dayOfTalkTextView.setText(item.getDayOfTalk());
        keynoteTextView.setText(item.getSpeakerKeynote());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Speakers").child(item.getPushId());


        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Uri downloadUrl = uri;

                Picasso.get().load(downloadUrl.toString()).into(speakerImageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
