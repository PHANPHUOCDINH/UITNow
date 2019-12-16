package com.uit.uitnow;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class OrderTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    RelativeLayout layoutRequesting, layoutOnTheWay;
    App app;
    GoogleMap map;
    TextView tvDelivery, tvStore, tvTotal, tvStatus, tvDriverName, tvOnTheWay;
    Button btnCancelBooking;
    FirebaseFirestore db;
    GeoPoint store,delivery;
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
        layoutRequesting = findViewById(R.id.layoutRequesting);
        layoutOnTheWay = findViewById(R.id.layoutOnTheWay);
        tvDelivery.setText(app.order.deliveryAddress);
        tvStore.setText(app.order.storeName);
        tvTotal.setText(app.order.basket.getTotalPrice());
        btnCancelBooking=findViewById(R.id.btnCancel);
        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBookingOrder();
            }
        });
        tvDriverName = findViewById(R.id.tvDriverName);
        tvOnTheWay = findViewById(R.id.tvOnTheWay);
        delivery=app.location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            map = googleMap;
            placeUserMarkerOnMap();
            placeStoreMarkerOnMap();
            getCurrentRequest();
        }
    }

    private void placeUserMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(delivery.getLatitude(),delivery.getLongitude()));
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_location)));
        options.title(PrefUtil.loadPref(this,"address"));
        map.addMarker(options);
    }

    private void placeStoreMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(store.getLatitude(),store.getLongitude()));
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(),R.drawable.ic_marker)));
        options.title(app.order.storeName);
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(store.getLatitude(),store.getLongitude()),18));
       // map.moveCamera(CameraUpdateFactory.newLatLngZoom(app.order.storeLocation, 20));
    }

    private void placeDriverMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(app.request.driverLocation.getLatitude(), app.request.driverLocation.getLongitude())).title(app.request.driverName);
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_motor)));
        options.title(app.request.driverName);
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(app.request.driverLocation.getLatitude(),app.request.driverLocation.getLongitude()),18));
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

    private void getCurrentRequest() {
        final DocumentReference docRef = db.collection("Requests").document(app.requestId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Test", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.e("Test", "Current data: " + snapshot.getData());
                    app.request = snapshot.toObject(OrderRequest.class);
                    if (app.request.status == OrderRequestStatus.REQUESTING) {
                        displayOrderRequest();
                    } else if (app.request.status == OrderRequestStatus.ACCEPTED) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("driverName",app.request.driverName);
                        db.collection("Orders").document(app.request.idOrder).set(data,SetOptions.merge());
                        tvOnTheWay.setTextColor(getResources().getColor(R.color.colorPrimary));
                        displayDriverInfo();
                        placeDriverMarkerOnMap();
                    } else if (app.request.status == OrderRequestStatus.CANCELED_BY_DRIVER) {
                                driverCancel();
                    } else if (app.request.status == OrderRequestStatus.FINISHED) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("trangThai","Finished");
                        db.collection("Orders").document(app.request.idOrder).set(data, SetOptions.merge());
                        tvOnTheWay.setTextColor(getResources().getColor(R.color.colorBlack));
                        tvOnTheWay.setText("Order is finished");
                    }
                } else {
                    Log.e("Test", "Current data: null");
                }
            }
        });
    }
    private void displayOrderRequest() {
        layoutRequesting.setVisibility(View.VISIBLE);
        layoutOnTheWay.setVisibility(View.GONE);
        tvDelivery.setText(app.request.userAddress);
        tvStore.setText(app.request.storeName);
        tvTotal.setText(app.request.total + " VND");
    }

    private void displayDriverInfo() {
        layoutRequesting.setVisibility(View.GONE);
        layoutOnTheWay.setVisibility(View.VISIBLE);
        tvDriverName.setText("Driver: " + app.request.driverName);
    }

    private void driverCancel()
    {
        tvOnTheWay.setTextColor(getResources().getColor(R.color.colorRed));
        tvOnTheWay.setText("Order was canceled by driver");
    }

    private void cancelBookingOrder()
    {
        db.collection("Orders").document(app.order.getId()).update("trangThai", "Cancelled").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                db.collection("Requests").document(app.request.id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });
    }
}
