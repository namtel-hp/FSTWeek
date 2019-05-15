package com.qerat.fstweek;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SpeakerViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<KeynoteSpeakerItemFragment> itemList = new ArrayList<>();
    private KeyNoteSpeakerContainerFragment fragment;

    public SpeakerViewPagerAdapter(FragmentManager fm, KeyNoteSpeakerContainerFragment frag) {
        super(fm);
        this.fragment = frag;
        readDataFromFirebase();
    }


    @Override
    public Fragment getItem(int i) {

        return itemList.get(i);
    }



    @Override
    public int getCount() {
        fragment.setTotPage(itemList.size());
        return itemList.size();
    }

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

                notifyDataSetChanged();
                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

}
