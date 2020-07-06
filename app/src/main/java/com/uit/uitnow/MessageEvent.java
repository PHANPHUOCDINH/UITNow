package com.uit.uitnow;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;

public class MessageEvent {
    static final String FROM_storeFRAG_TO_storeACT = "FROM_storeFRAG_TO_storeACT";
    static final String FROM_storeACT_TO_trackingACT = "FROM_storeACT_TO_trackingACT";
    static final String FROM_resFRAG_TO_resACT="FROM_resFRAG_TO_resACT";
    static final String FROM_resACT_TO_confirmACT="FROM_resACT_TO_confirmACT";
    static final String FROM_bookingFRAG_TO_bookingtrackingACT="FROM_bookingFRAG_TO_bookingtrackingACT";
    static final String FROM_bookingtrackingACT_TO_confirmBooking="FROM_bookingtrackingACT_TO_confirmBooking";
    static final String TO_locationACT="TO_locationACT";
    Store store;
    Restaurant restaurant;
    String type;
    GeoPoint shop;
    Booking booking;
    public MessageEvent(Restaurant res,String type)
    {
        this.restaurant=res;
        this.type=type;
    }
    public MessageEvent(Store store, String type){
        this.store=store;
        this.type=type;
    }

    public MessageEvent(GeoPoint store,String type)
    {
        this.shop=store;
        this.type=type;
    }

    public MessageEvent(Booking booking,String type)
    {
        this.booking=booking;
        this.type=type;
    }
}
