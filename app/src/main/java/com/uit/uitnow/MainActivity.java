package com.uit.uitnow;

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

public class MainActivity extends AppCompatActivity {
    //    Coordinat
    private TabLayout tabLayout;
    Toolbar toolbar;
    TextView toolBarTextView;
    private ProfileFragment profileFragment;
    private OrderFragment orderFragment;
    private OrderFoodFragment orderFoodFragment;
    private BookTableFragment bookTableFragment;
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
        if(savedInstanceState==null) {
            profileFragment = new ProfileFragment();
            orderFragment = new OrderFragment();
            orderFoodFragment = new OrderFoodFragment();
            bookTableFragment = new BookTableFragment();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.add(R.id.layoutContainer,orderFoodFragment,"Order Food");
            ft.commit();
        }
      //  Log.e("Test",PrefUtil.loadPref(this,"email")+PrefUtil.loadPref(this,"id")+PrefUtil.loadPref(this,"name")+PrefUtil.loadPref(this,"phone"));
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
                    case 0: // Order Food
                        toolBarTextView.setText("Đặt Món");
                        displayOrderFoodFragment();
                        break;
                    case 1: // Book Table
                        toolBarTextView.setText("Đặt Bàn");
                        displayBookTableFragment();
                        break;
                    case 2: // Order
                        toolBarTextView.setText("Đơn đã đặt");
                        displayOrderFragment();
                        break;
                    case 3: // Profile
                        toolBarTextView.setText("Thông tin cá nhân");
                        displayProfileFragment();
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

    protected void displayOrderFoodFragment()
    {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(orderFoodFragment.isAdded())
        {
            ft.show(orderFoodFragment);
        }
        else
        {
            ft.add(R.id.layoutContainer,orderFoodFragment,"Order Food");
        }
        if(bookTableFragment.isAdded())
        {
            ft.hide(bookTableFragment);
        }
        if(orderFragment.isAdded())
        {
            ft.hide(orderFragment);
        }
        if(profileFragment.isAdded())
        {
            ft.hide(profileFragment);
        }
        ft.commit();
    }
    protected void displayBookTableFragment()
    {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(bookTableFragment.isAdded())
        {
            ft.show(bookTableFragment);
        }
        else
        {
            ft.add(R.id.layoutContainer,bookTableFragment,"Order Food");
        }
        if(orderFoodFragment.isAdded())
        {
            ft.hide(orderFoodFragment);
        }
        if(orderFragment.isAdded())
        {
            ft.hide(orderFragment);
        }
        if(profileFragment.isAdded())
        {
            ft.hide(profileFragment);
        }
        ft.commit();
    }
    protected void displayOrderFragment()
    {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(orderFragment.isAdded())
        {
            ft.show(orderFragment);
        }
        else
        {
            ft.add(R.id.layoutContainer,orderFragment,"Order Food");
        }
        if(bookTableFragment.isAdded())
        {
            ft.hide(bookTableFragment);
        }
        if(orderFoodFragment.isAdded())
        {
            ft.hide(orderFoodFragment);
        }
        if(profileFragment.isAdded())
        {
            ft.hide(profileFragment);
        }
        ft.commit();
    }
    protected void displayProfileFragment()
    {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(profileFragment.isAdded())
        {
            ft.show(profileFragment);
        }
        else
        {
            ft.add(R.id.layoutContainer,profileFragment,"Order Food");
        }
        if(bookTableFragment.isAdded())
        {
            ft.hide(bookTableFragment);
        }
        if(orderFragment.isAdded())
        {
            ft.hide(orderFragment);
        }
        if(orderFoodFragment.isAdded())
        {
            ft.hide(orderFoodFragment);
        }
        ft.commit();
    }
}
