package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity  extends AppCompatActivity {
    Button register;
    Button login;
    EditText email,password;
    TextView forgotpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        register=findViewById(R.id.btnSignUp);
        login=findViewById(R.id.btnSignIn);
        email=findViewById(R.id.txtEmail);
        password=findViewById(R.id.txtPassword);
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
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),
                        password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignInActivity.this,"login sucsessfull",Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(SignInActivity.this,"Account doesn't exist",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,ForgotPasswordActivity.class));
            }
        });
    }
}
