package com.qerat.fstweek;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyGroupFragment extends Fragment {

    private LinearLayout mainLayout, notAMentorshipLayout, loadingLayout, failedLayout;
    private RecyclerView recyclerView;
    private Button signUp;
    private MyGroupAdapter mAdapter;
    private List<String> itemList = new ArrayList<>();
    private List<UserDetails> userItemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_my_group, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        recyclerView = view.findViewById(R.id.myGroupRecyclerView);
        notAMentorshipLayout = view.findViewById(R.id.notAMentorLayout);
        mainLayout = view.findViewById(R.id.mainParent);
        failedLayout = view.findViewById(R.id.failedLayout);
        loadingLayout = view.findViewById(R.id.loadingLayout);
        signUp = view.findViewById(R.id.signUpButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MentorshipInfoActivity.class);
                intent.putExtra("back", true);
                startActivity(intent);
            }
        });
        checkIfMentor();


        mAdapter = new MyGroupAdapter(userItemList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }


    private void setAllOk() {
        mainLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        failedLayout.setVisibility(View.GONE);
        notAMentorshipLayout.setVisibility(View.GONE);
    }

    private void setFailed() {
        mainLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        failedLayout.setVisibility(View.VISIBLE);
        notAMentorshipLayout.setVisibility(View.GONE);
    }

    private void seNotAMentorship() {
        mainLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        failedLayout.setVisibility(View.GONE);
        notAMentorshipLayout.setVisibility(View.VISIBLE);
    }

    private void checkIfMentor() {
        FirebaseUtilClass.getDatabaseReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mentorshipInformation").child("mentorship").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean ment = dataSnapshot.getValue(Boolean.class);
                if (ment!=null && ment) {
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

    public void readDataFromFirebase() {

        FirebaseUtilClass.getDatabaseReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mentorshipInformation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MentorshipInformation info = dataSnapshot.getValue(MentorshipInformation.class);
                FirebaseUtilClass.getDatabaseReference().child("MentorshipGroups").child(info.getPurpose() + "_" + info.getAreaStudy()).child("members").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        itemList.clear();
                        userItemList.clear();
                        setAllOk();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {


                            itemList.add(dsp.getValue(String.class));

                            FirebaseUtilClass.getDatabaseReference().child("Users").child(dsp.getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    userItemList.add(dataSnapshot.getValue(UserDetails.class));
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    setFailed();
                                }
                            });
                            //  dayList.add(dsp.getValue(DayEventClass.class)); //add result into array list
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        setFailed();
                    }
                });


                //  mAdapter.notifyDataSetChanged();


                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setFailed();
            }
        });
    }
}
