package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class BookingFragment extends Fragment implements BookingAdapter.OnBookingHistoryClickListener {
    RecyclerView rvBookings;
    BookingAdapter adapter;
    ArrayList<Booking> bookings=new ArrayList<>();
    SwipeRefreshLayout swipeBookings;
    FirebaseFirestore db;
    App app;
    String idUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_fragment, container, false);
        rvBookings=view.findViewById(R.id.rvBookings);
        swipeBookings=view.findViewById(R.id.swipeBookings);
        swipeBookings.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showBookings();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showBookings();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        idUser=PrefUtil.loadPref(getActivity(),"id");
        db=FirebaseFirestore.getInstance();
        app=(App)getActivity().getApplication();
        showBookings();
    }

    private void showBookings()
    {
        db.collection("Bookings").whereEqualTo("idKhachHang",idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    bookings.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Booking b=document.toObject(Booking.class);
                        bookings.add(b);
                    }
                    DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
                    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_1));
                    rvBookings.addItemDecoration(dividerItemDecoration);
                    adapter=new BookingAdapter(bookings,BookingFragment.this);
                    rvBookings.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvBookings.setLayoutManager(layoutManager);
                    swipeBookings.setRefreshing(false);
                } else {
                    Log.e("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onClick(Booking booking) {
        Intent intent = new Intent(getActivity(), BookingTableTrackingActivity.class);
        startActivity(intent);
        EventBus.getDefault().postSticky(new MessageEvent(booking, MessageEvent.FROM_bookingFRAG_TO_bookingtrackingACT));
        getActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }
}
