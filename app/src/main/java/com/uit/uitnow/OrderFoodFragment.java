package com.uit.uitnow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class OrderFoodFragment extends Fragment implements StoreAdapter.StoreListener {
    App app;
    RecyclerView rvStores;
    StoreAdapter storeAdapter;
    ArrayList<Store> listStores=new ArrayList<>();
    SwipeRefreshLayout swipeStores;
    TextView tvMyAddress;
    AppCompatSpinner spinner;
    FirebaseFirestore db;
    Button btnTimKiem;
    EditText txtTimKiem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orderfood_fragment, container, false);
        tvMyAddress=view.findViewById(R.id.tvMyAddress);
        rvStores = view.findViewById(R.id.rvStores);
        swipeStores=view.findViewById(R.id.swipeStores);
        spinner=view.findViewById(R.id.spinnerDistrict);
        btnTimKiem=view.findViewById(R.id.btnTimKiem);
        txtTimKiem=view.findViewById(R.id.txtTimKiem);
        String[] districts={"Tất cả","Quận 1","Quận 3","Quận Tân Bình"};
        ArrayAdapter<String> adapterDistrict=new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,districts);
        spinner.setAdapter(adapterDistrict);
        swipeStores.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeStores.setRefreshing(false);
            }
        });
        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search=txtTimKiem.getText().toString();
                String district=spinner.getSelectedItem().toString();
                filter(search,district);
            }
        });
        showStores();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app=(App)getActivity().getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LocationServiceTask.isLocationServiceEnabled(getActivity())) { // 1
            if (PermissionTask.isLocationServiceAllowed(getActivity())) // 2
                getLastLocation(getActivity()); // 3
            else
                PermissionTask.requestLocationServicePermissions(getActivity()); // 4
        } else {
            LocationServiceTask.displayEnableLocationServiceDialog(getActivity()); // 5
        }
    }

    private void showStores()
    {
        listStores.clear();
        db= FirebaseFirestore.getInstance();
        db.collection("Stores").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Store s=document.toObject(Store.class);
                        listStores.add(s);
                    }
                    storeAdapter = new StoreAdapter(listStores,OrderFoodFragment.this);
                    rvStores.setAdapter(storeAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvStores.setLayoutManager(layoutManager);
                } else {
                    Log.d("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getLastLocation(Context context) {
        FusedLocationProviderClient locationClient =
                getFusedLocationProviderClient(context);
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null) {
                    //Log.e("Test", "Location Success " + String.valueOf(location.getLatitude()));
                    onLocationChanged(location);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Test","Location Failed");
                e.printStackTrace();
            }
        });
    }

    private void onLocationChanged(Location location) {
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
        app.location=geoPoint;
        String address=LocationServiceTask.getAddressFromLatLng(getActivity(),geoPoint);
        tvMyAddress.setText("Delivery to: "+ address);
        PrefUtil.savePref(getActivity(),"address",address);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionTask.LOCATION_SERVICE_REQUEST_CODE &&
                grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation(getActivity());
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStoreClick(Store store) {
        Intent intent = new Intent(getActivity(), StoreActivity.class);
        //intent.putExtra("RESTAURANT", restaurant);
        startActivity(intent);
        EventBus.getDefault().postSticky(new MessageEvent(store, com.uit.uitnow.MessageEvent.FROM_storeFRAG_TO_storeACT));
// getActivity().overridePendingTransition(R.anim.slide_in_right,
        //       R.anim.slide_out_left); // animation
    }

    private void filter(String search,String district)
    {

    }
}
