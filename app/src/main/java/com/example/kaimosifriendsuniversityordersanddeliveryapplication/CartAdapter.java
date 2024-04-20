package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private final ArrayList<CartModel> productModels;
    private final Context context;

    public CartAdapter(ArrayList<CartModel> productModels, Context context) {
        this.productModels = productModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_cart_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartModel currentItem = productModels.get(position);
        holder.txtName.setText(currentItem.getMealId());
        
        holder.btnRemove.setOnClickListener(v->{
            Constants.getDb().child("cart").child(SharedPrefManager.getInstance(context).getUserId())
                    .child(currentItem.getMealId()).removeValue()
                    .addOnSuccessListener(unused -> {
                        notifyDataSetChanged();
                Toast.makeText(context, "Item Removed from cart", Toast.LENGTH_SHORT).show();
            })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    });
        });

        Constants.getDb().child("products").child(currentItem.getMealId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    MealModel meal = snapshot.getValue(MealModel.class);
                    if (meal != null) {
                        holder.txtName.setText(meal.getName());
                        holder.txtPrice.setText("Ksh. " + meal.getPrice());
                        holder.txtQuantity.setText("Quantity: " + currentItem.getQuantity());
                        Picasso.get().load(meal.getImage()).into(holder.imageView);
                        int totalAmount = Integer.parseInt(currentItem.getQuantity()) * Integer.parseInt(meal.getPrice());
                        holder.txtTotal.setText("Kshs. " + totalAmount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        MaterialTextView txtName, txtPrice, txtQuantity, txtTotal;
        MaterialButton btnRemove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.imageView);
            txtName = itemView.findViewById(R.id.mealName);
            txtPrice = itemView.findViewById(R.id.mealPrice);
            txtQuantity = itemView.findViewById(R.id.mealQuantity);
            txtTotal = itemView.findViewById(R.id.total);
            btnRemove=itemView.findViewById(R.id.btnRemove);
        }
    }
}

