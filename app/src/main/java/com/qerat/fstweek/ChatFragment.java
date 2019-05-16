package com.qerat.fstweek;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noDiscussions;
    private EditText msgEditText;

    private Button sendMsgButton, signUpButton;
    private MentorshipInformation info;

    private LinearLayout allOKLayout, loadingLayout, notAMentorshipLayout, failedLayout;
    private PostAdapter mAdapter;
    private List<PostClass> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_chat, container, false);

    }

    private void setHaveData() {
        noDiscussions.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.discussions);

        noDiscussions = view.findViewById(R.id.noDiscussionsMsg);
        msgEditText = view.findViewById(R.id.msgEditText);

        sendMsgButton = view.findViewById(R.id.postButton);
        failedLayout = view.findViewById(R.id.failedLayout);
        allOKLayout = view.findViewById(R.id.allOkLayout);
        loadingLayout = view.findViewById(R.id.loadingLayout);

        notAMentorshipLayout = view.findViewById(R.id.notAMentorLayout);
        signUpButton = view.findViewById(R.id.signUpButton);


        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(msgEditText.getText().toString().trim())) {
                    String pushId = FirebaseUtilClass.getDatabaseReference().child("Chats").child("mentDirect").child(info.getPurpose() + "_" + info.getAreaStudy()).push().getKey();
                    writeMsgToFirebase(new PostClass(pushId, FirebaseAuth.getInstance().getCurrentUser().getEmail(), msgEditText.getText().toString()));

                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MentorshipInfoActivity.class);
                intent.putExtra("back", true);
                startActivity(intent);
            }
        });
        checkIfMentor();
    }

    private void setAllOk() {
        allOKLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        notAMentorshipLayout.setVisibility(View.GONE);

    }


    private void seNotAMentorship() {
        allOKLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        notAMentorshipLayout.setVisibility(View.VISIBLE);

    }

    private void checkIfMentor() {
        FirebaseUtilClass.getDatabaseReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mentorshipInformation").child("mentorship").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean ment = dataSnapshot.getValue(Boolean.class);
                if (ment) {
                    readDataFromFirebase();
                } else {
                    seNotAMentorship();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setFailed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (itemList.size() != 0) {
            setHaveData();
        }
    }

    private void writeMsgToFirebase(final PostClass item) {
        sendingMsg();
        FirebaseUtilClass.getDatabaseReference().child("Chats").child("mentDirect").child(info.getPurpose() + "_" + info.getAreaStudy()).child(item.getPushId()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    public void readDataFromFirebase() {

        FirebaseUtilClass.getDatabaseReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mentorshipInformation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info = dataSnapshot.getValue(MentorshipInformation.class);

                setListenForMessage(info.getPurpose() + "_" + info.getAreaStudy());


                //  mAdapter.notifyDataSetChanged();


                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setFailed();
            }
        });
    }

    private void setListenForMessage(String pathId) {

        mAdapter = new PostAdapter(itemList, getActivity(), PostAdapter.MENT_CHAT, pathId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        itemList.clear();
        FirebaseUtilClass.getDatabaseReference().child("Chats").child("mentDirect").child(pathId).orderByChild("msgTime").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                setHaveData();
                PostClass temp = dataSnapshot.getValue(PostClass.class);

                //  PostClass temp = new PostClass((String) map.get("pushId"), (String) map.get("email"), (String) map.get("msg"), (long) map.get("msgTime"));
                itemList.add(temp);
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(itemList.size() - 1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                PostClass temp = dataSnapshot.getValue(PostClass.class);
                itemList.remove(temp);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void setFailed() {
        allOKLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        failedLayout.setVisibility(View.VISIBLE);
        notAMentorshipLayout.setVisibility(View.GONE);
    }

}
