package com.example.basna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basna.Model.Passenger;
import com.example.basna.collections.MarkerCollection;
import com.example.basna.inrerfaces.FirebaseDriverListener;
import com.example.basna.inrerfaces.LatLngInterpolator;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.basna.helpers.*;

public class MainActivity extends AppCompatActivity implements FirebaseDriverListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String ONLINE_DRIVERS = "online_drivers";

    private final GoogleMapHelper googleMapHelper = new GoogleMapHelper();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(ONLINE_DRIVERS);

    private GoogleMap googleMap;
    private LocationRequest locationRequest;
    private UiHelper uiHelper;
    private FirebaseEventListenerHelper firebaseEventListenerHelper;
    private FusedLocationProviderClient locationProviderClient;

    private TextView totalOnlineDrivers;

    private boolean locationFlag = true;

    private Marker currentPositionMarker;


    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            if (location == null) return;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (locationFlag) {
                locationFlag = false;
                animateCamera(latLng);
            }
            showOrAnimateMarker(location);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.supportMap);
        assert mapFragment != null;

        uiHelper = new UiHelper(this);

        mapFragment.getMapAsync(googleMap -> this.googleMap = googleMap);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = uiHelper.getLocationRequest();

        if (!uiHelper.isPlayServicesAvailable(this)) {
            Toast.makeText(this, "Play Services did not installed!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            requestLocationUpdates();
        }

        totalOnlineDrivers = findViewById(R.id.totalOnlineDrivers);
        firebaseEventListenerHelper = new FirebaseEventListenerHelper(this);
        databaseReference.addChildEventListener(firebaseEventListenerHelper);
    }

    private void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = googleMapHelper.buildCameraUpdate(latLng, this);
        googleMap.animateCamera(cameraUpdate);
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        if (!uiHelper.hasLocationPermission()) {
            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        if (uiHelper.isLocationProviderEnabled())
        {
                uiHelper.buildAlertMessageNoGps();
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            int value = grantResults[0];
            if (value == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            } else if (value == PackageManager.PERMISSION_GRANTED)
                requestLocationUpdates();
        }
    }

    @Override
    public void onPassengerAdded(Passenger passenger) {
        MarkerOptions markerOptions =
                googleMapHelper.getDriverMarkerOptions(
                        new LatLng(passenger.getLat()
                        , passenger.getLng())
                        , getApplicationContext()
                        , R.drawable.car_icon);

        Marker marker = googleMap.addMarker(markerOptions);

        Log.e("Add new passenger -> ", passenger.toString());

        marker.setTag(passenger.getId());

        MarkerCollection.insertMarker(marker);

        totalOnlineDrivers.setText(getResources().
                getString(R.string.total_online_drivers).
                concat(" ").
                concat(String
                        .valueOf(MarkerCollection
                                .allMarkers()
                                .size())));
    }

    @Override
    public void onPassengerRemoved(Passenger passenger) {
        MarkerCollection.removeMarker(passenger.getId());
        Log.e("Removed user -> ", passenger.toString());
        totalOnlineDrivers.setText(getResources()
                .getString(R.string.total_online_drivers)
                .concat(" ")
                .concat(String
                        .valueOf(MarkerCollection
                                .allMarkers()
                                .size())));
    }

    @Override
    public void onPassengerUpdated(Passenger passenger) {
        Log.e("Updated passenger -> ", passenger.toString());
        Marker marker = MarkerCollection.getMarker(passenger.getId());
        assert marker != null;
        MarkerAnimationHelper.animateMarkerToGB(marker, new LatLng(passenger.getLat(), passenger.getLng()), new LatLngInterpolator.Spherical());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(firebaseEventListenerHelper);
        locationProviderClient.removeLocationUpdates(locationCallback);
        MarkerCollection.clearMarkers();
    }

    private void showOrAnimateMarker(Location location)
    {
        if (currentPositionMarker == null)
            currentPositionMarker = googleMap
                    .addMarker(googleMapHelper.getDriverMarkerOptions(new LatLng(location.getLatitude()
                            , location.getLongitude())
                            , this
                            , R.drawable.current_position_icon));
        else
            MarkerAnimationHelper.animateMarkerToGB(
                    currentPositionMarker,
                    new LatLng(location.getLatitude(),
                            location.getLongitude()),
                    new LatLngInterpolator.Spherical());
    }


}
