package com.example.basna.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.basna.Model.Passenger;
import com.example.basna.inrerfaces.FirebasePassengerListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class FirebaseEventListenerHelper implements ChildEventListener {

    private final FirebasePassengerListener firebasePassengerListener;

    public FirebaseEventListenerHelper(FirebasePassengerListener firebasePassengerListener) {
        this.firebasePassengerListener = firebasePassengerListener;
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Passenger passenger = dataSnapshot.getValue(Passenger.class);
        firebasePassengerListener.onPassengerAdded(passenger);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Passenger passenger = dataSnapshot.getValue(Passenger.class);
        firebasePassengerListener.onPassengerUpdated(passenger);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Passenger passenger = dataSnapshot.getValue(Passenger.class);
        firebasePassengerListener.onPassengerRemoved(passenger);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
