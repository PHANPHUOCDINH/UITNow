package com.uit.uitnow;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BasketDialogFragment extends DialogFragment implements ItemBasketAdapter.OnItemQuantityListener, View.OnClickListener {
    private TextView tvTotal;
    private RecyclerView rvItems;
    private ItemBasketAdapter adapter;
    private Button btnPlaceOrder, btnCheckCode;
    ArrayList data;
    EditText txtCode;
    App app;
    FirebaseFirestore db;
    TextView txtSeeVouchers, txtTienGiam;
    boolean isVoucherUsed = false;
    ClipboardManager clipboardManager;

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
        app = (App) getActivity().getApplication();
        tvTotal = view.findViewById(R.id.tvTotal);
        tvTotal.setText("Tổng cộng: " + app.basket.getTotalPrice());
        rvItems = view.findViewById(R.id.rvFoods);
        data = new ArrayList<>(app.basket.items.values());
        adapter = new ItemBasketAdapter(data, this);
        rvItems.setAdapter(adapter); // 1
        db = FirebaseFirestore.getInstance();
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(this);
        txtCode = view.findViewById(R.id.txtCode);
        btnCheckCode = view.findViewById(R.id.btnCheckCode);
        txtSeeVouchers = view.findViewById(R.id.txtSeeVouchers);
        btnCheckCode.setOnClickListener(this);
        txtSeeVouchers.setOnClickListener(this);
        txtTienGiam = view.findViewById(R.id.txtTienGiam);
        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(true);
        super.onResume();
    }

    public BasketDialogFragment() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlaceOrder) {
            if (app.basket.getTotalItem() > 0) {
                GeoPoint geoPoint = new GeoPoint(Double.parseDouble(((StoreActivity)
                        getActivity()).store.lat), Double.parseDouble(((StoreActivity)
                        getActivity()).store.lng));

// 1
                long l = System.currentTimeMillis();
                app.order = new Order(String.valueOf(l), PrefUtil.loadPref(getActivity(), "id"), app.basket, PrefUtil.loadPref(getActivity(), "address"), ((StoreActivity) getActivity()).store.name, app.location, geoPoint);
                Map<String, Object> data = new HashMap<>();
                data.put("id", String.valueOf(l));
                data.put("storeAddress", ((StoreActivity) getActivity()).store.getAddress());
                data.put("idKhachHang", PrefUtil.loadPref(getActivity(), "id"));
                data.put("storeName", ((StoreActivity) getActivity()).store.getName());
                data.put("deliveryAddress", PrefUtil.loadPref(getActivity(), "address"));
                data.put("tongGia", String.valueOf(app.basket.totalPrice));
                data.put("trangThai", app.order.trangThai);
                data.put("deliveryLocation", app.order.deliveryLocation);
                data.put("storeLocation", app.order.storeLocation);
                db.collection("Orders").document(String.valueOf(l)).set(data, SetOptions.merge());
// 2
//                for(int i=0;i<app.basket.getItems().size();i++)
//                {
//                    dataBasket.put("name","")
//                }
                for (Map.Entry<String, ItemBasket> entry : app.basket.getItems().entrySet()) {
                    ItemBasket item = entry.getValue();
                    Map<String, Object> itemData = new HashMap<>();
                    itemData.put("name", item.getName());
                    itemData.put("price", item.getPrice());
                    itemData.put("quantity", item.getQuantity());
                    itemData.put("ghichu", item.getGhichu());
                    itemData.put("image", item.getImage());
                    db.collection("Orders").document(String.valueOf(l)).collection("basket").document(item.getId()).set(itemData, SetOptions.merge());
                }
                ((StoreActivity) getActivity()).requestOrder(geoPoint);
                getDialog().dismiss();
            } else {
                getDialog().dismiss();
            }
        } else {
            if (v.getId() == R.id.btnCheckCode) {
                if (!isVoucherUsed) {
                    String code = txtCode.getText().toString().trim();
                    checkCode(code);
                } else {
                    Toast.makeText(getActivity(), "Bạn đã sử dụng voucher", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (v.getId() == R.id.txtSeeVouchers) {
                    VoucherDialogFragment dialog = new VoucherDialogFragment(this);
                    dialog.show(getActivity().getSupportFragmentManager(), "voucher_dialog");
                } else {

                }
            }
        }
    }


    @Override
    public void onChangeItemQuantity(ItemBasket item) {
        if (item.quantity > 0) {
            app.basket.addItem(item);
        } else {
            app.basket.removeItem(item);
            dismiss();

        }
        txtTienGiam.setVisibility(View.GONE);
        isVoucherUsed = false;
        app.basket.calculateBasket();
        tvTotal.setText("Tổng cộng: " + String.valueOf(app.basket.getTotalPrice()));
        ((StoreActivity) getActivity()).updateBasket();
    }

    private void checkCode(String code) {
        db.collection("Vouchers").whereEqualTo("id", code).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty())
                        Toast.makeText(getActivity(), "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
                    else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Voucher v = document.toObject(Voucher.class);
                            int tienGiam = (app.basket.totalPrice * v.value) / 100;
                            app.basket.totalPrice -= tienGiam;
                            isVoucherUsed = true;
                            tvTotal.setText("Tổng cộng: " + app.basket.getTotalPrice());
                            txtTienGiam.setText("(Đã giảm " + tienGiam + " VND)");
                            txtTienGiam.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Log.e("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void setPasteCode() {
        ClipData clipData = clipboardManager.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        txtCode.setText(item.getText().toString());
        btnCheckCode.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VoucherDialogFragment.VOUCHER_COPY_CODE_REQUEST) {
            String code = data.getStringExtra("code");
            txtCode.setText(code);
            btnCheckCode.setEnabled(true);
        }
    }


}
