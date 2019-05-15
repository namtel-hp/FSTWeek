package com.qerat.fstweek;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<EventItemFragment> itemList = new ArrayList<>();
    private ArrayList<String> dayList = new ArrayList<>();
    private EventsContainerFragment fragment;

    public EventViewPagerAdapter(FragmentManager fm, EventsContainerFragment fragment) {
        super(fm);
        this.fragment = fragment;

        readDataFromFirebase();
    }

    @Override
    public Fragment getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }


    public void readDataFromFirebase() {

        FirebaseUtilClass.getDatabaseReference().child("Talks").orderByChild("Day").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                dayList.clear();

                // dayList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    Bundle bundle = new Bundle();
                    bundle.putString("item", dsp.getKey());


                    EventItemFragment temp = new EventItemFragment();
                    temp.setArguments(bundle);
                    itemList.add(temp);
                    dayList.add(dsp.getKey());

                    //  dayList.add(dsp.getValue(DayEventClass.class)); //add result into array list
                }
                fragment.setDayList(dayList);
                notifyDataSetChanged();
                //  mAdapter.notifyDataSetChanged();


                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
