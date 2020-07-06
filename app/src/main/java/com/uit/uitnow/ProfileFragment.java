package com.uit.uitnow;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.uit.uitnow.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;



public class ProfileFragment extends Fragment implements View.OnClickListener,ChoosePhotoDialogFragment.ChoosePhotoDialogItemListener  {
    private static final String APP_TAG = "UITNow";
    public final static int TAKE_PHOTO_REQUEST_CODE = 1034;
    public final static int PICK_PHOTO_REQUEST_CODE = 1046;
    Dialog dialog;
    File photoFile;
    App app;
    CircleImageView ivAvatar;
    TextView tvEmail,tvAddress,txtSeeVouchers;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db;
    Button btnUpdate,btnChangePass,btnSignOut;
    EditText txtName,txtPhone;
    ImageView ivNoti;
    public ProfileFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        db=FirebaseFirestore.getInstance();
        btnChangePass=view.findViewById(R.id.btnChangePassword);
        btnChangePass.setOnClickListener(this);
        btnUpdate=view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnSignOut=view.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener(this);
        tvEmail=view.findViewById(R.id.tvEmail);
        tvAddress=view.findViewById(R.id.tvAddress);
        txtName=view.findViewById(R.id.tvName);
        txtPhone=view.findViewById(R.id.tvPhone);
        txtSeeVouchers=view.findViewById(R.id.txtSeeVouchers);
        txtSeeVouchers.setOnClickListener(this);
        tvAddress.setText(PrefUtil.loadPref(getActivity(),"address"));
        txtName.setText(PrefUtil.loadPref(getActivity(),"name"));
        txtPhone.setText(PrefUtil.loadPref(getActivity(),"phone"));
        tvEmail.setText(PrefUtil.loadPref(getActivity(),"email"));
        String photoPath=PrefUtil.loadPref(getActivity(),"photo");
        if(!photoPath.isEmpty())
            Picasso.get().load(PrefUtil.loadPref(getActivity(), "photo")).into(ivAvatar);
        app=(App)getActivity().getApplication();
        ivNoti=view.findViewById(R.id.ivNoti);
        ivNoti.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String photoPath=PrefUtil.loadPref(getActivity(),"photo");
        if(!photoPath.isEmpty())
            Picasso.get().load(PrefUtil.loadPref(getActivity(), "photo")).into(ivAvatar);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.ivAvatar) {
            ChoosePhotoDialogFragment dialog = new ChoosePhotoDialogFragment(this);
            dialog.show(getActivity().getSupportFragmentManager(), "photo_dialog");
        }
        else
        {
            if(id==R.id.btnSignOut)
            {
                // Đăng xuất
                FirebaseAuth.getInstance().signOut();
                PrefUtil.clearPref(getActivity(),"email");
                PrefUtil.clearPref(getActivity(),"id");
                PrefUtil.clearPref(getActivity(),"photo");
                PrefUtil.clearPref(getActivity(),"name");
                PrefUtil.clearPref(getActivity(),"phone");
                PrefUtil.clearPref(getActivity(),"address");
                PrefUtil.clearPref(getActivity(),"search");
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(id==R.id.btnUpdate)
                {
                    final String name=txtName.getText().toString();
                    final String phone=txtPhone.getText().toString();
                    final String address=tvAddress.getText().toString();
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", name);
                    data.put("phone", phone);
                    data.put("address",address);
                    DocumentReference docRef = db.collection("Users").document(PrefUtil.loadPref(getActivity(),"id"));
// cập nhật field “capital” của document “DC”
                    docRef
                            .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    app.user.setName(name);
                                    app.user.setPhone(phone);
                                    app.user.setAddress(address);
                                    Toast.makeText(getActivity(),"Đã cập nhật",Toast.LENGTH_SHORT).show();
                                    PrefUtil.savePref(getActivity(),"name",name);
                                    PrefUtil.savePref(getActivity(),"phone",phone);
                                    PrefUtil.savePref(getActivity(),"address",address);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    if(id==R.id.btnChangePassword)
                    {
                        showChangePasswordDialog();
                    }
                    else
                    {
                        if(id==R.id.txtSeeVouchers)
                        {
                            VoucherDialogFragment dialog = new VoucherDialogFragment();
                            dialog.show(getActivity().getSupportFragmentManager(), "voucher_dialog");
                        }
                        else
                        {
                            if(id==R.id.ivNoti)
                            {
                                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                            }
                        }
                    }
                }
            }
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void launchCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String photoFileName = "IMG_" + System.currentTimeMillis();
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.uit.uitnow.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
        }
    }

    private void pickPhoto()
    {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                uploadImage(FileProvider.getUriForFile(getActivity(), "com.uit.uitnow.fileprovider", photoFile));
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 200);
                ivAvatar.setImageBitmap(resizedBitmap);
            } else {
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri photoUri = data.getData();
                    uploadImage(photoUri);
                    Bitmap selectedImage = null;
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (selectedImage != null) {
                        ivAvatar.setImageBitmap(BitmapScaler.scaleToFitWidth(selectedImage, 200));
                    }
                }
            }
        }
    }

    private void uploadImage(Uri uriPhoto)
    {
        if(uriPhoto!=null)
        {
            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref=storageReference.child("avatars/"+ PrefUtil.loadPref(getActivity(),"id"));
            ref.putFile(uriPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();


                    //    updateUserPhoto();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Upload Failed",Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    //cập nhật link avatar
                    if (task.isSuccessful()){
                        Uri downUri = task.getResult();
                        Map<String, Object> data = new HashMap<>();
                        PrefUtil.savePref(getActivity(),"photo",downUri.toString());
                        data.put("photo", downUri.toString());
                        DocumentReference docRef = db.collection("Users").document(PrefUtil.loadPref(getActivity(),"id"));
                        docRef
                                .update(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(),"Đã cập nhật",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }
    }

    private void showChangePasswordDialog()
    {
        dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_changepass);
        final EditText txtOldPasswordDialog=dialog.findViewById(R.id.txtOldPassword);
        final EditText txtNewPasswordDialog=dialog.findViewById(R.id.txtNewPassword);
        final EditText txtNewPasswordConfirmDialog=dialog.findViewById(R.id.txtNewPasswordConfirm);
        final Button btnOK=dialog.findViewById(R.id.btnOK);
        final Button btnCancel=dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass=txtOldPasswordDialog.getText().toString();
                String newPass=txtNewPasswordDialog.getText().toString();
                String newPassconfirm=txtNewPasswordConfirmDialog.getText().toString();
                if(newPass.equals(newPassconfirm))
                    changePass(oldPass,newPass);
                else
                    Toast.makeText(getActivity(),"Password Confirm Fail", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void changePass(String oldPass, final String newPass)
    {
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential= EmailAuthProvider.getCredential(user.getEmail(),oldPass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.e("Test","Update Password Successful");
                                Toast.makeText(getActivity(),"Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Log.e("Test","Update Password Failed");
                        }
                    });
                }
                else
                    Log.e("Test","Old Password Wrong");
            }
        });
    }

    @Override
    public void onCameraClicked() {
        launchCamera();
    }

    @Override
    public void onGalleryClicked() {
        pickPhoto();
    }

}
