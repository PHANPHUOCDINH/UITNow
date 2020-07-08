package com.uit.uitnow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class OrderFoodFragment extends Fragment implements LocationListener, ListStoreViewAdapter.OnStoreClickListener {
    List<String> images= Arrays.asList("https://firebasestorage.googleapis.com/v0/b/uitnow-8e7f3.appspot.com/o/stores%2Fic_thecoffeehouse.jpg?alt=media&token=15f3f27e-c8c4-4fa1-ac0d-8ca44809d43c","https://firebasestorage.googleapis.com/v0/b/uitnow-8e7f3.appspot.com/o/stores%2Fic_phuclong.png?alt=media&token=6580bb9e-77e4-4793-8b26-1dea6365f22c","https://firebasestorage.googleapis.com/v0/b/uitnow-8e7f3.appspot.com/o/stores%2Fic_toocha.jpg?alt=media&token=68461b64-64f9-467c-a54a-2a30656e3ffb","https://firebasestorage.googleapis.com/v0/b/uitnow-8e7f3.appspot.com/o/stores%2Fic_highland.jpeg?alt=media&token=e0e5322a-a9c6-4930-8442-58feeebaf343","https://firebasestorage.googleapis.com/v0/b/uitnow-8e7f3.appspot.com/o/stores%2Fic_thealley.png?alt=media&token=b4b931fc-0ebb-4d0d-a8d5-0f599e2ee644");
    List<String> infos=Arrays.asList("Shop 1","Shop 2","Shop 3","Shop 4","Shop 5");
    List<String> ids=Arrays.asList("azE2b6uJIWWc1fQZvqZ2","eMfwm1dcOxK1tZrL8pqh","jX2lsAwZYA4sADSqaov8","oMlwOsitRYeaE3duu6rJ","tgueDzlIGVRArsPUfuC5");
    FirebaseFirestore db;
    App app;
    SwipeRefreshLayout swipe;
    TextView tvMyAddress,txtTitle1,txtTitle2,txtTitle3,txtTitle4,txtSearch;
    TwoWayView list1,list2,list3,list4,list5;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orderfood_fragment, container, false);
        //twoWayView=view.findViewById(R.id.listSearchHistory);
        txtSearch=view.findViewById(R.id.txtSearch);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                //intent.putExtra("RESTAURANT", restaurant);
                startActivity(intent);
               // EventBus.getDefault().postSticky(new MessageEvent(s, com.uit.uitnow.MessageEvent.FROM_storeFRAG_TO_storeACT));
                getActivity().overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left); // animation
            }
        });
        tvMyAddress = view.findViewById(R.id.tvMyAddress);
        txtTitle1=view.findViewById(R.id.tvlay1);
        txtTitle2=view.findViewById(R.id.tvlay2);
        txtTitle3=view.findViewById(R.id.tvlay3);
        txtTitle4=view.findViewById(R.id.tvlay4);
        list1=view.findViewById(R.id.list1);
        list2=view.findViewById(R.id.list2);
        list3=view.findViewById(R.id.list3);
        list4=view.findViewById(R.id.list4);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app = (App) getActivity().getApplication();

        showLay1();
        showLay2();
        showLay3();
        showLay4();
//        txtTimKiem.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(app.searchHistory.size()>0) {
//                    ListSearchHistoryAdapter mAdapter = new ListSearchHistoryAdapter(app.searchHistory, OrderFoodFragment.this);
//                    twoWayView.setAdapter(mAdapter);
//                    twoWayView.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        txtTimKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                app.searchHistory.add(newText);
//                storeAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
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

    private void showLay1() {
        txtTitle1.setText("Recent Visits");
        ListStoreViewAdapter mAdapter=new ListStoreViewAdapter(getContext(),images,infos,ids,this);
        list1.setAdapter(mAdapter);
    }

    private void showLay2() {
        txtTitle2.setText("Top Sell");
        ListStoreViewAdapter mAdapter=new ListStoreViewAdapter(getContext(),images,infos,ids,this);
        list2.setAdapter(mAdapter);
    }

    private void showLay3() {
        txtTitle3.setText("Guess You Like");
        ListStoreViewAdapter mAdapter=new ListStoreViewAdapter(getContext(),images,infos,ids,this);
        list3.setAdapter(mAdapter);
    }

    private void showLay4()
    {
        txtTitle4.setText("Trending Now");
        ListStoreViewAdapter mAdapter=new ListStoreViewAdapter(getContext(),images,infos,ids,this);
        list4.setAdapter(mAdapter);
    }

    private void getLastLocation(Context context) {
        FusedLocationProviderClient locationClient =
                getFusedLocationProviderClient(context);
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.e("Test", "Location Success " + String.valueOf(location.getLatitude()));
                    onLocationChanged(location);
                } else
                {
                    Log.e("Test", "Location null ");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Test", "Location Failed");
                e.printStackTrace();
            }
        });
    }

    public void onLocationChanged(Location location) {
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
        app.location=geoPoint;
        String address=LocationServiceTask.getAddressFromLatLng(getActivity(),geoPoint);
        tvMyAddress.setText("Delivery to: "+ address);
        PrefUtil.savePref(getActivity(),"address",address);

            app.user.setAddress(address);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
    public void onStoreClick(String s) {
        db=FirebaseFirestore.getInstance();
        db.collection("Stores").whereEqualTo("id",s).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Store s = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        s = document.toObject(Store.class);
                    }
                    Intent intent = new Intent(getActivity(), StoreActivity.class);
                    //intent.putExtra("RESTAURANT", restaurant);
                    startActivity(intent);
                    EventBus.getDefault().postSticky(new MessageEvent(s, com.uit.uitnow.MessageEvent.FROM_storeFRAG_TO_storeACT));
                    getActivity().overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left); // animation
                }
            }
        });
    }


//    @Override
//    public void onClick(String search) {
//        twoWayView.setVisibility(View.GONE);
//        txtTimKiem.setQuery(search,true);
//    }
}
