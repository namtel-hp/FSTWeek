package com.qerat.fstweek;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class PanelDiscussionsFragment extends Fragment {

    private RecyclerView recyclerView;


    private LinearLayout noDiscussions;
    private EditText msgEditText;
    private Button sendMsgButton;
    private PostAdapter mAdapter;
    private List<PostClass> itemList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_panel_discussions, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        recyclerView = view.findViewById(R.id.panelRecyclerView);

        noDiscussions = view.findViewById(R.id.nothingFoundLayout);
        msgEditText = view.findViewById(R.id.msgEditText);
        sendMsgButton = view.findViewById(R.id.postButton);

        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(msgEditText.getText().toString().trim())) {
                    String pushId = FirebaseUtilClass.getDatabaseReference().child("PanelDisc").child("pending").push().getKey();
                    writeMsgToFirebase(new PostClass(pushId, FirebaseAuth.getInstance().getCurrentUser().getEmail(), msgEditText.getText().toString()));

                }
            }
        });

        setListenForMessage();
    }

    private void sendingMsg() {
        msgEditText.setEnabled(false);
        sendMsgButton.setEnabled(false);
        sendMsgButton.setText("Sending..");
    }

    private void notSendingMsg() {
        msgEditText.setEnabled(true);
        sendMsgButton.setEnabled(true);
        sendMsgButton.setText("Send");
    }

    private void setHaveData() {
        noDiscussions.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    private void writeMsgToFirebase(final PostClass item) {
        sendingMsg();
        FirebaseUtilClass.getDatabaseReference().child("PanelDisc").child("pending").child(item.getPushId()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notSendingMsg();
                msgEditText.setText("");

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                msgEditText.setError(e.getMessage());
                notSendingMsg();
            }
        });
    }

    private void setListenForMessage() {

        FirebaseUtilClass.getDatabaseReference().child("PanelDisc").child("accepted").orderByChild("msgTime").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                setHaveData();
                PostClass temp = dataSnapshot.getValue(PostClass.class);

                //  PostClass temp = new PostClass((String) map.get("pushId"), (String) map.get("email"), (String) map.get("msg"), (long) map.get("msgTime"));
                itemList.add(temp);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void noDataFound() {
        recyclerView.setVisibility(View.GONE);
        noDiscussions.setVisibility(View.VISIBLE);
    }

    private void dataFound() {
        recyclerView.setVisibility(View.VISIBLE);
        noDiscussions.setVisibility(View.GONE);
    }
}
