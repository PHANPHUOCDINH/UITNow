package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity implements View.OnClickListener,ItemAdapter.OnItemClickListener {
    TextView tvName, tvAddress, tvOpenHours, tvTotalPrices, tvTotalItems;
    ImageView ivCover;
    View layoutViewBasket;
    RecyclerView rvDrinks;
    ItemAdapter itemAdapter;
    Store store;
    App app;
    FirebaseFirestore db;
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
        if (event != null && MessageEvent.FROM_storeFRAG_TO_storeACT.equals(event.type)) {
            store=event.store;
            store.menu=new ArrayList<>();
            app.basket = new Basket();
            displayRestaurantInfo();
            showMenu(store.id);
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        db=FirebaseFirestore.getInstance();
        app= (App) getApplication();
//        listRestaurant=Restaurant.getMockData();
        //  restaurant = new Restaurant("7 Eleven", R.drawable.ic_seveneleven, R.drawable.cover_menu_1, "82 Nguyen Thi Minh Khai, Phuong 6, Quan 3", "6:00 - 23:00");
        rvDrinks = findViewById(R.id.rvFoods);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvDrinks.setLayoutManager(layoutManager);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvOpenHours = findViewById(R.id.tvOpenHours);
        ivCover = findViewById(R.id.ivCover);
        tvTotalPrices = findViewById(R.id.tvTotalPrices);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        layoutViewBasket = findViewById(R.id.layoutViewBasket);
        layoutViewBasket.setOnClickListener(this);
    }

    private void displayRestaurantInfo() {
        tvName.setText(store.name);
        tvAddress.setText(store.address);
        tvOpenHours.setText(store.getOpenHours());
        Picasso.get().load(store.getLogoUrl()).into(ivCover);
    }
    private void showMenu(String id)
    {
        db.collection("Stores").document(id).collection("menu").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item=document.toObject(Item.class);
                                store.menu.add(item);
                                Log.d("Test", document.getId() + " => " + document.getData());
                            }
                            Log.e("Test","Number of stores: "+store.menu.size());
                            itemAdapter = new ItemAdapter(store.menu,StoreActivity.this);
                            rvDrinks.setAdapter(itemAdapter);
                      //      updateBasket();
                        } else {
                            Log.d("Test", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.layoutViewBasket)
        {
            if (app.basket.getTotalItem() > 0) {
                BasketDialogFragment dialog = new BasketDialogFragment(app.basket);
                dialog.show(getSupportFragmentManager(), "basket_dialog");
            } else {
                Toast.makeText(this, "Giỏ hàng đang trống",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(Item item) {
        ItemBasket itemBasket = app.basket.getItem(item.id); // 1
        if (itemBasket == null) // 2
            itemBasket = new ItemBasket(item, 1, item.price);

        AddToBasketDialogFragment dialog = new AddToBasketDialogFragment(itemBasket);
        dialog.show(getSupportFragmentManager(), "add_to_basket_dialog"); // 3
    }

    public void updateBasket() {
        app.basket.calculateBasket();
        tvTotalPrices.setText(app.basket.getTotalPrice());
        tvTotalItems.setText(app.basket.getTotalItem() + "");
    }

    public void openOrderTrackingActivity(LatLng store) {
        Intent intent = new Intent(this, OrderTrackingActivity.class);
        startActivity(intent);
        EventBus.getDefault().postSticky(new MessageEvent(store,MessageEvent.FROM_storeACT_TO_trackingACT));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
