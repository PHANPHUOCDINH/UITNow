package com.uit.uitnow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderDetailDialogFragment extends DialogFragment implements View.OnClickListener {
    RecyclerView rvOrderDetail;
    OrderDetailAdapter adapter;
    ArrayList<ItemBasket> itemBaskets;
    public OrderDetailDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public OrderDetailDialogFragment(ArrayList<ItemBasket> items) {
        this.itemBaskets=items;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_dialog, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvOrderDetail=view.findViewById(R.id.rvOrderDetail);
        adapter=new OrderDetailAdapter(itemBaskets);
        rvOrderDetail.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(true);
        super.onResume();
    }
}
