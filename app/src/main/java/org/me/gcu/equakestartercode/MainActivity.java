package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.LinkedList;

import org.me.gcu.equakestartercode.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity implements OnClickListener, NavigationView.OnNavigationItemSelectedListener, ReturnResult {
    private LinearLayout quakeData;
    private Button startButton;
    private Button search;
    private Button map;
    private String result;
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private LinkedList<Quake> quakeLinkedList = new LinkedList<>();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        quakeData = (LinearLayout)findViewById(R.id.quakeData);
        quakeData.removeAllViews();

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

        startProgress();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void setActionBar(Toolbar toolbar) {
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","in onClick");
        Log.e("MyTag","after startProgress");
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        XMLAsyncTask xmlAsyncTask = new XMLAsyncTask(this);
        xmlAsyncTask.execute();
    } //

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_map:
                Intent intent = new Intent(getApplicationContext(),Map.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("quake", quakeLinkedList);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.nav_search:
                Intent sIntent = new Intent(getApplicationContext(),Search.class);
                Bundle sBundle = new Bundle();
                sBundle.putSerializable("quake", quakeLinkedList);
                sIntent.putExtras(sBundle);
                startActivity(sIntent);
        }

        return true;
    }

    @Override
    public void returnQuake(LinkedList<Quake> quakeLinkedList) {
        this.quakeLinkedList = quakeLinkedList;
        for(int i =0; i < quakeLinkedList.size(); i++){
            String[] result = quakeLinkedList.get(i).getMagnitude().split(":");
            String magnitude = result[1];
            TextView textView = new TextView(getApplicationContext());
            textView.setText(quakeLinkedList.get(i).getLocation()+"\n"+quakeLinkedList.get(i).getMagnitude());
            textView.setPadding(10,10,10,10);
            if(Float.parseFloat(magnitude) < 1 ) {
                textView.setTextColor(Color.GREEN);
            } else if(Float.parseFloat(magnitude) < 2) {
                textView.setTextColor(Color.YELLOW);
            }else if(Float.parseFloat(magnitude) >= 2) {
                textView.setTextColor(Color.RED);
            }

            textView.setClickable(true);
            Quake q = quakeLinkedList.get(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Focused.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("quake",q);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            quakeData.addView(textView);
        }
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.

}