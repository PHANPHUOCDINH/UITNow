package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchActivity extends AppCompatActivity implements SearchHistoryAdapter.OnSearchHistoryClickListener, SearchResultFragment.OnStoreClickListener {
    TwoWayView twoWayView;
    EditText txtSearch;
    ViewPager mPager;
    TabLayout tabLayout;
    RecyclerView rvSearchHistory;
    TextView textHis,textClr;
    ProgressBar progressBar;
    ArrayList<Store> listStores=new ArrayList<>();
    FirebaseFirestore db;
    ArrayList<String> str = new ArrayList<>();
    TextView txtFilter;
    SearchPagerAdapter spa;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txtFilter=findViewById(R.id.txtFilter);
        txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialogFragment dialog = new FilterDialogFragment();
                dialog.show(getSupportFragmentManager(), "filter_dialog");
            }
        });
        str.add("xxx");
        str.add("yyy");
        str.add("zzz");
        progressBar=findViewById(R.id.spinner);
        textHis=findViewById(R.id.txtHis);
        textClr=findViewById(R.id.txtClr);
        rvSearchHistory=findViewById(R.id.rvSearchHistory);
      //  twoWayView=findViewById(R.id.listSearchHistory);
        txtSearch=findViewById(R.id.txtSearch);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtSearch.getText().toString().isEmpty())
                {
                Toast.makeText(SearchActivity.this,"Clicking...",Toast.LENGTH_SHORT).show();
           //     txtSearch.setFocusable(true);
                progressBar.setVisibility(View.GONE);
                rvSearchHistory.setVisibility(View.VISIBLE);
                mPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                textClr.setVisibility(View.VISIBLE);
                textHis.setVisibility(View.VISIBLE);
                showHistorySearch();
                }
            }
        });
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txtSearch.getText().toString().isEmpty())
                {
                    progressBar.setVisibility(View.GONE);
                    rvSearchHistory.setVisibility(View.VISIBLE);
                    mPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    textClr.setVisibility(View.VISIBLE);
                    textHis.setVisibility(View.VISIBLE);
                    showHistorySearch();
                }
//                else {
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(SearchActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.VISIBLE);
//                            showResult();
//                        }
//                    }, 3500);
//                }
            }
        });
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH)
                {
                    if(!txtSearch.getText().toString().isEmpty()) {
                      //  txtSearch.setFocusable(false);

                        rvSearchHistory.setVisibility(View.GONE);
                        textClr.setVisibility(View.GONE);
                        textHis.setVisibility(View.GONE);
                        mPager.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        showResult();
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        },2100);
                    }
                }
                return false;
            }
        });
       // getStores();
        mPager=findViewById(R.id.viewPager);
        showStores();
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               // Toast.makeText(SearchActivity.this,SearchPagerAdapter.tabs[position],Toast.LENGTH_SHORT).show();

                showResult();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public void onClick(String s) {
     //   txtSearch.setFocusable(false);
        rvSearchHistory.setVisibility(View.GONE);
        textClr.setVisibility(View.GONE);
        textHis.setVisibility(View.GONE);
        mPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        showResult();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        },2100);
    }

    public void showResult()
    {
        switch (mPager.getCurrentItem())
        {
            case 0:
              //  Toast.makeText(this,"Page "+SearchPagerAdapter.tabs[0],Toast.LENGTH_SHORT).show();

//                mPager.notifyAll();
                showRelevantResults();
                break;
            case 1:
              //  Toast.makeText(this,"Page "+SearchPagerAdapter.tabs[1],Toast.LENGTH_SHORT).show();

                showNearMeResults();
                break;
        }
    }

    public void showHistorySearch()
    {

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SearchActivity.this, DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_1));
            SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(str, SearchActivity.this);
            rvSearchHistory.addItemDecoration(dividerItemDecoration);
            rvSearchHistory.setAdapter(searchHistoryAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
            rvSearchHistory.setLayoutManager(layoutManager);

    }

    public void showRelevantResults()
    {
//        listStores.clear();
//        db = FirebaseFirestore.getInstance();
//        db.collection("Stores").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Store s = document.toObject(Store.class);
//                        listStores.add(s);
//                        Log.e("Test",s.getName());
//                    }
//                    SearchPagerAdapter spa=new SearchPagerAdapter(SearchActivity.this,getSupportFragmentManager(),listStores,SearchActivity.this);
//                    mPager.setAdapter(spa);
//                    tabLayout.setupWithViewPager(mPager);
//                } else {
//                    Log.d("Test", "Error getting documents: ", task.getException());
//                }
//            }
//        });

    }

    public void showNearMeResults()
    {
//        listStores.clear();
//        db = FirebaseFirestore.getInstance();
//        db.collection("Stores").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Store s = document.toObject(Store.class);
//                        listStores.add(s);
//                        Log.e("Test",s.getName());
//                    }
//                    SearchPagerAdapter spa=new SearchPagerAdapter(SearchActivity.this,getSupportFragmentManager(),listStores,SearchActivity.this);
//                    mPager.setAdapter(spa);
//                    tabLayout.setupWithViewPager(mPager);
//                } else {
//                    Log.d("Test", "Error getting documents: ", task.getException());
//                }
//            }
//        });

    }

    @Override
    public void onStoreClick(Store s) {
        Intent intent = new Intent(this, StoreActivity.class);
        //intent.putExtra("RESTAURANT", restaurant);
        startActivity(intent);
        EventBus.getDefault().postSticky(new MessageEvent(s, com.uit.uitnow.MessageEvent.FROM_storeFRAG_TO_storeACT));
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left); // animation
    }

    private void showStores() {

        listStores.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("Stores").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Store s = document.toObject(Store.class);
                        listStores.add(s);
                        Log.e("Test",s.getName());
                    }
                    spa=new SearchPagerAdapter(SearchActivity.this,getSupportFragmentManager(),listStores,SearchActivity.this);
                    mPager.setAdapter(spa);
                    tabLayout = findViewById(R.id.tabLayout);
//
                    tabLayout.setupWithViewPager(mPager);
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        tabLayout.getTabAt(i).setCustomView(spa.getTabView(i));
                    }
                    mPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                } else {
                    Log.d("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
