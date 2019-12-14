package com.uit.uitnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemBasketAdapter extends RecyclerView.Adapter<ItemBasketAdapter.ViewHolder> {
    public interface OnItemQuantityListener {
        void onChangeItemQuantity(ItemBasket item);
    }
    private List<ItemBasket> mItems;
    private OnItemQuantityListener mListener;

    public ItemBasketAdapter(List<ItemBasket> items, OnItemQuantityListener
            listener) {
        mItems = items;
        mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_basket, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final ItemBasket itemBasket = mItems.get(i);
        viewHolder.tvName.setText(itemBasket.name);
        viewHolder.tvQuantity.setText(itemBasket.getQuantityStr());
        viewHolder.tvPrice.setText(String.valueOf(itemBasket.getPrice()+ " VND"));
        viewHolder.tvSum.setText(itemBasket.getSum());
        viewHolder.btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 1
                itemBasket.decrease();
                updateStats(viewHolder, itemBasket);
                mListener.onChangeItemQuantity(itemBasket);
            }
        });
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 2
                itemBasket.increase();
                updateStats(viewHolder, itemBasket);
                mListener.onChangeItemQuantity(itemBasket);
            }
        });
        viewHolder.tvGhiChu.setText(itemBasket.getGhichu());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvSum,tvGhiChu;
        ImageView btnSubtract, btnAdd;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSum = itemView.findViewById(R.id.tvSum);
            btnSubtract = itemView.findViewById(R.id.btnSubtract);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            tvGhiChu=itemView.findViewById(R.id.tvGhiChu);
        }
    }

    private void updateStats(ViewHolder viewHolder, ItemBasket item) { // 3
        viewHolder.tvQuantity.setText(item.getQuantityStr());
        viewHolder.tvPrice.setText(String.valueOf(item.getPrice())+" VND");
        viewHolder.tvSum.setText(item.getSum());
    }
}
