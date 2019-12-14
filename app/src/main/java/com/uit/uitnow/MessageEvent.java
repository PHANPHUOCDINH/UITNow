package com.uit.uitnow;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;

public class MessageEvent {
    static final String FROM_storeFRAG_TO_storeACT = "FROM_storeFRAG_TO_storeACT";
    static final String FROM_storeACT_TO_trackingACT = "FROM_storeACT_TO_trackingACT";
    Store store;
    String type;
    GeoPoint shop;
    public MessageEvent(Store store, String type){
        this.store=store;
        this.type=type;
    }

    public MessageEvent(GeoPoint store,String type)
    {
        this.shop=store;
        this.type=type;
    }

}
