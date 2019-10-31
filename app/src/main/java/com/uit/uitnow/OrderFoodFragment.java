package com.uit.uitnow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class OrderFoodFragment extends Fragment {
    RecyclerView rvStores;
    StoreAdapter storeAdapter;
    ArrayList<Store> listStores;
    SwipeRefreshLayout swipeStores;
    TextView tvMyAddress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orderfood_fragment, container, false);
        tvMyAddress=view.findViewById(R.id.tvMyAddress);
        rvStores = view.findViewById(R.id.rvStores);
        swipeStores=view.findViewById(R.id.swipeStores);
        swipeStores.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeStores.setRefreshing(false);
            }
        });
        getStores();
        return view;
    }

    private void getStores()
    {
        // kết nối firebase lấy dữ liệu
        storeAdapter = new StoreAdapter(listStores);
        rvStores.setAdapter(storeAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvStores.setLayoutManager(layoutManager);
    }

}
