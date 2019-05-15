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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KeyNoteSpeakerContainerFragment extends Fragment {
  //  private View v;
    private TextView fragNoTextView, totFragNoTextView;
    private ImageView nextImageView, prevImageView;


    //  private ArrayList<KeynoteSpeakerItemFragment> itemList = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


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

    public void setTotPage(int i) {
        totFragNoTextView.setText(String.valueOf(i));
    }

    public void setCurrPageNo(int i) {
        fragNoTextView.setText(String.valueOf(i));
    }

    /*


        private void changeFragment(int index) {

            if (currIndex < index) {
                if (index >= itemList.size()) {
                    index = 0;
                    currIndex = -1;
                }


                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.speakerFrameLayout, itemList.get(index)).commit();
                currIndex++;

            } else {
                if (index <= -1) {
                    index = itemList.size() - 1;
                    currIndex = itemList.size();
                }


                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.speakerFrameLayout, itemList.get(index)).commit();
                currIndex--;

            }
            fragNoTextView.setText(String.valueOf(index+1));
        }
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //  if (v != null) {
      //      if ((ViewGroup) v.getParent() != null)
     //           ((ViewGroup) v.getParent()).removeView(v);
     //       return v;
     //   }
     //   v = inflater.inflate(R.layout.fragment_keynote_container, container, false);

        return inflater.inflate(R.layout.fragment_keynote_container, container, false);
    }
/*
    public void readDataFromFirebase() {
        FirebaseUtilClass.getDatabaseReference().child("Speakers").orderByChild("dayOfTalk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                //      fragItem.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", dsp.getValue(SpeakerClass.class));


                    KeynoteSpeakerItemFragment temp = new KeynoteSpeakerItemFragment();
                    temp.setArguments(bundle);
                    itemList.add(temp);
                    //add result into array list
                    //  fragItem.add(temp);
                }
                totFragNoTextView.setText(String.valueOf(itemList.size()));
                progressBar.setVisibility(View.GONE);
                if (first) {
                    changeFragment(0);
                    first = false;
                }

                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    */
}
