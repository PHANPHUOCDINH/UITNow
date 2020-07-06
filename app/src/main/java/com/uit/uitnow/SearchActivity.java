package com.uit.uitnow;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.lucasr.twowayview.TwoWayView;

public class SearchActivity extends AppCompatActivity {
    TwoWayView twoWayView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
