package com.example.final_project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Info extends AppCompatActivity implements View.OnClickListener, LocationListener {
    Button btngames, btntable, btnmap;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    User user;
    LocationManager locationManager;
    double latitude, longitude;
    List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        fragmentManager = getSupportFragmentManager();
        user = (User) getIntent().getExtras().getSerializable("user");

        btnmap = findViewById(R.id.mapbtn);
        btngames = findViewById(R.id.gamesbtn);
        btntable = findViewById(R.id.tablebtn);

        btnmap.setOnClickListener(this);
        btntable.setOnClickListener(this);
        btngames.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 2000, 10,this);


    }

    @Override
    public void onClick(View view) {
        if (view == btngames) {
            GamesFragment gamesFragment = (GamesFragment) fragmentManager.findFragmentByTag("games");
            if (gamesFragment == null) {
                gamesFragment = new GamesFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, gamesFragment, "games");
                fragmentTransaction.commit();
            }
        }

        if (view == btntable) {
            TableFragment tableFragment = (TableFragment) fragmentManager.findFragmentByTag("table");
            if (tableFragment == null) {
                tableFragment = new TableFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, tableFragment, "table");
                fragmentTransaction.commit();
            }
        }

        if (view == btnmap) {
          //  Intent intent = new Intent(this, MapActivity.class);
            //startActivity(intent);
            String country="Spain";
            String city="Madrid";
            String street="Av. de Concha Espina, 1, 28036";
            String geo="geo:0,0?q="+street+", "+city+", "+country+", &z=15";

            getDirections("40.4637,-3.7492","40.45211, -3.685518");

        }
    }
    public void getDirections(String from, String to) {
        Uri uri = Uri.parse("https://www.google.com/maps/dir/" + from + "/" + to);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude =  location.getLatitude();
        longitude =  location.getLongitude();
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());

        try{
            addressList=geocoder.getFromLocation(latitude,longitude,5);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
