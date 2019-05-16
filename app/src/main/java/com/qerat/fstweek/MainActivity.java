package com.qerat.fstweek;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private EventsContainerFragment eventsContainerFragment;
    private KeyNoteSpeakerContainerFragment speakerContainerFragment;
    private MeetConferencePeopleFragmennt meetConferencePeopleFragmennt;
    private MeetMentorFragment meetMentorFragment;
    private MyGroupFragment myGroupFragment;
    private PanelDiscussionsFragment panelDiscussionsFragment;
    private ChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_event));
        navigationView.setCheckedItem(R.id.nav_event);
        toolbar.setTitle("Events");



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_event) {
            if (eventsContainerFragment == null) {
                eventsContainerFragment = new EventsContainerFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, eventsContainerFragment).commit();
            toolbar.setTitle("Events");
            // Handle the camera action
        } else if (id == R.id.nav_speaker) {
/*
            if (speakerContainerFragment == null) {
                speakerContainerFragment = new KeyNoteSpeakerContainerFragment();
            }*/
            if (getSupportFragmentManager().findFragmentByTag("S") != null) {
                // getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("S"));
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new KeyNoteSpeakerContainerFragment()).addToBackStack("S").commit();

            }
            toolbar.setTitle("Speakers");
        } else if (id == R.id.nav_meeting) {
            if (meetMentorFragment == null) {
                meetMentorFragment = new MeetMentorFragment();
            }
            toolbar.setTitle("Meeting");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, meetMentorFragment).commit();
        } else if (id == R.id.nav_mygroup) {
            toolbar.setTitle("My Group");
            if (myGroupFragment == null) {
                myGroupFragment = new MyGroupFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myGroupFragment).commit();
        } else if (id == R.id.nav_meetup) {
            if (meetConferencePeopleFragmennt == null) {
                meetConferencePeopleFragmennt = new MeetConferencePeopleFragmennt();
            }
            toolbar.setTitle("Meet Up");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, meetConferencePeopleFragmennt).commit();
        } else if (id == R.id.nav_panel) {
            toolbar.setTitle("Panel Discussions");
            if (panelDiscussionsFragment == null) {
                panelDiscussionsFragment = new PanelDiscussionsFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, panelDiscussionsFragment).commit();
        } else if (id == R.id.nav_chat) {
            toolbar.setTitle("Mentors Chat");
            if (chatFragment == null) {
                chatFragment = new ChatFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chatFragment).commit();
        } else if (id == R.id.nav_visit) {
            String url = "https://www.fstweek2019.com/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_signout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure to logout?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signOut();
                    SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(SignInActivity.POSITION_KEY, SignInActivity.SIGN_IN);
                    editor.commit();
                    Intent i = new Intent(getBaseContext(), SignInActivity.class);


                    startActivity(i);
                    finish();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
