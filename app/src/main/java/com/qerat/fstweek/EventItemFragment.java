package com.qerat.fstweek;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventItemFragment extends Fragment {

    private String day;
    private RecyclerView recyclerView;
    private List<EventClass> itemList = new ArrayList<>();
    private EventsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        day = getArguments().getString("item");
        return inflater.inflate(R.layout.fragment_event_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        initializeRecyclerView();

        readDataFromFirebase();

    }

    private void initializeRecyclerView() {
        recyclerView = getView().findViewById(R.id.eventItemContainerRecyclerView);


        mAdapter = new EventsAdapter(itemList, getContext(),(EventsContainerFragment) getParentFragment());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    public void readDataFromFirebase() {
        FirebaseUtilClass.getDatabaseReference().child("Talks").child(day).orderByChild("eventTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    itemList.add(dsp.getValue(EventClass.class)); //add result into array list

                }
                mAdapter.notifyDataSetChanged();


                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //     refreshlayout.setRefreshing(false);
            }
        });
    }

}
