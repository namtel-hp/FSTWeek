package com.qerat.fstweek;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private EventsContainerFragment eventsContainerFragment;
    private KeyNoteSpeakerContainerFragment speakerContainerFragment;
    private MeetConferencePeopleFragmennt meetConferencePeopleFragmennt;

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
            if (speakerContainerFragment == null) {
                speakerContainerFragment = new KeyNoteSpeakerContainerFragment();
            }
            toolbar.setTitle("Speakers");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, speakerContainerFragment).commit();
        } else if (id == R.id.nav_meeting) {

            toolbar.setTitle("Meeting");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MeetMentorFragment()).commit();
        } else if (id == R.id.nav_mygroup) {
            toolbar.setTitle("My Group");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyGroupFragment()).commit();
        } else if (id == R.id.nav_meetup) {
            if (meetConferencePeopleFragmennt == null) {
                meetConferencePeopleFragmennt = new MeetConferencePeopleFragmennt();
            }
            toolbar.setTitle("Meet Up");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, meetConferencePeopleFragmennt).commit();
        } else if (id == R.id.nav_panel) {
            toolbar.setTitle("Panel Discussions");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PanelDiscussionsFragment()).commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
