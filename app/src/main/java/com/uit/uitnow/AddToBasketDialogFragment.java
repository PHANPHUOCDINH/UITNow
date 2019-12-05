package com.uit.uitnow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddToBasketDialogFragment extends DialogFragment implements View.OnClickListener {
    TextView tvName, tvPrice, tvQuantity;
    Button btnBuy;
    ImageView btnSubtract, btnAdd;
    App app;
    ItemBasket item;

    public AddToBasketDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public AddToBasketDialogFragment(ItemBasket item) {
        this.item = item;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_to_basket_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tvName);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnSubtract = view.findViewById(R.id.btnSubtract);
        btnBuy = view.findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSubtract.setOnClickListener(this);

        tvName.setText(item.name); // 1
        tvPrice.setText(String.valueOf(item.getPrice())+" VND");
        updateStats();
        app = (App) getActivity().getApplication();
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().setCancelable(true);
        super.onResume();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnSubtract: // 2
                item.decrease();
                updateStats();
                break;
            case R.id.btnAdd: // 3
                item.increase();
                updateStats();
                break;
            case R.id.btnBuy: // 4
                if (item.quantity > 0) {
                    app.basket.addItem(item);
                }
                ((StoreActivity) getActivity()).updateBasket();
                getDialog().dismiss();
                break;
        }
    }

    private void updateStats() { // 5
        if (item.quantity > 0) {
            tvQuantity.setText(String.valueOf(item.quantity));
            String add = "Thêm vào giỏ hàng";
            btnBuy.setText(add + " : " + item.getSum());
        } else {
            btnBuy.setText("Trở về menu");
        }
    }
}
