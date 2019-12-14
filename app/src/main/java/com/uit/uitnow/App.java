package com.uit.uitnow;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.GeoPoint;

public class App extends Application {
    User user;
    GeoPoint location;
    Basket basket;
    Order order;
    OrderRequest request;
    String requestId;
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationTask.createNotificationChannel(this);
    }
}
