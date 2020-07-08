package com.uit.uitnow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchResultFragment  extends Fragment implements StoreAdapter.StoreListener {
    ArrayList<Store> listStores;
    StoreAdapter adapter;
    App app;
    RecyclerView rvResults;
    OnStoreClickListener mListener;
    TextView textView;
    public interface OnStoreClickListener{
        void onStoreClick(Store s);
    }
    public SearchResultFragment()
    {

    }
    public SearchResultFragment(ArrayList<Store> listStores,OnStoreClickListener mListener)
    {
        this.listStores=listStores;
        this.mListener=mListener;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_result, container, false);
        rvResults=view.findViewById(R.id.rvResults);
        textView=view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app=(App)getActivity().getApplication();
        showResults();
    }

    void showResults(){

                    DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
                    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_1));
                    adapter = new StoreAdapter(listStores, SearchResultFragment.this,getActivity());
                    rvResults.addItemDecoration(dividerItemDecoration);
                    rvResults.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvResults.setLayoutManager(layoutManager);
    }

    @Override
    public void onStoreClick(Store store) {
mListener.onStoreClick(store);
    }

}
