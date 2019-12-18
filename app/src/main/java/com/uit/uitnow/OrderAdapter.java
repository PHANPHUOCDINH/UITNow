package com.uit.uitnow;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<Order> mOrders;
    private OrderListener mListener;
    public interface OrderListener {
        void onCancelBooking(Order order);
        void onXemChiTiet(Order order);
        void onReOrder(Order order);
    }
    public OrderAdapter(List<Order> mOrders,OrderListener listener,Context context) {
        this.context=context;
        this.mOrders = mOrders;
        this.mListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Order order=mOrders.get(position);
        holder.tvId.setText(order.id);
        holder.tvRestaurant.setText(order.storeName);
        holder.tvDestination.setText(order.deliveryAddress);
        holder.tvTotalPrices.setText(order.tongGia);
        String trangThai=order.trangThai;
        if(trangThai.equals("Cancelled"))
        {
            holder.tvTrangThai.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.btnCancelBooking.setVisibility(View.GONE);
            holder.btnReOrder.setVisibility(View.VISIBLE);
        }
        else
        {
            if(trangThai.equals("Booking")) {
                holder.tvTrangThai.setTextColor(context.getResources().getColor(R.color.colorOrange));
                holder.btnCancelBooking.setVisibility(View.VISIBLE);
                holder.btnReOrder.setVisibility(View.GONE);
            }
            else
            {
                if(trangThai.equals("Finished"))
                {
                    holder.tvTrangThai.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.btnCancelBooking.setVisibility(View.GONE);
                    holder.btnReOrder.setVisibility(View.GONE);
                }
            }
        }
        holder.tvTrangThai.setText(trangThai);
        holder.btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelBooking(order);
            }
        });
        holder.btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onXemChiTiet(order);
            }
        });
        holder.btnReOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onReOrder(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvRestaurant,tvDestination,tvTotalPrices,tvTrangThai;
        Button btnCancelBooking,btnChiTiet,btnReOrder;
        public ViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvOrderId) ;
            tvRestaurant = itemView.findViewById(R.id.tvRestaurant);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvTotalPrices = itemView.findViewById(R.id.tvTotalPrices);
            tvTrangThai=itemView.findViewById(R.id.tvTrangThai);
            btnCancelBooking = itemView.findViewById(R.id.btnCancelBooking);
            btnChiTiet=itemView.findViewById(R.id.btnChiTiet);
            btnReOrder=itemView.findViewById(R.id.btnReOrder);
        }
    }
}

