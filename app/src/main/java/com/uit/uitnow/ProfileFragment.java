package edu.csc.drinknow;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.uit.uitnow.R;

import de.hdodenhof.circleimageview.CircleImageView;

//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.FileProvider;
//import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment  {
    private static final String APP_TAG = "FoodNow";
    public final static int CAPTURE_IMAGE_REQUEST_CODE = 1034;
    public final static int PICK_PHOTO_REQUEST_CODE = 1046;
    Dialog dialog;

    CircleImageView ivAvatar;
    TextView tvEmail,tvAddress;

    Button btnUpdate,btnChangePass,btnSignOut;
    EditText txtName,txtPhone;

    public ProfileFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        return view;
    }




}
