package com.uit.uitnow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookTableFragment extends Fragment {
    RecyclerView rvRests;
    RestaurantAdapter resAdapter;
    ArrayList<Restaurant> listRests=new ArrayList<>();
    SwipeRefreshLayout swipeRests;
    AppCompatSpinner spinner;
    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booktable_fragment, container, false);
        rvRests = view.findViewById(R.id.rvRestaurants);
        swipeRests=view.findViewById(R.id.swipeRestaurants);
        spinner=view.findViewById(R.id.spinnerDistrict);
        String[] districts={"Quận 1","Quận 2","Quận 3","Quận 4","Quận 5"};
        ArrayAdapter<String> adapterDistrict=new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,districts);
        spinner.setAdapter(adapterDistrict);
        swipeRests.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRests.setRefreshing(false);
            }
        });
        showRestaurants();
        return view;
    }

    private void showRestaurants()
    {
        listRests.clear();
        db=FirebaseFirestore.getInstance();
        db.collection("Restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Restaurant r=document.toObject(Restaurant.class);
                        listRests.add(r);
                    }
                    resAdapter = new RestaurantAdapter(listRests);
                    rvRests.setAdapter(resAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvRests.setLayoutManager(layoutManager);
                } else {
                    Log.d("CSC", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
