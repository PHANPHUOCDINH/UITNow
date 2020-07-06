package com.uit.uitnow;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class ListSearchHistoryAdapter implements ListAdapter {
    ArrayList<String> listData;
    LayoutInflater inflater;
    OnSearchHistoryClickListener mListener;
    public interface OnSearchHistoryClickListener{
        void onClick(String search);
    }
    public ListSearchHistoryAdapter(ArrayList<String> listSearch,OnSearchHistoryClickListener mListener)
    {
        this.listData=listSearch;
        this.mListener=mListener;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        if(listData != null && !listData.isEmpty()){
            return listData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View rowView = view;

        if(rowView == null){
            rowView = inflater.inflate(R.layout.row_hour, parent, false);
            ListSearchHistoryAdapter.ViewHolder viewHolder = new ListSearchHistoryAdapter.ViewHolder();
            viewHolder.layout = (LinearLayout) rowView.findViewById(R.id.layout);
            viewHolder.textItem = (Button) rowView.findViewById(R.id.text_item);
            rowView.setTag(viewHolder);
        }

        final ListSearchHistoryAdapter.ViewHolder holder = (ListSearchHistoryAdapter.ViewHolder) rowView.getTag();
        holder.textItem.setText(listData.get(i));
        holder.textItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(holder.textItem.getText().toString());
            }
        });
        return rowView;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private class ViewHolder {
        public LinearLayout layout;
        public Button textItem;
    }
}
