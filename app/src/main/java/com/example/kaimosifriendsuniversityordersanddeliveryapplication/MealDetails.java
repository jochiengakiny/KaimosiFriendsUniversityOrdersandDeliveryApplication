package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MealDetails extends AppCompatActivity {
    String mealId;
    TextView txtName, txtDescription, txtPrice, txtCategory, txtQuantity;
    ImageView imageView;

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        ImageView imageView1 = findViewById(R.id.arrow);

        Intent intent = getIntent();

        mealId = intent.getStringExtra("id");

        txtQuantity = findViewById(R.id.quantity);
        txtQuantity.setText(quantity + "");

        findViewById(R.id.plus).setOnClickListener(v -> {
            quantity = quantity + 1;
            if (quantity >= 10) {
                quantity = 10;
            }
            txtQuantity.setText(quantity + "");
        });

        findViewById(R.id.minus).setOnClickListener(v -> {
            quantity = quantity - 1;
            if (quantity <= 0) {
                quantity = 1;
            }
            txtQuantity.setText(quantity + "");
        });

        imageView1.setOnClickListener(v -> finish());
        MaterialButton cartButton = findViewById(R.id.cart);

        cartButton.setOnClickListener(v -> {
            // Start the new activity
            Intent intent1 = new Intent(MealDetails.this, CartActivity.class);
            startActivity(intent1);
        });
        Button button = findViewById(R.id.btnAddToCart);

        button.setOnClickListener(v -> {
            // Start the new activity
            //  Query query=Constants.getDb().child("cart")
            if (SharedPrefManager.getInstance(MealDetails.this).isLoggedIn()) {
                CartModel cartModel = new CartModel(
                        mealId,
                        quantity + "",
                        new Constants().getDate(),
                        SharedPrefManager.getInstance(MealDetails.this).getUserId()
                );
                Constants.getDb().child("cart").child(
                                SharedPrefManager.getInstance(MealDetails.this).getUserId()
                        ).child(mealId).setValue(cartModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MealDetails.this, "Cart Updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MealDetails.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {

                Toast.makeText(MealDetails.this, "please login", Toast.LENGTH_SHORT).show();
                // Start the login activity
                Intent intent12 = new Intent(MealDetails.this, login.class);
                startActivity(intent12);
            }
        });

        txtName = findViewById(R.id.name);
        txtDescription = findViewById(R.id.description);
        txtPrice = findViewById(R.id.price);
        txtCategory = findViewById(R.id.category);
        imageView = findViewById(R.id.image);


        getMealDetails(mealId);

    }

    private void getMealDetails(String mealId) {
        Query query = Constants.getDb().child("products").orderByChild("id").equalTo(mealId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        MealModel mealModel = productSnapshot.getValue(MealModel.class);

                        txtName.setText(mealModel.getName());
                        txtPrice.setText(mealModel.getPrice());
                        txtCategory.setText(mealModel.getCategory());
                        txtDescription.setText(mealModel.getDescription());
                        Picasso.get().load(mealModel.getImage()).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MealDetails.this, "Failed to fetch", Toast.LENGTH_SHORT).show();
            }
        });
    }
}