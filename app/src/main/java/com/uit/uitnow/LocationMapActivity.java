package com.uit.uitnow;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LocationMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    GeoPoint store;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frgMaps);
        mapFragment.getMapAsync(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event)
    {
        if (event != null && MessageEvent.TO_locationACT.equals(event.type)) {
            store=event.shop;
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            map = googleMap;
            placeUserMarkerOnMap();
            placeStoreMarkerOnMap();
        }
    }

    private void placeUserMarkerOnMap() {
//       // MarkerOptions options = new MarkerOptions().position(new LatLng(delivery.getLatitude(),delivery.getLongitude()));
//        options.icon(BitmapDescriptorFactory.fromBitmap(
//                BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_location)));
//        options.title(PrefUtil.loadPref(this,"address"));
//        map.addMarker(options);
    }

    private void placeStoreMarkerOnMap() {
//        MarkerOptions options = new MarkerOptions().position(new LatLng(store.getLatitude(),store.getLongitude()));
//        options.icon(BitmapDescriptorFactory.fromBitmap(
//                BitmapFactory.decodeResource(getResources(),R.drawable.ic_marker)));
//        options.title(app.request.storeName);
//        map.addMarker(options);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(store.getLatitude(),store.getLongitude()),14));
//        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(app.order.storeLocation, 20));
    }
}
