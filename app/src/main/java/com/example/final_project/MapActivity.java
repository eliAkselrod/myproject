package com.example.final_project;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private Marker destinationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Create GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Check location permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        // Set a listener for marker click events
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Handle marker click event
                // Display directions to the specific location
                // ...
                return true;
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Check location permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, createLocationRequest(), this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Handle connection suspended
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Handle connection failed
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        // Update the user's current location on the map
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));

        // Add a marker for the specific location
        LatLng destinationLatLng = new LatLng(40.453053, -3.688344);
        destinationMarker = googleMap.addMarker(new MarkerOptions().position(destinationLatLng));

        // Display directions to the specific location
        String directionsUrl = getDirectionsUrl(currentLatLng, destinationLatLng);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(directionsUrl);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        String apiKey = "AIzaSyB4hbgOud2uhijUzjymoU1YUYFqH-T1vCE"; // Replace with your actual Google Maps API key
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String parameters = strOrigin + "&" + strDestination + "&key=" + apiKey;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        return url;
    }



}
