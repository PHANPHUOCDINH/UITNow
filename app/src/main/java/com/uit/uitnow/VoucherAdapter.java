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

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder>{
    public interface OnClickUseVoucherListener {
        void onUseVoucher(Voucher v);
    }

    private ArrayList<Voucher> listVouchers;
    private OnClickUseVoucherListener listener;

    public VoucherAdapter(ArrayList<Voucher> vouchers, OnClickUseVoucherListener
            listener) {
        listVouchers = vouchers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_voucher, parent, false);
        VoucherAdapter.ViewHolder viewHolder = new VoucherAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Voucher v= listVouchers.get(position);
        Picasso.get().load(v.getImage()).into(holder.ivVoucher);
        holder.txtVoucherName.setText(v.getName());
        holder.txtUseVoucher.setText("Sử dụng");
        holder.txtUseVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUseVoucher(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVouchers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivVoucher;
        TextView txtVoucherName,txtUseVoucher;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVoucher=itemView.findViewById(R.id.ivVoucher);
            txtUseVoucher=itemView.findViewById(R.id.txtUseVoucher);
            txtVoucherName=itemView.findViewById(R.id.txtVoucherName);
        }
    }
}
