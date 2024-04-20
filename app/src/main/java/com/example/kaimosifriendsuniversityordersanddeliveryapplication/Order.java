package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Order extends AppCompatActivity {
    TextView total;
    ArrayList<CartModel> list = new ArrayList<>();
    TextInputEditText edLocation, edPhone;
    String mealIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        total = findViewById(R.id.total);
        total.setText(getIntent().getStringExtra("total"));

        if (getIntent().getStringExtra("total").equals("0.0")) {
            Toast.makeText(this, "You have no products in cart", Toast.LENGTH_SHORT).show();
            finish();
        }

        edLocation = findViewById(R.id.location);
        edPhone = findViewById(R.id.edphone);

        findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edPhone.getText().toString().trim();
                String location = edLocation.getText().toString().trim();
                String id = Constants.getDb().push().getKey();

                Toast.makeText(Order.this, "Meal Ids: " + mealIds, Toast.LENGTH_SHORT).show();

                if (ContextCompat.checkSelfPermission(Order.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    if (phone.isEmpty() || location.isEmpty()) {
                        Toast.makeText(Order.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        ProgressDialog progressDialog = new ProgressDialog(Order.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        OrderModel orderModel = new OrderModel(id, SharedPrefManager.getInstance(Order.this).getUserId(), phone, mealIds, location, getIntent().getStringExtra("total"), new Constants().getDate(), "Pending");

                        Constants.getDb().child("orders").child(
                                        SharedPrefManager.getInstance(Order.this).getUserId()
                                ).child(id).setValue(orderModel)
                                .addOnSuccessListener(unused -> {
                                    progressDialog.dismiss();

                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(phone, null, "Order placed successfully and it will be delivered to the following location:" + location, null, null);

                                    Toast.makeText(Order.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                    Constants.getDb().child("cart").child(
                                            SharedPrefManager.getInstance(Order.this).getUserId()
                                    ).removeValue();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(Order.this, "Failed", Toast.LENGTH_SHORT).show();
                                });

                    }
                } else {
                    ActivityCompat.requestPermissions(Order.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pullMealsFromCart(SharedPrefManager.getInstance(Order.this).getUserId());
    }

    private void pullMealsFromCart(String userId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Constants.getDb().child("cart").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                if (snapshot.exists()) {
                    list.clear(); // Clear the list before adding items
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartModel cart = dataSnapshot.getValue(CartModel.class);
                        list.add(cart);
                    }
                    StringBuilder mealIdsBuilder = new StringBuilder();

                    for (CartModel cart : list) {
                        mealIdsBuilder.append(cart.getMealId()).append(",");
                    }

                    // Remove the trailing comma
                    mealIds = mealIdsBuilder.toString();
                    if (mealIds.endsWith(",")) {
                        mealIds = mealIds.substring(0, mealIds.length() - 1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(Order.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}