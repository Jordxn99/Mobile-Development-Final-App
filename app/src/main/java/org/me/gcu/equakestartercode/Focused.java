package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

public class Focused extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView depth;
    TextView location;
    TextView magnitude;
    TextView link;
    TextView date;
    Quake quake;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focused);
        quake = (Quake)getIntent().getExtras().getSerializable("quake");
        depth = (TextView)findViewById(R.id.depth);
        location = (TextView)findViewById(R.id.location);
        magnitude = (TextView)findViewById(R.id.magnitude);
        link = (TextView)findViewById(R.id.link);
        date = (TextView)findViewById(R.id.date);
        depth.setText(quake.getDepth());
        String newLocation = quake.getLocation().replace("Location:","");
        location.setText(newLocation);
        date.setText(quake.getPubDate());
        magnitude.setText(quake.getMagnitude());
        link.setText("Link to Earthquake: " + quake.getLink());
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


    }

    private void setActionBar(Toolbar toolbar) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("quake", quake);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.nav_map:
                Intent intent = new Intent(getApplicationContext(),Map.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("quake", quake);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.nav_search:
                Intent sIntent = new Intent(getApplicationContext(),Search.class);
                Bundle sBundle = new Bundle();
                sBundle.putSerializable("quake", quake);
                sIntent.putExtras(sBundle);
                startActivity(sIntent);
        }


        return true;
    }
}