package com.uit.uitnow;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListStoreViewAdapter implements ListAdapter {
    LayoutInflater inflater;
    OnStoreClickListener mListener;
    List<String> images,infos,ids;
    public interface OnStoreClickListener{
        void onStoreClick(String s);
    }
    public ListStoreViewAdapter(Context c, List<String> images,List<String> infos,List<String> ids,OnStoreClickListener mListener)
    {
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images=images;
        this.infos=infos;
        this.ids=ids;
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
        if(images != null && !images.isEmpty()){
            return images.size();
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
    public View getView(final int i, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if(rowView == null){
            rowView = inflater.inflate(R.layout.row_store_1, parent, false);
            ListStoreViewAdapter.ViewHolder viewHolder = new ListStoreViewAdapter.ViewHolder();
            viewHolder.layout = (RelativeLayout) rowView.findViewById(R.id.layout);
            viewHolder.imageView=rowView.findViewById(R.id.ivStore);
            viewHolder.textItem =  rowView.findViewById(R.id.text_item);
            rowView.setTag(viewHolder);
        }
        final ListStoreViewAdapter.ViewHolder holder = (ListStoreViewAdapter.ViewHolder) rowView.getTag();
        Picasso.get().load(images.get(i)).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStoreClick(ids.get(i));
            }
        });
        holder.textItem.setText(infos.get(i));
        return rowView;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return images.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private class ViewHolder {
        public RelativeLayout layout;
        public TextView textItem;
        public ImageView imageView;
    }
}
