package com.example.helbblitz;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MainActivity extends BaseActivity {
    public FloatingActionButton fab_search;
    public DrawerLayout drawerLayout;
    public BottomNavigationView bottomNavigationView;
    private NavigationView myNavigationView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);

        methodeAlarme();

        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab_search = findViewById(R.id.fab_buttonSearch);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        fab_search.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        // Menu en bas de l'écran
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.map:
                    startActivity(new Intent(MainActivity.this, MapActivity.class));    // Pour la map on utilise une activite
                    break;
                case R.id.fab_buttonSearch:
                    break;
                case R.id.settings:
                    if (mAuth.getCurrentUser() != null) {
                        fragment = new SettingsFragment();
                    } else {
                        Toast.makeText(this, "Please Log In to access settings", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.userprofile:
                    if (mAuth.getCurrentUser() != null) {
                        fragment = new UserprofileFragment();
                    } else {
                        Toast.makeText(this, "Please Log In to access profile", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
            return true;
        });

        fab_search.setOnClickListener(v -> {
            replaceFragment(new SearchFragment());
        });


        //Menu naviguation a gauche de l'ecran (défilant)
        myNavigationView = findViewById(R.id.navigation_view_left);
        myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Log.d(TAG, "onNavigationItemSelected: Clicked!");

                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.nav_home:
                        Log.d(TAG, "onNavigationItemSelected: Nav_home");
                        replaceFragment(new HomeFragment());
                        return true;
                    case R.id.nav_settings:
                        Log.d(TAG, "onNavigationItemSelected: Nav_settings");
                        replaceFragment(new SettingsFragment());
                        return true;
                    case R.id.nav_about:
                        Log.d(TAG, "onNavigationItemSelected: Nav_about");
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        return true;
                    case R.id.nav_logout:
                        Log.d(TAG, "onNavigationItemSelected: Nav_logout");
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    // Réalisé avec ChatGPT
    private void methodeAlarme() {
        // Définissez le moment où vous voulez que l'alarme se déclenche
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00); // heures
        calendar.set(Calendar.MINUTE, 15); // minutes

        // Créez un Intent qui pointe vers votre BroadcastReceiver
        Intent intent = new Intent(this, AlarmReceiver.class);

        // Créez un PendingIntent qui sera déclenché lorsque l'alarme se déclenche
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Récupérez une instance de AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Définissez l'alarme pour se répéter tous les jours à la même heure
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}






