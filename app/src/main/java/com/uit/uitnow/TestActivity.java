package com.uit.uitnow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    TextView txtDate;
    Button btnReset;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
//        txtDate=findViewById(R.id.txtDate);
//        btnReset=findViewById(R.id.btnReset);
//        db.collection("Tests").document("Cgp6TmVK3Nl23o0UtEaJ").update("date",new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
//        txtDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar=Calendar.getInstance();
//                int year=calendar.get(Calendar.YEAR);
//                int month=calendar.get(Calendar.MONTH);
//                int day=calendar.get(Calendar.DAY_OF_MONTH);
//                final DatePickerDialog dialog= new DatePickerDialog(TestActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        txtDate.setText(dayOfMonth+"/"+month+"/"+year);
//                    }
//                },year,month,day);
//                dialog.show();
//            }
//        });
//        txtDate.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.toString().isEmpty())
//                    Toast.makeText(TestActivity.this,"Empty",Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(TestActivity.this,charSequence.toString(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        btnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                txtDate.setText("");
//            }
//        });
        db.collection("Vouchers").whereEqualTo("id","DISCOUNT20").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().isEmpty())
                        Toast.makeText(TestActivity.this,"Mã không hợp lệ",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(TestActivity.this,"Mã hợp lệ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.e("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }


}
