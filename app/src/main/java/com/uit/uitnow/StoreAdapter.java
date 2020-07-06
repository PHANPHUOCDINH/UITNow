package com.uit.uitnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Cửa hàng đặt món
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> implements Filterable {
    private ArrayList<Store> listStoresFull;
    private ArrayList<Store> listStores;
    private StoreListener listener;
    private Context context;
    public StoreAdapter(ArrayList<Store> stores,StoreListener listener,Context context) {
        listStores = stores;
        listStoresFull=new ArrayList<>(stores);
        this.listener=listener;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_store, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Store store= listStores.get(position);
        Picasso.get().load(store.getLogoUrl()).into(holder.ivImage);
        holder.tvName.setText(store.getName());
        holder.tvAddress.setText(store.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStoreClick(store);
            }
        });
        double point=Double.parseDouble(store.getPointRate());
        if(point>6.0)
        {
            holder.tvPoint.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            holder.tvPoint.setTextColor(context.getResources().getColor(R.color.colorRed));
        }
        holder.tvPoint.setText(store.getPointRate());
        holder.tvDescription.setText(store.getDescription());
    }

    @Override
    public int getItemCount() {
        return listStores.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress,tvPoint,tvDescription;
        ImageView ivImage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName) ;
            ivImage = itemView.findViewById(R.id.ivImage);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPoint=itemView.findViewById(R.id.tvPoint);
            tvDescription=itemView.findViewById(R.id.tvDescription);
        }
    }

    public interface StoreListener{
        public void onStoreClick(Store store);
    }

    @Override
    public Filter getFilter() {
        return storeFilter;
    }

    private Filter storeFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
             ArrayList<Store> filterList=new ArrayList<>();
             if(charSequence==null||charSequence.length()==0)
             {
                 filterList.addAll(listStoresFull);
             }
             else
             {
                 String filterString=charSequence.toString().toLowerCase().trim();
                 for(Store item: listStoresFull)
                 {
                     if(item.getName().toLowerCase().contains(filterString) || item.getDescription().toLowerCase().contains(filterString))
                         filterList.add(item);
                 }
             }
             FilterResults results=new FilterResults();
             results.values=filterList;
             return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listStores.clear();
            listStores.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
