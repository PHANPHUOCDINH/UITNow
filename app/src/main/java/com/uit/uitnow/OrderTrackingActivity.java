package com.uit.uitnow;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    ImageButton btnCall,btnChat;
    static final int CALL_REQUEST=101;
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
        tvDelivery.setText(app.request.userAddress);
        tvStore.setText(app.request.storeName);
        tvTotal.setText(app.request.getTotal());
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
        btnCall=findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(OrderTrackingActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},CALL_REQUEST);
                    }
                    return;
                }
                String txtcall="tel:"+app.request.getDriverPhone();
                Intent callIntent=new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(txtcall));
                if(callIntent.resolveActivity(getPackageManager())!=null)
                {
                    startActivity(callIntent);
                }
            }
        });
        btnChat=findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChat();
            }
        });
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
        options.title(app.request.storeName);
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(store.getLatitude(),store.getLongitude()),14));
       // map.moveCamera(CameraUpdateFactory.newLatLngZoom(app.order.storeLocation, 20));
    }

    private void placeDriverMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(app.request.driverLocation.getLatitude(), app.request.driverLocation.getLongitude())).title(app.request.driverName);
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_motor)));
        options.title(app.request.driverName);
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(app.request.driverLocation.getLatitude(),app.request.driverLocation.getLongitude()),14));
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn muốn hủy đơn hàng?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.collection("Orders").document(app.request.idOrder).update("trangThai", "Cancelled").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        db.collection("Requests").document(app.request.id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        });
                    }
                });

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
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
                    OrderRequest tempRequest=app.request;
                    app.request = snapshot.toObject(OrderRequest.class);
                    if (app.request.status == OrderRequestStatus.REQUESTING) {
                        displayOrderRequest();
                    } else if (app.request.status == OrderRequestStatus.ACCEPTED) {
                        NotificationTask.createNotification(OrderTrackingActivity.this,(int)System.currentTimeMillis(),R.drawable.ic_notification,"Đơn hàng đã được nhận","Đơn hàng "+app.request.getIdOrder()+" đã được shipper "+app.request.getDriverName()+" nhận");
                        Map<String, Object> data = new HashMap<>();
                        data.put("driverName",app.request.driverName);
                        db.collection("Orders").document(app.request.idOrder).set(data,SetOptions.merge());
                        tvOnTheWay.setTextColor(getResources().getColor(R.color.colorPrimary));
                        displayDriverInfo();
                        placeDriverMarkerOnMap();
                    } else if (app.request.status == OrderRequestStatus.CANCELED_BY_DRIVER) {
                                driverCancel();
                    } else if (app.request.status == OrderRequestStatus.FINISHED) {
                        NotificationTask.createNotification(OrderTrackingActivity.this,(int)System.currentTimeMillis(),R.drawable.ic_notification,"Đơn hàng đã hoàn tất","Đơn hàng "+app.request.getIdOrder()+" đã được hoàn tất");
                        Map<String, Object> data = new HashMap<>();
                        data.put("trangThai","Finished");
                        db.collection("Orders").document(app.request.idOrder).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                db.collection("Requests").document(app.request.id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        tvOnTheWay.setTextColor(getResources().getColor(R.color.colorBlack));
                                        tvOnTheWay.setText("Order is finished");
                                        showRateDialog();
                                    }
                                });
                            }
                        });
                    }
                    else if(app.request.driverLocation.getLatitude()!=tempRequest.driverLocation.getLatitude()||app.request.driverLocation.getLongitude()!=tempRequest.driverLocation.getLongitude())
                    {
                        placeDriverMarkerOnMap();
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
        tvDriverName.setText("Shipper: " + app.request.driverName);
    }

    private void driverCancel()
    {
        tvOnTheWay.setTextColor(getResources().getColor(R.color.colorRed));
        tvOnTheWay.setText("Order was canceled by driver");
    }

    private void cancelBookingOrder()
    {
        db.collection("Orders").document(app.request.getIdOrder()).update("trangThai", "Cancelled").addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void showRateDialog()
    {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.rating_dialog);
        RatingBar ratingBar=dialog.findViewById(R.id.ratingBar);
        final TextView txtRatingDetail=dialog.findViewById(R.id.txtRateDetail);
        Button btnSubmitRate=dialog.findViewById(R.id.btnRate);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int)ratingBar.getRating())
                {
                    case 1:
                        txtRatingDetail.setText("Very bad");
                        break;
                    case 2:
                        txtRatingDetail.setText("Bad");
                        break;
                    case 3:
                        txtRatingDetail.setText("Good");
                        break;
                    case 4:
                        txtRatingDetail.setText("Great");
                        break;
                    case 5:
                        txtRatingDetail.setText("Awesome. I love it");
                        break;
                    default:
                        txtRatingDetail.setText("");
                }
            }
        });
        btnSubmitRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void openChat() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}
