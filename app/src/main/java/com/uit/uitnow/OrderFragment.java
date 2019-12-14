package com.uit.uitnow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderFragment extends Fragment implements OrderAdapter.OrderListener {
    RecyclerView rvOrders;
    OrderAdapter adapter;
    ArrayList<Order> orders=new ArrayList<>();
    ArrayList<ItemBasket> arrayList=new ArrayList<>();
    SwipeRefreshLayout swipeOrders;
    FirebaseFirestore db;
    App app;
    String idUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        rvOrders=view.findViewById(R.id.rvOrders);
        swipeOrders=view.findViewById(R.id.swipeOrders);
        swipeOrders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrders();
                swipeOrders.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getRequest();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        idUser=PrefUtil.loadPref(getActivity(),"id");
        db=FirebaseFirestore.getInstance();
        app=(App)getActivity().getApplication();
        getOrders();
        //   getActivity().getActionBar().setTitle("Đơn Hàng");
    }

    @Override
    public void onCancelBooking(Order order) {
        Map<String, Object> data = new HashMap<>();
        data.put("trangThai", "Cancel");
        DocumentReference washingtonRef = db.collection("orders").document(order.id);
// cập nhật field “capital” của document “DC”
        washingtonRef
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"Đã hủy",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                    }
                });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onXemChiTiet(Order order) {
        getItemFromOrder(order);
    }

    private void getOrders()
    {
        orders.clear();
        db.collection("Orders").whereEqualTo("idKhachHang",idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Order o=document.toObject(Order.class);
                        Log.e("Test","Order "+o.id);
                        orders.add(o);
                    }
                    Log.e("Test","Number of order: "+String.valueOf(orders.size()));
                    adapter=new OrderAdapter(orders,OrderFragment.this,getActivity());
                    rvOrders.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvOrders.setLayoutManager(layoutManager);
                } else {
                    Log.e("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getRequest()
    {

    }

    private void getItemFromOrder(Order order)
    {
        arrayList.clear();
        db.collection("Orders").document(order.getId()).collection("basket").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ItemBasket item=document.toObject(ItemBasket.class);
                        Log.e("Test","Order "+item.name);
                        arrayList.add(item);
                    }
                    OrderDetailDialogFragment dialog = new OrderDetailDialogFragment(arrayList);
                    dialog.show(getActivity().getSupportFragmentManager(), "orderdetail_dialog");
                } else {
                    Log.e("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
