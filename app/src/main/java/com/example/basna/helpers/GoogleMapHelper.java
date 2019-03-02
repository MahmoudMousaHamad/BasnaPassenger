package com.example.basna.helpers;

import android.content.Context;

import com.example.basna.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapHelper {

    private final int ZOOM_LEVEL = 18;
    private final int TILT_LEVEL = 25;


    public CameraUpdate buildCameraUpdate(LatLng latLng, Context context)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .tilt(TILT_LEVEL)
                .zoom(ZOOM_LEVEL)
                .build();

        MapsInitializer.initialize(context);

        return CameraUpdateFactory.newCameraPosition(cameraPosition);
    }

    public MarkerOptions getDriverMarkerOptions(LatLng position, Context context, int resource)
    {
        MarkerOptions options = getMarkerOptions(resource, position, context);
        options.flat(true);
        return options;
    }

    public MarkerOptions getMarkerOptions(int resource, LatLng position, Context context)
    {
        MapsInitializer.initialize(context);
        return new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(resource))
                .position(position);
    }
}
