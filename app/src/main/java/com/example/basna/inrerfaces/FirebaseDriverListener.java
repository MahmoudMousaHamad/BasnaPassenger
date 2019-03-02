package com.example.basna.inrerfaces;

import com.example.basna.Model.Driver;

public interface FirebaseDriverListener {

    void onDriverAdded(Driver driver);

    void onDriverRemoved(Driver driver);

    void onDriverUpdated(Driver driver);
}
