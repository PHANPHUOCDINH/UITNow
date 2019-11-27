package com.uit.uitnow;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

public class MessageEvent {
    static final String FROM_storeFRAG_TO_storeACT = "FROM_storeFRAG_TO_storeACT";
    Store store;
    String type;
    public MessageEvent(Store store, String type){
        this.store=store;
        this.type=type;
    }

}
