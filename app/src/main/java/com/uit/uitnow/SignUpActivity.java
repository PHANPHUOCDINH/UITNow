package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText txtEmail,txtPassword,txtConfirm,txtName,txtPhone;
    Button register;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtEmail=findViewById(R.id.email);
        txtPassword=findViewById(R.id.password);
        txtConfirm=findViewById(R.id.confirm);
        txtName=findViewById(R.id.name);
        txtPhone=findViewById(R.id.phoneNum);
        register=(Button)findViewById((R.id.register));
        db= FirebaseFirestore.getInstance();
        firebaseAuth =FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=txtEmail.getText().toString();
                final String password=txtPassword.getText().toString();
                final String confirm=txtConfirm.getText().toString();
                final String name=txtName.getText().toString();
                final String phone=txtPhone.getText().toString();
                if(password.equals(confirm)) {
                    firebaseAuth.createUserWithEmailAndPassword(email
                            , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               // Toast.makeText(SignUpActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                                long l=System.currentTimeMillis();
                                User u=new User(String.valueOf(l),name,email,phone,"","");
                                db.collection("Users").document(String.valueOf(l)).set(u);
                                Toast.makeText(SignUpActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Register fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"Password Confirm Wrong",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
