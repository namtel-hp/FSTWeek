package com.qerat.fstweek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MeetMentorFragment extends Fragment {
    private View v;
    private RecyclerView recyclerView;
    private TextView dateTextView, timeTextView, locTextView, noDiscussions;
    private EditText msgEditText;
    private ImageView locImageView;
    private Button sendMsgButton,signUpButton;
    private LinearLayout allOKLayout,loadingLayout,notMeetUpScheduledLayout,notAMentorshipLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v != null) {
            if ((ViewGroup) v.getParent() != null)
                ((ViewGroup) v.getParent()).removeView(v);
            return v;
        }
        v = inflater.inflate(R.layout.fragment_meet_mentorship, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.discussions);
        dateTextView = view.findViewById(R.id.dateTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        locTextView = view.findViewById(R.id.locationTextView);
        noDiscussions = view.findViewById(R.id.noDiscussionsMsg);
        msgEditText = view.findViewById(R.id.msgEditText);
        locImageView = view.findViewById(R.id.locImageView);
        sendMsgButton = view.findViewById(R.id.postButton);

        allOKLayout=view.findViewById(R.id.allOkLayout);
        loadingLayout=view.findViewById(R.id.loadingLayout);
        notMeetUpScheduledLayout=view.findViewById(R.id.noMeetUpLayout);
        notAMentorshipLayout=view.findViewById(R.id.notAMentorLayout);
        signUpButton=view.findViewById(R.id.signUpButton);

    }

    private void setAllOk(){
        allOKLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        notAMentorshipLayout.setVisibility(View.GONE);
        notMeetUpScheduledLayout.setVisibility(View.GONE);
    }
    private void setNotMeetUp(){
        allOKLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        notAMentorshipLayout.setVisibility(View.GONE);
        notMeetUpScheduledLayout.setVisibility(View.VISIBLE);
    }
    private void seNotAMentorship(){
        allOKLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        notAMentorshipLayout.setVisibility(View.VISIBLE);
        notMeetUpScheduledLayout.setVisibility(View.GONE);
    }


}
