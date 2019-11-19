package com.uit.uitnow;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;

public class ChoosePhotoDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button btnCamera, btnGallery, btnCancel;
    private ChoosePhotoDialogItemListener listener;

    public ChoosePhotoDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public ChoosePhotoDialogFragment(ChoosePhotoDialogItemListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_picture, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);

        btnGallery = view.findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().setCancelable(true);
        super.onResume();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //  ((StoreActivity) getActivity()).updateBasket();
        super.onCancel(dialog);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnCamera) {
            listener.onCameraClicked();
        } else if (id == R.id.btnGallery) {
            listener.onGalleryClicked();
        }

        getDialog().dismiss();
    }

    public interface ChoosePhotoDialogItemListener {
        void onCameraClicked();

        void onGalleryClicked();
    }
}
