package com.example.final_project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DirectionsFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private final LatLng destination = new LatLng(40.4530, -3.6883);
    private List<LatLng> routePoints;
    private Polyline routePolyline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_directions, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

        MarkerOptions destinationMarker = new MarkerOptions().position(destination).title("Destination");
        map.addMarker(destinationMarker);

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f));
                drawRoute(currentLocation, destination);
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        drawRoute(currentLocation, destination);
    }

    private void drawRoute(LatLng origin, LatLng destination) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                origin.latitude + "," + origin.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&key=AIzaSyB4hbgOud2uhijUzjymoU1YUYFqH-T1vCE";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonData = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray routesArray = jsonObject.getJSONArray("routes");
                        if (routesArray.length() > 0) {
                            JSONObject routeObject = routesArray.getJSONObject(0);
                            JSONObject polylineObject = routeObject.getJSONObject("overview_polyline");
                            String encodedPolyline = polylineObject.getString("points");

                            routePoints = decodePolyline(encodedPolyline);

                            getActivity().runOnUiThread(() -> {
                                if (routePolyline != null) {
                                    routePolyline.remove();
                                }

                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .addAll(routePoints)
                                        .color(Color.BLUE)
                                        .width(5f);

                                routePolyline = map.addPolyline(polylineOptions);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private List<LatLng> decodePolyline(String encodedPolyline) {
        List<LatLng> polyLine = new ArrayList<>();
        int index = 0;
        int len = encodedPolyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            polyLine.add(p);
        }
        return polyLine;
    }
}
