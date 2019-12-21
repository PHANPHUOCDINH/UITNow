package com.uit.uitnow;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VoucherDialogFragment extends DialogFragment implements VoucherAdapter.OnClickUseVoucherListener {
    static int VOUCHER_COPY_CODE_REQUEST=999;
    ArrayList<Voucher> list=new ArrayList<>();
    RecyclerView rvVoucher;
    VoucherAdapter adapter;
    FirebaseFirestore db;
    DialogFragment previousFragment;
    public VoucherDialogFragment()
    {

    }
    public VoucherDialogFragment(DialogFragment fragment)
    {
        previousFragment=fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.voucher_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db=FirebaseFirestore.getInstance();
        rvVoucher=view.findViewById(R.id.rvVoucher);
        showVouchers();
    }

    private void showVouchers()
    {
        list.clear();
        db.collection("Vouchers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Voucher v = document.toObject(Voucher.class);
                        list.add(v);
                    }
                    adapter = new VoucherAdapter(list,VoucherDialogFragment.this);
                    rvVoucher.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvVoucher.setLayoutManager(layoutManager);
                } else {
                    Log.d("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onUseVoucher(Voucher v) {
        if(previousFragment!=null) {
            ((BasketDialogFragment) previousFragment).txtCode.setText(v.getId());
        }
        else
        {
            Toast.makeText(getActivity(),"Vui lòng sử dụng mã khi đặt hàng",Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }
    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(true);
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
