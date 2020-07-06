package com.uit.uitnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder>{
    private Context context;
   // private List<Booking> mOrdersFull;
    private ArrayList<Booking> mBookings;
    private OnBookingHistoryClickListener mListener;
    public interface OnBookingHistoryClickListener{
        void onClick(Booking booking);
    }

    public BookingAdapter(ArrayList<Booking> bookings,OnBookingHistoryClickListener mListener)
    {
        this.mBookings=bookings;
        this.mListener=mListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_booking_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Booking booking=mBookings.get(position);
        holder.tvId.setText(booking.getId());
        holder.tvDate.setText(booking.getTime());
        holder.tvName.setText(booking.getResName());
        holder.tvDestination.setText(booking.getResAddress());
        int tt=Integer.parseInt(booking.getTrangThai());
        holder.tvTrangThai.setText(tt==BookingRequestStatus.ACCEPTED?"Đã xác nhận":tt==BookingRequestStatus.FINISHED?"Đã kết thúc":tt==BookingRequestStatus.CANCELED_BY_USER?"Đã hủy":"Đang chờ xác nhận");
        holder.txtTime.setText(booking.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvDate,tvName,tvDestination,tvTrangThai,txtDate,txtTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvBookingId) ;
            tvDate = itemView.findViewById(R.id.tvDate);
            tvName = itemView.findViewById(R.id.tvRestaurant);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvTrangThai = itemView.findViewById(R.id.txtTrangThai);
            txtTime=itemView.findViewById(R.id.txtTime);
        }
    }
}
