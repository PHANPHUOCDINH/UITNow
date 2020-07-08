package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookingTableTrackingActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private TextView txtDay, txtHour, txtMinute, txtSecond;
    private TextView tvEventStart;
    private Handler handler;
    private Runnable runnable;
    private Booking booking;
    private TextView txtId,txtTrangThai,txtDate,txtResName,txtTime,txtNum,txtCusName,txtCusNum;
    private Button btnRebook,btnDel;
    LinearLayout layoutCountdown;
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
        if (event != null && MessageEvent.FROM_bookingFRAG_TO_bookingtrackingACT.equals(event.type)) {
            booking=event.booking;
            showBookingInfo();
            EventBus.getDefault().removeStickyEvent(event);
        }
    }
    private void showBookingInfo()
    {
        txtId.setText(booking.getId());
        int tt=Integer.parseInt(booking.getTrangThai());
        txtTrangThai.setText(tt==BookingRequestStatus.ACCEPTED?"Đã xác nhận":tt==BookingRequestStatus.FINISHED?"Đã kết thúc":tt==BookingRequestStatus.CANCELED_BY_USER?"Đã hủy":"Đang chờ xác nhận");
        txtDate.setText(booking.getTime());
        txtResName.setText(booking.getResName());
        txtTime.setText(booking.getDate());
        txtNum.setText(booking.getNumNguoiLon()+" Người lớn, "+booking.getNumTreEm()+" Trẻ em");
        txtCusName.setText(booking.getCusName());
        txtCusNum.setText(booking.getCusPhoneNum());
        if(Integer.parseInt(booking.getTrangThai())==BookingRequestStatus.REQUESTING)
        {
            tvEventStart.setVisibility(View.GONE);
            layoutCountdown.setVisibility(View.GONE);
        }
        if(Integer.parseInt(booking.getTrangThai())==BookingRequestStatus.ACCEPTED)
        {
            tvEventStart.setVisibility(View.VISIBLE);
            layoutCountdown.setVisibility(View.VISIBLE);
            btnDel.setVisibility(View.INVISIBLE);
            countDownStart();
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_tracking);
        db=FirebaseFirestore.getInstance();
        txtId=findViewById(R.id.txtId);
        txtTrangThai=findViewById(R.id.txtTrangThai);
        txtDate=findViewById(R.id.txtDate);
        txtResName=findViewById(R.id.txtResName);
        txtTime=findViewById(R.id.txtTime);
        txtNum=findViewById(R.id.txtNum);
        txtCusName=findViewById(R.id.txtCusName);
        txtCusNum=findViewById(R.id.txtCusNum);
        btnDel=findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBooking();
            }
        });
        btnRebook=findViewById(R.id.btnReBook);
        btnRebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reBooking();
            }
        });
        txtDay = (TextView) findViewById(R.id.txtDay);
        txtHour = (TextView) findViewById(R.id.txtHour);
        txtMinute = (TextView) findViewById(R.id.txtMinute);
        txtSecond = (TextView) findViewById(R.id.txtSecond);
        tvEventStart = (TextView) findViewById(R.id.tveventStart);
        layoutCountdown=findViewById(R.id.layoutCountdown);
    }
    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd-MM-yyyy");
                    // Please here set your event date//YYYY-MM-DD
                    Date futureDate = dateFormat.parse(booking.getDate());
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtDay.setText("" + String.format("%02d", days));
                        txtHour.setText("" + String.format("%02d", hours));
                        txtMinute.setText(""
                                + String.format("%02d", minutes));
                        txtSecond.setText(""
                                + String.format("%02d", seconds));
                    } else {
                        tvEventStart.setVisibility(View.VISIBLE);
                        tvEventStart.setText("Đã tới ngày đặt bàn");
                        textViewGone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    public void textViewGone() {
        findViewById(R.id.LinearLayout1).setVisibility(View.GONE);
        findViewById(R.id.LinearLayout2).setVisibility(View.GONE);
        findViewById(R.id.LinearLayout3).setVisibility(View.GONE);
        findViewById(R.id.LinearLayout4).setVisibility(View.GONE);
//        findViewById(R.id.textViewheader1).setVisibility(View.GONE);
//        findViewById(R.id.textViewheader2).setVisibility(View.GONE);
    }

    private void reBooking()
    {
        Intent intent = new Intent(this, ConfirmBookingActivity.class);
        startActivity(intent);
        EventBus.getDefault().postSticky(new MessageEvent(booking,MessageEvent.FROM_bookingtrackingACT_TO_confirmBooking));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void deleteBooking()
    {
        db.collection("Bookings").document(booking.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onBackPressed();
            }
        });
    }
}
