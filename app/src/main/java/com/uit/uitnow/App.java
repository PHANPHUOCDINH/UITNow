package com.uit.uitnow;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {
    User user;
    GeoPoint location;
    Basket basket;
    Order order;
    OrderRequest request;
    String requestId;
    String currentAddress;
    ArrayList<String> savedStore=new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationTask.createNotificationChannel(this);
    }
}
