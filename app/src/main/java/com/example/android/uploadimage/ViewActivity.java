package com.example.android.uploadimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {

    ImageView imageView;
    DatabaseReference databaseReference, reference;
    StorageReference storageReference;
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        imageView = findViewById(R.id.image_view_activity);
        button = findViewById(R.id.btn_delete);
        textView = findViewById(R.id.textView_view_activity);
        String Carkey = getIntent().getStringExtra("CarKey");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Car");
        storageReference = FirebaseStorage.getInstance().getReference().child("CarImages").child(Carkey+ ".jpg");
        reference = FirebaseDatabase.getInstance().getReference().child("Car").child(Carkey);

        databaseReference.child(Carkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String carName = snapshot.child("CarName").getValue().toString();
                    String imageUrl = snapshot.child("ImageUrl").getValue().toString();

                    Picasso.get().load(imageUrl).into(imageView);
                    textView.setText(carName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            }
                        });
                    }
                });
            }
        });
    }
}