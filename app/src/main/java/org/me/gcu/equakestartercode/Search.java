package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Search extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private TextView startDate;
    private TextView endDate;
    private Button button;
    private LinearLayout linearLayout;
    private ArrayList<Quake> quakes;
    private DatePickerDialog.OnDateSetListener dateListener, toDateListener;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        startDate = findViewById(R.id.startDate);
        startDate.setKeyListener(null);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Search.this, dateListener, 2021,01,01 );
                Date currDate = new Date();
                datePickerDialog.getDatePicker().setMaxDate(currDate.getTime());
                datePickerDialog.show();
            }
        });
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "." + (month + 1) + "." + year;
                startDate.setText(date);
            }
        };
        endDate = findViewById(R.id.endDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                DatePickerDialog datePickerDialog = new DatePickerDialog(Search.this, toDateListener, 2021,01,01);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                try {
                    datePickerDialog.getDatePicker().setMinDate(simpleDateFormat.parse(startDate.getText().toString()).getTime());
                }
                catch (Exception E) {
                    E.printStackTrace();
                }
                datePickerDialog.show();
            }
        });
        toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "." + (month + 1) + "." + year;
                endDate.setText(date);
            }
        };
        endDate.setKeyListener(null);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        linearLayout = findViewById(R.id.results);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        quakes = (ArrayList<Quake>) getIntent().getExtras().getSerializable("quake");
    }


    @Override
    public void onClick(View v) {
        if (v == button) {
            searchItems();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void searchItems() {
        ArrayList<Quake> selectedQuakes = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        Date sDate = null;
        Date eDate = null;


        Quake eastest = null;
        Quake westest = null;
        Quake northest = null;
        Quake southest = null;
        Quake shallowest = null;
        Quake biggest = null;
        Quake deepest = null;
        String neastest = "";
        String nwestest = "";
        String nnorthest = "";
        String nsouthest = "";
        String nshallowest = "";
        String nbiggest = "";
        String ndeepest = "";

        try {
            sDate = simpleDateFormat.parse(startDate.getText().toString());
            eDate = simpleDateFormat.parse(endDate.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < quakes.size(); i++) {
            try {
                Date currentDate = simpleDateFormat1.parse(quakes.get(i).getPubDate());
                if (currentDate.after(sDate) && currentDate.before(eDate)) {
                    selectedQuakes.add(quakes.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int k = 0; k < selectedQuakes.size(); k++) {
            if (k == 0) {
                eastest = selectedQuakes.get(0);
                northest = selectedQuakes.get(0);
                southest = selectedQuakes.get(0);
                westest = selectedQuakes.get(0);
                biggest = selectedQuakes.get(0);
                shallowest = selectedQuakes.get(0);
                deepest = selectedQuakes.get(0);
            } else {
                Float mag = Float.parseFloat(selectedQuakes.get(k).getMagnitude().replace("Magnitude:", ""));
                Float magnitude = Float.parseFloat(biggest.getMagnitude().replace("Magnitude:", ""));
                Float currentQDepth = Float.parseFloat(selectedQuakes.get(k).getDepth().replace("Depth:", "").replace("km", ""));
                Float currentDDepth = Float.parseFloat(deepest.getDepth().replace("Depth:", "").replace("km", ""));
                Float currentQShallowest = Float.parseFloat(shallowest.getDepth().replace("Depth:", "").replace("km", ""));
                if (Float.parseFloat(selectedQuakes.get(k).getGeolong()) > Float.parseFloat(eastest.getGeolong())) {
                    eastest = selectedQuakes.get(k);
                }
                if (Float.parseFloat(selectedQuakes.get(k).getGeolong()) > Float.parseFloat(westest.getGeolong())) {
                    westest = selectedQuakes.get(k);
                }
                if (Float.parseFloat(selectedQuakes.get(k).getGeolat()) < Float.parseFloat(northest.getGeolat())) {
                    northest = selectedQuakes.get(k);
                }
                if (Float.parseFloat(selectedQuakes.get(k).getGeolat()) > Float.parseFloat(southest.getGeolat())) {
                    southest = selectedQuakes.get(k);
                }
                if (currentQDepth < currentDDepth) {
                    deepest = selectedQuakes.get(k);
                }
                if (currentQDepth > currentQShallowest) {
                    shallowest = selectedQuakes.get(k);
                }
                if (mag > magnitude) {
                    biggest = selectedQuakes.get(k);
                }
            }
        }
        TextView textView = new TextView(this);
        neastest = eastest.getLocation().replace("Location: ", "");
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(0,10,0,0);
        textView.setText("EasternMost: " + neastest);
        linearLayout.addView(textView);

        TextView textView1 = new TextView(this);
        nnorthest = northest.getLocation().replace("Location: ", "");
        textView1.setTextColor(Color.BLACK);
        textView1.setPadding(0,15,0,0);
        textView1.setTextSize(16);
        textView1.setText("NorthernMost: " + nnorthest);
        linearLayout.addView(textView1);

        TextView textView2 = new TextView(this);
        nsouthest = southest.getLocation().replace("Location: ", "");
        textView2.setTextColor(Color.BLACK);
        textView2.setPadding(0,15,0,0);
        textView2.setTextSize(16);
        textView2.setText("SouthernMost: " + nsouthest);
        linearLayout.addView(textView2);

        TextView textView3 = new TextView(this);
        nwestest = westest.getLocation().replace("Location: ", "");
        textView3.setTextColor(Color.BLACK);
        textView3.setPadding(0,15,0,0);
        textView3.setTextSize(16);
        textView3.setText("WesternMost: " + nwestest);
        linearLayout.addView(textView3);

        TextView textView4 = new TextView(this);
        nbiggest = biggest.getLocation().replace("Location: ", "");
        textView4.setTextColor(Color.BLACK);
        textView4.setPadding(0,15,0,0);
        textView4.setTextSize(16);
        textView4.setText("Biggest Earthquake: " + nbiggest);
        linearLayout.addView(textView4);

        TextView textView5 = new TextView(this);
        nshallowest = shallowest.getLocation().replace("Location: ", "");
        textView5.setTextColor(Color.BLACK);
        textView5.setPadding(0,15,0,0);
        textView5.setTextSize(16);
        textView5.setText("Smallest Earthquake: " + nshallowest);
        linearLayout.addView(textView5);

        TextView textView6 = new TextView(this);
        ndeepest = deepest.getLocation().replace("Location: ", "");
        textView6.setTextColor(Color.BLACK);
        textView6.setPadding(0,15,0,0);
        textView6.setTextSize(16);
        textView6.setText("Deepest Earthquake: " + ndeepest);
        linearLayout.addView(textView6);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search:
                break;
            case R.id.nav_home:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("quake", quakes);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.nav_map:
                Intent sIntent = new Intent(getApplicationContext(),Map.class);
                Bundle sBundle = new Bundle();
                sBundle.putSerializable("quake", quakes);
                sIntent.putExtras(sBundle);
                startActivity(sIntent);
        }
        return true;
    }
}