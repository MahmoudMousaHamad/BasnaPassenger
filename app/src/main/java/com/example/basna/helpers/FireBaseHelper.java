package com.example.basna.helpers;

import android.util.Log;

import com.example.basna.Model.Passenger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseHelper {

    private final String ONLINE_DRIVERS = "online_drivers";

    private DatabaseReference onlineDriverDatabaseReference;

    public FireBaseHelper(String driverId)
    {
        onlineDriverDatabaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(ONLINE_DRIVERS)
                .child(driverId);
        onlineDriverDatabaseReference
                .onDisconnect()
                .removeValue();
    }

    public void updatePassenger(Passenger Passenger)
    {
        onlineDriverDatabaseReference
                .setValue(Passenger);
        Log.e("Passenger info", "Updated");
    }

    public void deleteDriver()
    {
        onlineDriverDatabaseReference
                .removeValue();
    }


}
