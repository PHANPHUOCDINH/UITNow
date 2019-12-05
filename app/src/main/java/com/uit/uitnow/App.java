package com.uit.uitnow;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;

public class App extends Application {
    User user;
    LatLng location;
    Basket basket;
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
