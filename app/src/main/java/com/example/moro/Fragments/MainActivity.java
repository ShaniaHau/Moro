package com.example.moro.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.moro.Fragments.EventHandler.EventFragment;
import com.example.moro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.sentry.Sentry;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Sentry.captureMessage("testing SDK setup");
//        // SENTRY TEST
//        try {
//            throw new Exception("This is a test of sentry.");
//        } catch (Exception e) {
//            Sentry.captureException(e);
//        }


        Fragment home = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, home).commit();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        BottomNavigationView topNav = findViewById(R.id.top_navigation);

        topNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.top_nav_profile:
                        selectedFragment = new LoginFragment();
                        break;
                    case R.id.top_nav_search:
                        //selectedFragment = new SearchFragment();
                        break;
                }
                if (selectedFragment == null)
                    return true;
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, selectedFragment).addToBackStack(null).commit();
                return true;
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.bot_nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.bot_nav_events:
                        selectedFragment = new EventFragment();
                        break;
                    case R.id.bot_nav_favorite:
                        selectedFragment = new favoritterFragment();
                        break;
                    case R.id.bot_nav_menu:
                        selectedFragment = new BurgerMenuFragment();
                        break;
                }
                if (selectedFragment == null)
                    return true;
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, selectedFragment).addToBackStack(null).commit();
                return true;
            }
        });
    }
}