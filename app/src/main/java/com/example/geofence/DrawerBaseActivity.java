package com.example.geofence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FrameLayout frameLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        frameLayout = drawerLayout.findViewById(R.id.nav_ActivityContainer);
        frameLayout.addView(view);
        super.setContentView(drawerLayout);

        toolbar = drawerLayout.findViewById(R.id.nav_Toolbar);
        setSupportActionBar(toolbar);

        navigationView = drawerLayout.findViewById(R.id.nav_View);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch(item.getItemId()){
            case (R.id.nav_Pets):
                startActivity(new Intent(this, AccountDetailsActivity.class));
                overridePendingTransition(0,0);
                break;
            case (R.id.nav_Map):
                startActivity(new Intent(this, MapsActivity.class));
                overridePendingTransition(0,0);
                break;
            case (R.id.nav_Settings):
                break;
            case (R.id.nav_Logout):
                break;
            default:
                break;
        }

        return false;
    }

    // Writing the title to the app bar
    protected void setNavActivityTitle(String str){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(str);
        }
    }
}