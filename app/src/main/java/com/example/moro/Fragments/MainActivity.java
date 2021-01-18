package com.example.moro.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SearchView;


import com.example.moro.BuildConfig;
import com.example.moro.Data.DTO.EventDTO;
import com.example.moro.Data.DTO.ProfileDTO;
import com.example.moro.Fragments.BurgerMenu.BurgerMenuFragment;
import com.example.moro.Fragments.EventHandler.EventAdapter;
import com.example.moro.Fragments.EventHandler.EventFragment;
import com.example.moro.Fragments.Login.Context;
import com.example.moro.Fragments.Login.FavouritesFragment;
import com.example.moro.Fragments.Login.LoginFragment;
import com.example.moro.Fragments.Login.MyProfile;
import com.example.moro.Fragments.Login.NotLoginState;
import com.example.moro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.EnumMap;

import io.sentry.android.core.SentryAndroid;

public class MainActivity extends AppCompatActivity {

    Context ctx = Context.getInstance();
    ArrayList<EventDTO> favouritesEvents = new ArrayList<>();

    public ArrayList<EventDTO> getFavouritesEvents() {
        return favouritesEvents;
    }

    public static MainActivity activity;
    BottomNavigationView bottomNav;
    Toolbar topNav;
    boolean RUNSONPHONE = Build.PRODUCT.contains("sdk"); //|| Build.MODEL.contains("Emulator");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new HomeFragment());
        activity = this;

        /* Sentry Error tracking initialization */
        SentryAndroid.init(this, options -> {
            options.setDsn("https://5c95bc18ac2347c1a654c669e48ee273@o503098.ingest.sentry.io/5587708");
            options.setBeforeSend(((event, hint) -> {
                /* If run in debug, dont report events */
                if (BuildConfig.DEBUG) {
                    return null;
                } else
                    return event;
            }));
            /* Sets environment */
            if (RUNSONPHONE) {
                options.setEnvironment("PHONE");
            } else {
                options.setEnvironment("EMULATOR");
            }
        });

        /* Sets support for the navigation bar and top toolbar */
        bottomNav = findViewById(R.id.bottom_navigation);
        topNav = findViewById(R.id.top_navigation_toolbar);
        setSupportActionBar(topNav);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        topNav.setNavigationIcon(null);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.toptoolbar);

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.bot_nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.bot_nav_events:
                    selectedFragment = new EventFragment();
                    break;
                case R.id.bot_nav_favorite:
                    //henvises til login fragment, hvis ikke man er logget in
                    ctx.favouritFragment(getSupportFragmentManager());
                    break;
                case R.id.bot_nav_menu:
                    selectedFragment = new BurgerMenuFragment();
                    break;
            }
            if (selectedFragment == null)
                return true;

            replaceFragment(selectedFragment);

            return true;
        });
    }

    /* Sets the menu for top nav to the custom search menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation, menu);
        menu.findItem(R.id.menu_top_nav_search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_top_nav_profile:
                ctx.profilePressed(getSupportFragmentManager());
                break;
        }
        return true;
    }

    /* If back stack has a count of 1 - finish the activity instead of going back another time (Would give a whitescreen) */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_fragment_container, fragment);
//            ft.setCustomAnimations(R.anim.fragment_close_enter, R.anim.fragment_close_exit,R.anim.fragment_close_enter, R.anim.fragment_close_exit);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}