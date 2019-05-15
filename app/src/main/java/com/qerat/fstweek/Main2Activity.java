package com.qerat.fstweek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity {

    private EventsContainerFragment eventsFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, eventsFragment).commit();
                    return true;
                case R.id.navigation_more:
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signOut();
                    SharedPreferences sharedPref = Main2Activity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(SignInActivity.POSITION_KEY, SignInActivity.SIGN_IN);
                    editor.commit();
                    Intent i = new Intent(getBaseContext(), SignInActivity.class);


                    startActivity(i);
                    finish();
                    return true;
                case R.id.navigation_speakers:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        eventsFragment = new EventsContainerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, eventsFragment).commit();
    }

}
