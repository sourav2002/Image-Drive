package com.example.android.uploadimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<CarModel> options;
    FirebaseRecyclerAdapter<CarModel, MyViewHolder> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Car");
        inputSearch = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        floatingActionButton = findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        LoadData("");

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null){
                    LoadData(s.toString());
                }else {
                    LoadData("");
                }
            }
        });
    }

    private void LoadData(String data) {
        Query query = databaseReference.orderByChild("CarName").startAt(data).endAt(data+ "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<CarModel>().setQuery(query, CarModel.class).build();
        adapter = new FirebaseRecyclerAdapter<CarModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull CarModel model) {
                holder.textView.setText(model.getCarName());
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, ViewActivity.class);
                        intent.putExtra("CarKey", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}