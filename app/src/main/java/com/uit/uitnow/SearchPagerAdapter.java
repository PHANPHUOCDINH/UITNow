package com.uit.uitnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private static final int NUM_PAGES = 2;
    public static String[] tabs = {"Đúng nhất", "Gần tôi"};
    ArrayList<Store> listStores;
    SearchResultFragment.OnStoreClickListener mListener;
    public SearchPagerAdapter(Context context, FragmentManager fm, ArrayList<Store> listStores, SearchResultFragment.OnStoreClickListener mListener) {
        super(fm);
        this.context=context;
        this.listStores=listStores;
        this.mListener=mListener;
    }
    @Override
    public Fragment getItem(int position) {
                return new SearchResultFragment(listStores, mListener);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
    public View getTabView(int position)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_tab_item, null);
        TextView tvTitle = v.findViewById(R.id.tvTabTitle);
        tvTitle.setText(tabs[position]);
        return v;
    }

//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
//        LayoutInflater inflater=(LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View page= inflater.inflate(R.layout.layout_tab_item,null);
//        page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context,"Page "+tabs[position],Toast.LENGTH_SHORT).show();
//            }
//        });
//        ((ViewPager) container).addView(page,0);
//        return page;
//    }
}
