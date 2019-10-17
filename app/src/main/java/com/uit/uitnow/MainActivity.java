package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.uit.uitnow.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class MainActivity extends AppCompatActivity {
    //    Coordinat
    private TabLayout tabLayout;
    Toolbar toolbar;
    TextView toolBarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolBar);
        for(int i=0;i<toolbar.getChildCount();i++)
        {
            View child=toolbar.getChildAt(i);
            if(child instanceof TextView)
            {
                toolBarTextView=(TextView)child;
                break;
            }
        }
        toolBarTextView.setText("Đặt Món");
        // setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        createTabs();

    }


    private void createTabs() {
        TextView tabFoodOrder = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        tabFoodOrder.setText("Đặt món");
        tabFoodOrder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_foodorder, 0, 0);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setCustomView(tabFoodOrder);


        TextView tabTableOrder = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        tabTableOrder.setText("Đặt bàn");
        tabTableOrder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tableorder, 0, 0);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(1).setCustomView(tabTableOrder);

        TextView tabOrder = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        tabOrder.setText("Đơn");
        tabOrder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_order, 0, 0);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(2).setCustomView(tabOrder);

        TextView tabProfile = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        tabProfile.setText("Hồ sơ");
        tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_profile, 0, 0);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(3).setCustomView(tabProfile);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPos = tab.getPosition();
                switch (tabPos) {
                    case 0: // Home
                        toolBarTextView.setText("Đặt Món");
                   //     Đặt món
                        break;
                    case 1: // Order
                        toolBarTextView.setText("Đặt Bàn");
                     //  Đặt bàn
                        break;
                    case 2: // Profile
                        toolBarTextView.setText("Đơn đã đặt");
                       // Đơn đã đặt
                        break;
                    case 3:
                        toolBarTextView.setText("Thông tin cá nhân");
                        // TTCN
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
