package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignInActivity  extends AppCompatActivity {
    ProgressBar spinner;
    App app;
    Button register;
    Button login;
    EditText txtEmail,txtPassword;
    TextView forgotpass;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        spinner=findViewById(R.id.spinner);
        register=findViewById(R.id.btnSignUp);
        login=findViewById(R.id.btnSignIn);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);
        forgotpass=findViewById(R.id.tvForgot);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                String email=txtEmail.getText().toString();
                String pass=txtPassword.getText().toString();
                if(email.isEmpty()||pass.isEmpty())
                    Toast.makeText(SignInActivity.this,"Không để trống thông tin",Toast.LENGTH_SHORT).show();
                else
                    signIn(email,pass);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,ForgotPasswordActivity.class));
            }
        });
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        app=(App)getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String email=PrefUtil.loadPref(this,"email");
        if(email!=null)
        {
            spinner.setVisibility(View.VISIBLE);
            db= FirebaseFirestore.getInstance();
            db.collection("Users").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User u=document.toObject(User.class);
                            app.user=u;
                            PrefUtil.savePref(SignInActivity.this,"email",email);
                            PrefUtil.savePref(SignInActivity.this,"phone",u.getPhone());
                            PrefUtil.savePref(SignInActivity.this,"id",u.getId());
                            PrefUtil.savePref(SignInActivity.this,"name",u.getName());
                            PrefUtil.savePref(SignInActivity.this,"photo",u.getPhoto());
                            PrefUtil.savePref(SignInActivity.this,"address",u.getAddress());
                            getInMainActivity();
                        }
                    } else {
                        Log.d("Test", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else
        {
            spinner.setVisibility(View.GONE);
        }
    }

    private void signIn(final String email, String pass)
    {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// đăng nhập thành công, cập nhật UI với thông tin của người dùng
//                            Log.d("Test", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.collection("Users").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            User u=document.toObject(User.class);
                                            app.user=u;
                                            PrefUtil.savePref(SignInActivity.this,"email",email);
                                            PrefUtil.savePref(SignInActivity.this,"phone",u.getPhone());
                                            PrefUtil.savePref(SignInActivity.this,"id",u.getId());
                                            PrefUtil.savePref(SignInActivity.this,"name",u.getName());
                                            PrefUtil.savePref(SignInActivity.this,"photo",u.getPhoto());
                                            PrefUtil.savePref(SignInActivity.this,"address",u.getPhoto());
                                            PrefUtil.savePref(SignInActivity.this,"address",u.getAddress());
                                            getInMainActivity();
                                        }
                                    } else {
                                        Log.d("Test", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        } else {
// nếu đăng nhập thất bại, thông báo lỗi đến người dùng
                            Log.w("Test", "SignInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Đăng nhập thất bại",
                                    Toast.LENGTH_SHORT).show();

                        }
// ...
                    }
                });
    }

    private void getInMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
