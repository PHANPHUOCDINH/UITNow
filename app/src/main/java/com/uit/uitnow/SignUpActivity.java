package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
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

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText email,password,confirm;
    Button register;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email=(TextInputEditText)findViewById(R.id.email);
        password=(TextInputEditText)findViewById(R.id.password);
        confirm=(TextInputEditText)findViewById(R.id.confirm);
        register=(Button)findViewById((R.id.register));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth =FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString()
                        , password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Register successfull", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                        } else {
                            Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }
}
