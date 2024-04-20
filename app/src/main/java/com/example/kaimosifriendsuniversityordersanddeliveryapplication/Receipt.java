package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Receipt extends AppCompatActivity {

    private TextView tvOrderId, tvItem1, tvTotalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        // Initialize TextViews
        tvOrderId = findViewById(R.id.tvOrderId);
        tvItem1 = findViewById(R.id.tvItem1);
        tvTotalCost = findViewById(R.id.tvTotalCost);

        // Retrieve data from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String orderId = intent.getStringExtra("orderId");
            String item1 = intent.getStringExtra("item1");
            // Retrieve more items if needed

            // Display order details
            tvOrderId.setText("Order ID: #" + orderId);
            tvItem1.setText(item1);
            // Set more TextViews for other order details

            // Calculate and display total cost
            double totalCost = calculateTotalCost(); // Implement this method to calculate total cost
            tvTotalCost.setText("Total: $" + String.format("%.2f", totalCost));
        }
    }

    // Method to calculate total cost
    private double calculateTotalCost() {
        // Implement your logic to calculate the total cost based on item prices and quantities
        return 0.0; // Placeholder value
    }
}