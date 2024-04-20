package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<MealModel> productModels = new ArrayList<>();
    FoodAdapter productsAdapter;
    MaterialToolbar toolbar;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productsAdapter = new FoodAdapter(productModels, this);

        recyclerView.setAdapter(productsAdapter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button loginButton = findViewById(R.id.login);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            loginButton.setVisibility(View.VISIBLE);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the login activity
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        productModels.clear();
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MealModel productModel = dataSnapshot.getValue(MealModel.class);
                        productModels.add(productModel);
                        productsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Failed to fetch", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mn_cart) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
            return true;
        } else if (item.getItemId() == R.id.mn_profile) {
            if (SharedPrefManager.getInstance(MainActivity.this).isLoggedIn()) {
                if (SharedPrefManager.getInstance(MainActivity.this).getRole().equals("admin"))
                    startActivity(new Intent(MainActivity.this, AdminDashboard.class));
                else
                    startActivity(new Intent(MainActivity.this, UserDashboard.class));
            } else {
                startActivity(new Intent(MainActivity.this, login.class));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}