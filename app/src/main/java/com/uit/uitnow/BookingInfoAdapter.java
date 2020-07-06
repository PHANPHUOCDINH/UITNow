package com.uit.uitnow;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingInfoAdapter  extends RecyclerView.Adapter<BookingInfoAdapter.ViewHolder> implements ListViewAdapter.OnHourClickListener {
    ArrayList<BookingInfo> bookings;
    Context context;
    OnHourAdapterClickListener mListener;
    DatePickerDialog datePickerDialog;
    public interface OnHourAdapterClickListener{
        void onHourClick(String hour,BookingInfo bookingInfo,Button txtDate);
    }
    public BookingInfoAdapter(ArrayList<BookingInfo> bookings,Context c,OnHourAdapterClickListener mListener) {
        this.bookings=bookings;
        context=c;
        this.mListener=mListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_booking, parent, false);
        BookingInfoAdapter.ViewHolder viewHolder = new BookingInfoAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        BookingInfo b=bookings.get(position);
        holder.tvTitle.setText(b.getName());
        holder.tvDetail.setText(b.getDescription());
        Picasso.get().load(b.getUrl()).into(holder.ivImage);
        String []hourS;
        ArrayList<String> listHours=new ArrayList<>();
        int from=Integer.parseInt(b.getTimeStart());
        int to=Integer.parseInt(b.getTimeEnd());
        for(int i=from;i<=to;i++)
        {
            listHours.add(i+":00");
            listHours.add(i+":15");
            listHours.add(i+":30");
            listHours.add(i+":45");
        }

        holder.btnDateChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog dialog= new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        holder.btnDateChoose.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                calendar.add(Calendar.DATE,100);
                dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                dialog.show();
            }
        });
        ListViewAdapter mAdapter=new ListViewAdapter(context,this,listHours,b,holder.btnDateChoose);
        holder.twoWayView.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    @Override
    public void onHourClick(String hour,BookingInfo bookingInfo,Button txtDate) {
        mListener.onHourClick(hour,bookingInfo,txtDate);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetail;
        Button btnDateChoose;
        ImageView ivImage;
        TwoWayView twoWayView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookingTitle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDetail = itemView.findViewById(R.id.tvBookingDetail);
            twoWayView=itemView.findViewById(R.id.listHours);
            btnDateChoose=itemView.findViewById(R.id.btnDateChoose);
        }
    }
}
