package com.uit.uitnow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasketDialogFragment extends DialogFragment implements ItemBasketAdapter.OnItemQuantityListener, View.OnClickListener {
    private TextView tvTotal;
    private RecyclerView rvItems;
    private Basket basket;
    private ItemBasketAdapter adapter;
    private Button btnPlaceOrder;
    App app;
    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.giohang, container, false);
//        rvGiohang=view.findViewById(R.id.rvGiohang);
//        adapter=new FoodBasketAdapter(arrayBasket,this);
//        rvGiohang.setAdapter(adapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        rvGiohang.setLayoutManager(layoutManager);
//        return view;
        return inflater.inflate(R.layout.basket_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvTotal.setText("Tổng cộng: " + basket.getTotalPrice());
        rvItems = view.findViewById(R.id.rvFoods);
        adapter = new ItemBasketAdapter(new ArrayList<>(basket.items.values()), this);
        rvItems.setAdapter(adapter); // 1
        db=FirebaseFirestore.getInstance();
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(this);

        app = (App) getActivity().getApplication();
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(true);
        super.onResume();
    }

    @SuppressLint("ValidFragment")
    public BasketDialogFragment(Basket basket) {
        this.basket=basket;
    }

    public BasketDialogFragment()
    {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlaceOrder) {
//            if (basket.getTotalItem() > 0) {
//                LatLng latLng = new LatLng(Float.parseFloat(((StoreActivity)
//                        getActivity()).store.lat), Float.parseFloat(((StoreActivity)
//                        getActivity()).store.lng));
//
//// 1
//                Log.e("CSC","Store Location: "+latLng.latitude);
//                Log.e("CSC","Store Location: "+latLng.longitude);
//                long l=System.currentTimeMillis();
//                app.order = new Order(String.valueOf(l),PrefUtil.loadPref(getActivity(),"id"),app.basket, app.currentAddress, ((StoreActivity) getActivity()).store.name, app.currentLocation, latLng);
//                Map<String, Object> data = new HashMap<>();
//                data.put("id", String.valueOf(l));
//                data.put("idKhachHang", PrefUtil.loadPref(getActivity(),"id"));
//                data.put("storeName", ((StoreActivity) getActivity()).store.name);
//                data.put("deliveryAddress", app.currentAddress);
//                data.put("tongGia", String.valueOf(app.basket.totalPrice));
//                data.put("trangThai", "Booking");
//                db.collection("orders").document(String.valueOf(l)).set(data, SetOptions.merge());
//// 2
//                ((StoreActivity) getActivity()).openOrderTrackingActivity(latLng,app.currentLocation);
//                getDialog().dismiss();
//            } else {
//                getDialog().dismiss();
//            }
        }
    }


    @Override
    public void onChangeItemQuantity(ItemBasket item) {
        if (item.quantity > 0) {
            app.basket.addItem(item);
        }
        app.basket.calculateBasket();
        tvTotal.setText("Tổng cộng: "+String.valueOf(app.basket.getTotalPrice()));
        ((StoreActivity) getActivity()).updateBasket();
    }
}