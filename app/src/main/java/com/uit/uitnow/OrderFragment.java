package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderFragment extends Fragment implements OrderAdapter.OrderListener,SwipeRefreshLayout.OnRefreshListener {
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
        swipeOrders.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        idUser=PrefUtil.loadPref(getActivity(),"id");
        db=FirebaseFirestore.getInstance();
        app=(App)getActivity().getApplication();
        showOrders();
        //   getActivity().getActionBar().setTitle("Đơn Hàng");
    }

    @Override
    public void onCancelBooking(final Order order) {
//        db.collection("Orders").document(order.id).update("trangThai", "Cancelled").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                swipeOrders.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeOrders.setRefreshing(true);
//                        showOrders();
//                    }
//                });
//            }
//        });
        db.collection("Orders").document(order.getId()).update("trangThai", "Cancelled").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                db.collection("Requests").whereEqualTo("idOrder",order.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            OrderRequest request=null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                request = document.toObject(OrderRequest.class);
                            }
                            db.collection("Requests").document(request.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    swipeOrders.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeOrders.setRefreshing(true);
                                            showOrders();
                                        }
                                    });
                                }
                            });
                        }
                        else {
                            Log.d("Test", "Error getting documents: ", task.getException());
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onXemChiTiet(Order order) {
        getItemFromOrder(order);
    }

    @Override
    public void onReOrder(final Order order) {
        db.collection("Orders").document(order.getId()).update("trangThai","Booking").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                app.request = new OrderRequest();
                app.request.userId = app.user.id;
                app.request.userName = app.user.name;
                app.request.userAddress = app.user.address;
                app.request.userLocation=app.location;
                app.request.storeName = order.storeName;
                app.request.storeAddress = order.getStoreAddress();
                app.request.storeLocation=order.storeLocation;
                app.request.total = order.getTongGia()+ " VND";
                app.request.idOrder=order.getId();
                app.request.userPhone=app.user.phone;
                db.collection("Requests").add(app.request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        app.requestId = documentReference.getId();
                        Log.e("Test:",   " request id: " + app.requestId);
                        app.request.id = documentReference.getId();
                        PrefUtil.savePref(getActivity(), "request_id", app.requestId);
                        updateRequestId();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        });
    }

    private void showOrders()
    {
        db.collection("Orders").whereEqualTo("idKhachHang",idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    orders.clear();
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
                    swipeOrders.setRefreshing(false);
                } else {
                    Log.e("Test", "Error getting documents: ", task.getException());
                }
            }
        });
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

    @Override
    public void onRefresh() {
        showOrders();
    }

    private void updateRequestId() {
        db.collection("Requests").document(app.requestId).set(app.request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                        openOrderTrackingActivity();
            }
        });
    }

    public void openOrderTrackingActivity() {
        Intent intent = new Intent(getActivity(), OrderTrackingActivity.class);
        startActivity(intent);
        EventBus.getDefault().postSticky(new MessageEvent(app.request.storeLocation,MessageEvent.FROM_storeACT_TO_trackingACT));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onResume() {
        super.onResume();
        showOrders();
    }
}
