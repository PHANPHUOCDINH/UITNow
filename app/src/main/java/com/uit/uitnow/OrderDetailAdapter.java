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

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{
    ArrayList<ItemBasket> mItems;

    public OrderDetailAdapter(ArrayList<ItemBasket> mItems)
    {
        this.mItems=mItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_orderdetail, parent, false);
        OrderDetailAdapter.ViewHolder viewHolder = new OrderDetailAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ItemBasket itemBasket = mItems.get(i);
        viewHolder.tvName.setText(itemBasket.name);
        viewHolder.tvQuantity.setText(itemBasket.getQuantityStr());
        viewHolder.tvPrice.setText(String.valueOf(itemBasket.getPrice()+ " VND"));
        viewHolder.tvSum.setText(itemBasket.getQuantity()*itemBasket.getPrice()+ " VND");
        viewHolder.tvGhiChu.setText(itemBasket.getGhichu());
        Picasso.get().load(itemBasket.getImage()).into(viewHolder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvSum,tvGhiChu;
        ImageView ivImage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSum = itemView.findViewById(R.id.tvSum);
            tvGhiChu=itemView.findViewById(R.id.tvGhiChu);
            ivImage=itemView.findViewById(R.id.ivImage);
        }
    }
}
