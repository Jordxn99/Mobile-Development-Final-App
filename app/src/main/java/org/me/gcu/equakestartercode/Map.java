package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.LinkedList;

public class Map extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private ArrayList<Quake> quakes;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setActionBar(toolbar);
        }


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        try{
            quakes = (ArrayList<Quake>)getIntent().getExtras().getSerializable("quake");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setActionBar(Toolbar toolbar) {
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng a = new LatLng(54.5, -2.77);
        for(int i=0; i < quakes.size(); i++) {
                Float color = null;
                LatLng q = new LatLng(Float.parseFloat(quakes.get(i).getGeolat()), Float.parseFloat(quakes.get(i).getGeolong()));
                Float mag =  Float.parseFloat(quakes.get(i).getMagnitude().replace("Magnitude:",""));
            if(mag < 1 ) {
                color = BitmapDescriptorFactory.HUE_GREEN;
            } else if(mag < 2){
                color = BitmapDescriptorFactory.HUE_YELLOW;
            } else {
                color = BitmapDescriptorFactory.HUE_RED;
            }
                mMap.addMarker(new MarkerOptions().
                        position(q)
                        .title("Marker in " + quakes.get(i).getLocation()).icon(BitmapDescriptorFactory.defaultMarker(color)));

            }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(a));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_map:
                break;
            case R.id.nav_home:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("quake", quakes);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.nav_search:
                Intent sIntent = new Intent(getApplicationContext(),Search.class);
                Bundle sBundle = new Bundle();
                sBundle.putSerializable("quake", quakes);
                sIntent.putExtras(sBundle);
                startActivity(sIntent);
        }

        return true;
    }
}