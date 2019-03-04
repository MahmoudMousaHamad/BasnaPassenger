package com.example.basna.inrerfaces;

import com.example.basna.Model.Passenger;

public interface FirebaseDriverListener {

    void onPassengerAdded(Passenger passenger);

    void onPassengerRemoved(Passenger passenger);

    void onPassengerUpdated(Passenger passenger);
}
