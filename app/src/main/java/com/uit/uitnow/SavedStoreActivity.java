package com.uit.uitnow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class SavedStoreActivity extends AppCompatActivity {
    ImageView imageView;
    TextView tvName,tvDes,tvPoint;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_store);
        imageView=findViewById(R.id.ivImage);
        tvName=findViewById(R.id.tvName);
        tvDes=findViewById(R.id.tvDescription);
        tvPoint=findViewById(R.id.tvPoint);
        show();
    }

    private void show()
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Stores").whereEqualTo("id","azE2b6uJIWWc1fQZvqZ2").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Store s = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        s = document.toObject(Store.class);
                    }
                    Picasso.get().load(s.getLogoUrl()).into(imageView);
                    tvDes.setText(s.getDescription());
                    tvName.setText(s.getName());
                    tvPoint.setText(s.getPointRate());
                }
            }
        });
    }
}
