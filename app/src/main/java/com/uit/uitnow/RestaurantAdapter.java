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

// nhà hàng đặt bàn
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    ArrayList<Restaurant> listRests;
    OnRestaurantClickListener listener;
    public interface OnRestaurantClickListener{
        void onRestaurantClick(Restaurant res);
    }
    public RestaurantAdapter(ArrayList<Restaurant> rests,OnRestaurantClickListener listener)
    {
        this.listRests=rests;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_restaurant, parent, false);
        RestaurantAdapter.ViewHolder viewHolder = new RestaurantAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Restaurant r=listRests.get(position);
        Picasso.get().load(r.getLogoUrl()).into(holder.ivImage);
        holder.tvAddress.setText(r.getAddress());
        holder.tvName.setText(r.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRestaurantClick(r);
            }
        });
        holder.tvDescription.setText(r.getDescription());
    }

    @Override
    public int getItemCount() {
        return listRests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress,tvDescription;
        ImageView ivImage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName) ;
            ivImage = itemView.findViewById(R.id.ivImage);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDescription=itemView.findViewById(R.id.tvDescription);
        }
    }
}
