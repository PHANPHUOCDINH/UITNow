package com.uit.uitnow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity implements BookingInfoAdapter.OnHourAdapterClickListener {
    App app;
    Restaurant res;
    TextView tvName, tvAddress, tvOpenHours;
    ImageView ivCover;
    FirebaseFirestore db;
    RecyclerView rvBookings;
    BookingInfoAdapter bookingInfoAdapter;
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event)
    {
        if (event != null && MessageEvent.FROM_resFRAG_TO_resACT.equals(event.type)) {
            res=event.restaurant;
            res.bookingInfos=new ArrayList<>();
            displayRestaurantInfo();
            showBookingInfos(res.id);
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        db= FirebaseFirestore.getInstance();
        app= (App) getApplication();
//        listRestaurant=Restaurant.getMockData();
        //  restaurant = new Restaurant("7 Eleven", R.drawable.ic_seveneleven, R.drawable.cover_menu_1, "82 Nguyen Thi Minh Khai, Phuong 6, Quan 3", "6:00 - 23:00");
//        rvDrinks = findViewById(R.id.rvFoods);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        rvDrinks.setLayoutManager(layoutManager);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvOpenHours = findViewById(R.id.tvOpenHours);
        ivCover = findViewById(R.id.ivCover);
        rvBookings=findViewById(R.id.rvBookings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvBookings.setLayoutManager(layoutManager);
    }
    protected void displayRestaurantInfo()
    {
        tvName.setText(res.name);
        tvAddress.setText(res.address);
        tvOpenHours.setText(res.getOpenHours());
        Picasso.get().load(res.getLogoUrl()).into(ivCover);
    }

    protected void showBookingInfos(String resId)
    {
        db.collection("Restaurants").document(resId).collection("bookingmenu").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BookingInfo item=document.toObject(BookingInfo.class);
                                res.bookingInfos.add(item);
                                Log.d("Test", document.getId() + " => " + document.getData());
                            }
                            Log.e("Test","Number of bookings: "+res.bookingInfos.size());
                            DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(RestaurantActivity.this,DividerItemDecoration.VERTICAL);
                            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_1));
                            bookingInfoAdapter = new BookingInfoAdapter(res.bookingInfos,RestaurantActivity.this,RestaurantActivity.this);
                            rvBookings.addItemDecoration(dividerItemDecoration);
                            rvBookings.setAdapter(bookingInfoAdapter);
                        } else {
                            Log.d("Test", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onHourClick(String hour,BookingInfo bookingInfo,Button txtDate) {
       // Log.e("Test",hour);
        if(!txtDate.getText().toString().isEmpty()) {

            Booking booking = new Booking();
            booking.setIdBooking(bookingInfo.getId());
            booking.setIdKhachHang(app.user.getId());
            booking.setCusName(app.user.getName());
            booking.setPhoneNum(app.user.getPhone());
            booking.setTime(hour);
            booking.setDate(txtDate.getText().toString());
            booking.setResName(res.getName());
            booking.setResAddress(res.getAddress());
            Intent intent = new Intent(this, ConfirmBookingActivity.class);
            //intent.putExtra("RESTAURANT", restaurant);
            startActivity(intent);
            EventBus.getDefault().postSticky(new MessageEvent(booking, MessageEvent.FROM_resACT_TO_confirmACT));
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
        else
        {
            Toast.makeText(this, "Ngày chưa chọn", Toast.LENGTH_SHORT).show();
        }
    }
}
