package com.example.basna.helpers;

import android.util.Log;

import com.example.basna.Model.Passenger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseHelper {

    private final String ONLINE_DRIVERS = "online_drivers";

    private DatabaseReference onlinePassengerDatabaseReference;

    public FireBaseHelper(String passengerId)
    {
        onlinePassengerDatabaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(ONLINE_DRIVERS)
                .child(passengerId);
        onlinePassengerDatabaseReference
                .onDisconnect()
                .removeValue();
    }

    public void updatePassenger(Passenger Passenger)
    {
        onlinePassengerDatabaseReference
                .setValue(Passenger);
        Log.e("Passenger info", "Updated");
    }

    public void deletePassenger()
    {
        onlinePassengerDatabaseReference
                .removeValue();
    }


}
