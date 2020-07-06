package com.uit.uitnow;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.lucasr.twowayview.TwoWayView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ConfirmBookingActivity extends AppCompatActivity {
    Booking booking;
    ImageView imageView;
    EditText txtName;
    EditText txtPhoneNum;
    TextView txtResName;
    TextView txtResAddress,txtQuantity1,txtQuantity2,txtHour,txtDate,txtGhiChu;
    ImageView btnSub1,btnSub2,btnAdd1,btnAdd2;
    TwoWayView twoWayView;
    Button btnXacNhan;
    FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmbooking_activity);
        txtGhiChu=findViewById(R.id.txtGhiChu);
        txtDate=findViewById(R.id.txtDate);
        txtHour=findViewById(R.id.txtHour);
        twoWayView=findViewById(R.id.listTypes);
        imageView=findViewById(R.id.ivImage);
        txtResName=findViewById(R.id.resName);
        txtResAddress=findViewById(R.id.resAddress);
        txtName=findViewById(R.id.cusName);
        txtPhoneNum=findViewById(R.id.cusPhoneNum);
        txtQuantity1=findViewById(R.id.tvQuantity1);
        txtQuantity2=findViewById(R.id.tvQuantity2);
        btnSub1=findViewById(R.id.btnSubtract1);
        btnSub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=Integer.parseInt(txtQuantity1.getText().toString());
                if(i>1)
                    txtQuantity1.setText(String.valueOf(i-1));
            }
        });
        btnSub2=findViewById(R.id.btnSubtract2);
        btnSub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=Integer.parseInt(txtQuantity2.getText().toString());
                if(i>1)
                    txtQuantity2.setText(String.valueOf(i-1));
            }
        });
        btnAdd1=findViewById(R.id.btnAdd1);
        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtQuantity1.setText(String.valueOf(Integer.parseInt(txtQuantity1.getText().toString())+1));
            }
        });
        btnAdd2=findViewById(R.id.btnAdd2);
        btnAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtQuantity2.setText(String.valueOf(Integer.parseInt(txtQuantity2.getText().toString())+1));
            }
        });
        ArrayList<String> listTypes=new ArrayList<>();
        listTypes.add("Sinh nhật");
        listTypes.add("Hẹn hò");
        listTypes.add("Kỉ niệm");
        listTypes.add("Công việc");
        listTypes.add("Họp mặt");
        ListTypeViewAdapter adapter=new ListTypeViewAdapter(this,null,listTypes);
        twoWayView.setAdapter(adapter);
        btnXacNhan=findViewById(R.id.btnXacNhan);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xacNhan();
            }
        });
        db = FirebaseFirestore.getInstance();
    }

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
        if (event != null && (MessageEvent.FROM_resACT_TO_confirmACT.equals(event.type) || MessageEvent.FROM_bookingtrackingACT_TO_confirmBooking.equals(event.type))) {
            booking=event.booking;
            showBookingInfo();
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    protected void showBookingInfo()
    {
        txtResName.setText(booking.getResName());
        txtResAddress.setText(booking.getResAddress());
        txtName.setText(booking.getCusName());
        txtPhoneNum.setText(booking.getCusPhoneNum());
        txtHour.setText(booking.getTime());
        txtDate.setText(booking.getDate());
    }

    protected void xacNhan()
    {
        long l = System.currentTimeMillis();
        booking.setId(String.valueOf(l));
        booking.setCusName(txtName.getText().toString());
        booking.setPhoneNum(txtPhoneNum.getText().toString());
        booking.setGhiChu(txtGhiChu.getText().toString());
        booking.setNumNguoiLon(Integer.parseInt(txtQuantity2.getText().toString()));
        booking.setNumTreEm(Integer.parseInt(txtQuantity1.getText().toString()));
        Map<String, Object> data = new HashMap<>();
        data.put("id",booking.getId());
        data.put("idKhachHang",booking.getIdKhachHang());
        data.put("idBooking",booking.getIdBooking());
        data.put("resName",booking.getResName());
        data.put("time",(new SimpleDateFormat("dd-MM-yyy")).format(Calendar.getInstance().getTime()));
        data.put("date",booking.getDate());
        data.put("resAddress",booking.getResAddress());
        data.put("numNguoiLon",booking.getNumNguoiLon());
        data.put("numTreEm",booking.getNumTreEm());
        data.put("ghiChu",booking.getGhiChu());
        data.put("cusName",booking.getCusName());
        data.put("cusPhoneNum",booking.getCusPhoneNum());
        data.put("trangThai",String.valueOf(BookingRequestStatus.REQUESTING));
        db.collection("Bookings").document(booking.getId()).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onBackPressed();
            }
        });
    }
}
