package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button reset;
    EditText email;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        reset=findViewById(R.id.reset);
        email=findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPasswordActivity.this,"password reset email sent!",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(ForgotPasswordActivity.this,SignInActivity.class));
                        }
                        else {
                            Toast.makeText(ForgotPasswordActivity.this,"password reset error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
