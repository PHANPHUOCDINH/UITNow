package com.uit.uitnow;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OrderTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    App app;
    GoogleMap map;
    TextView tvDelivery, tvStore, tvTotal, tvStatus;
    Button btnCancelBooking;
    FirebaseFirestore db;
    LatLng store,delivery;
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
        if (event != null && MessageEvent.FROM_storeACT_TO_trackingACT.equals(event.type)) {
            store=event.shop;
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        db=FirebaseFirestore.getInstance();
        app = (App) getApplication();
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frgMaps);
        mapFragment.getMapAsync(this);

        tvDelivery = findViewById(R.id.tvDelivery);
        tvStore = findViewById(R.id.tvStore);
        tvTotal = findViewById(R.id.tvTotal);
        tvStatus = findViewById(R.id.tvStatus);

        tvDelivery.setText(app.order.deliveryAddress);
        tvStore.setText(app.order.storeName);
        tvTotal.setText(app.order.basket.getTotalPrice());
        btnCancelBooking=findViewById(R.id.btnCancel);
        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancelBookingOrder(app.order.id);
            }
        });
        delivery=app.location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            map = googleMap;
            placeUserMarkerOnMap();
            placeStoreMarkerOnMap();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(store,18));

        }
    }

    private void placeUserMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(delivery);
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_location)));
        options.title(PrefUtil.loadPref(this,"address"));
        map.addMarker(options);
    }

    private void placeStoreMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(store);
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(),R.drawable.ic_marker)));
        options.title(app.order.storeName);
        map.addMarker(options);
       // map.moveCamera(CameraUpdateFactory.newLatLngZoom(app.order.storeLocation, 20));
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

//    private void cancelBookingOrder(String id)
//    {
//        Map<String, Object> data = new HashMap<>();
//        data.put("trangThai", "Cancel");
//        db.collection("orders").document(id).set(data, SetOptions.merge());
//        finish();
//    }
}
