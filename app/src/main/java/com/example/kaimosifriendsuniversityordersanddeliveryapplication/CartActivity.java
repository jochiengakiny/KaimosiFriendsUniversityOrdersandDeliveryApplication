package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CartAdapter adapter;
    ArrayList<CartModel> list = new ArrayList<>();
    TextView txtTotal;
    double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerView);
        txtTotal = findViewById(R.id.total);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(list, this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.order).setOnClickListener(v->{
            Intent intent=new Intent(CartActivity.this, Order.class);
            intent.putExtra("total",totalAmount+"");
            startActivity(intent);
        });

        getCartDetails(SharedPrefManager.getInstance(this).getUserId());
    }

    private void getCartDetails(String userId) {
        Query query = Constants.getDb().child("cart").child(
                SharedPrefManager.getInstance(this).getUserId()
        );
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    totalAmount = 0.0; // Reset total amount
                    list.clear(); // Clear the list before adding items
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartModel cart = dataSnapshot.getValue(CartModel.class);
                        list.add(cart);
                        calculate(cart);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculate(CartModel cart) {
        Constants.getDb().child("products").child(cart.getMealId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    MealModel model = snapshot.getValue(MealModel.class);
                    if (model != null) {
                        double price = Double.parseDouble(model.getPrice());
                        double quantity = Double.parseDouble(cart.getQuantity());
                        totalAmount += price * quantity;
                        txtTotal.setText("Ksh. " + totalAmount);
                        // Toast.makeText(CartActivity.this, "Total: " + totalAmount, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}