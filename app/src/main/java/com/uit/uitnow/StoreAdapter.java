package com.uit.uitnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Cửa hàng đặt món
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>  {
    private ArrayList<Store> listStores;

    public StoreAdapter(ArrayList<Store> stores) {
        listStores = stores;
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
                // click vào một cửa hàng
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress;
        ImageView ivImage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName) ;
            ivImage = itemView.findViewById(R.id.ivImage);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }
    }


}
