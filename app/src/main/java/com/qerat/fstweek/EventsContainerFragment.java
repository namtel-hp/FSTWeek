package com.qerat.fstweek;

import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsContainerFragment extends Fragment {


    private TextView dayNoTextView;
    private Button registerButton;
    private ImageView nextImageView, prevImageView;
    private ArrayList<String> dayList = new ArrayList<>();
    private ViewPager viewPager;
    private EventViewPagerAdapter adapter;
    private LinearLayout noEvents, hasEvents, loading;


    private List<EventClass> selectedPushId = new ArrayList<>();
    private List<EventClass> notSelectedPushId = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conf, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noEvents = view.findViewById(R.id.noMeetUpLayout);
        loading = view.findViewById(R.id.loadingLayout);
        hasEvents = view.findViewById(R.id.eventHas);

        registerButton = view.findViewById(R.id.participateButton);

        dayNoTextView = view.findViewById(R.id.dayNoTextView);
        nextImageView = view.findViewById(R.id.nextImageView);
        prevImageView = view.findViewById(R.id.prevImageView);

        viewPager = view.findViewById(R.id.viewPager);

        adapter = new EventViewPagerAdapter(getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);


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


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeDataToFirebase();
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                dayNoTextView.setText(String.valueOf(dayList.get(i)));


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void setHasEvents() {
        hasEvents.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        noEvents.setVisibility(View.GONE);
    }

    private void setNoEvents() {
        hasEvents.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        noEvents.setVisibility(View.VISIBLE);
    }


    private int dataWritten = 0;

    private void writeDataToFirebase() {
        loadingData();
        for (EventClass str : selectedPushId) {
            dataWritten++;
            FirebaseUtilClass.getDatabaseReference().child("Talks").child(str.getDay()).child(str.getPushId()).child("participantsMap").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("p").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dataWritten--;
                    if (dataWritten == 0) {
                        notLoadingData();
                    }
                }
            });

        }

        for (EventClass str : notSelectedPushId) {
            dataWritten++;
            FirebaseUtilClass.getDatabaseReference().child("Talks").child(str.getDay()).child(str.getPushId()).child("participantsMap").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dataWritten--;
                    if (dataWritten == 0) {
                        notLoadingData();
                    }
                }
            });

        }
    }

    public void setDayList(ArrayList<String> dayList) {
        if (dayList != null && dayList.size() > 0) {
            setHasEvents();
        } else {
            setNoEvents();
        }
        this.dayList = dayList;
    }

    private void loadingData() {
        registerButton.setText("Registering..");
        registerButton.setEnabled(false);
        nextImageView.setEnabled(false);
        prevImageView.setEnabled(false);

    }

    private void notLoadingData() {
        registerButton.setText("Register");
        registerButton.setEnabled(true);
        nextImageView.setEnabled(true);
        prevImageView.setEnabled(true);
    }

    public void addToNotSelectedPushId(EventClass str) {
        notSelectedPushId.add(str);
    }

    public void removeFromNotSelectedPushId(EventClass str) {
        notSelectedPushId.remove(str);
    }


    public void addToList(EventClass str) {
        selectedPushId.add(str);
    }

    public void removeFromList(EventClass str) {
        selectedPushId.remove(str);
    }

}
