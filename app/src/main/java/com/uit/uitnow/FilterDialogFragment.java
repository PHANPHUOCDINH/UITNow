package com.uit.uitnow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FilterDialogFragment extends DialogFragment {
    TextView txtReset;
    ArrayList<RadioButton> list=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtReset=view.findViewById(R.id.txtReset);
        list.add((RadioButton)view.findViewById(R.id.btnTraSua));
        list.add((RadioButton)view.findViewById(R.id.btnCom));
        list.add((RadioButton)view.findViewById(R.id.btnSinhTo));
        list.add((RadioButton)view.findViewById(R.id.btnQ2));
        list.add((RadioButton)view.findViewById(R.id.btnQ1));
        list.add((RadioButton)view.findViewById(R.id.btnQ3));
        list.add((RadioButton)view.findViewById(R.id.btnQ4));
        list.add((RadioButton)view.findViewById(R.id.btnTopRate));
        list.add((RadioButton)view.findViewById(R.id.btnPickUp));
        list.add((RadioButton)view.findViewById(R.id.btnSuggestion));
        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(RadioButton r:list)
                {
                   r.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(true);
        super.onResume();
    }
}
