package com.uit.uitnow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class BookTableFragment extends Fragment implements RestaurantAdapter.OnRestaurantClickListener {
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
        final String[] districts={"All","Q.1","Q.2","Q.3","Q.Phú Nhuận","Q.Gò Vấp"};
        ArrayAdapter<String> adapterDistrict=new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,districts);
        spinner.setAdapter(adapterDistrict);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                showRestaurants(districts[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        swipeRests.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRests.setRefreshing(false);
            }
        });
        showRestaurants("All");
        return view;
    }

    private void showRestaurants(String district)
    {
        listRests.clear();
        db=FirebaseFirestore.getInstance();
        if(district.equals("All"))
        {
            db.collection("Restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Restaurant r = document.toObject(Restaurant.class);
                            listRests.add(r);
                        }
                        resAdapter = new RestaurantAdapter(listRests, BookTableFragment.this);
                        rvRests.setAdapter(resAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rvRests.setLayoutManager(layoutManager);
                    } else {
                        Log.d("Test", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else {
            db.collection("Restaurants").whereEqualTo("district",district).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Restaurant r = document.toObject(Restaurant.class);
                            listRests.add(r);
                        }
                        resAdapter = new RestaurantAdapter(listRests, BookTableFragment.this);
                        rvRests.setAdapter(resAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rvRests.setLayoutManager(layoutManager);
                    } else {
                        Log.d("Test", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    @Override
    public void onRestaurantClick(Restaurant res) {
        RestaurantActivity restaurantActivity = new RestaurantActivity();
        restaurantActivity.setPhone(res.getPhone());
        restaurantActivity.show(getFragmentManager(), "book restaurant");
    }
}
