package com.qerat.fstweek;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KeyNoteSpeakerContainerFragment extends Fragment {

    private TextView fragNoTextView, totFragNoTextView;
    private ImageView nextImageView, prevImageView;
    private LinearLayout noEvents, loading;
    private RelativeLayout hasEvents;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        noEvents=view.findViewById(R.id.noMeetUpLayout);
        loading=view.findViewById(R.id.loadingLayout);
        hasEvents=view.findViewById(R.id.hasSpeaker);


        nextImageView = view.findViewById(R.id.nextLayout);
        prevImageView = view.findViewById(R.id.prevLayout);

        fragNoTextView = view.findViewById(R.id.fragNoTextView);
        totFragNoTextView = view.findViewById(R.id.totFragNotTextView);


        final ViewPager viewPager = view.findViewById(R.id.viewPager);


        SpeakerViewPagerAdapter adapter = new SpeakerViewPagerAdapter(getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setCurrPageNo(i + 1);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }

        });
        prevImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        });

        // readDataFromFirebase();

    }
    private void setHasEvents(){
        hasEvents.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        noEvents.setVisibility(View.GONE);
    }

    private void setNoEvents(){
        hasEvents.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        noEvents.setVisibility(View.VISIBLE);
    }
    public void setTotPage(int i) {
        if(i>0){
            setHasEvents();
        }else {
            setNoEvents();
        }
        totFragNoTextView.setText(String.valueOf(i));
    }

    public void setCurrPageNo(int i) {
        fragNoTextView.setText(String.valueOf(i));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_keynote_container, container, false);
    }

}
