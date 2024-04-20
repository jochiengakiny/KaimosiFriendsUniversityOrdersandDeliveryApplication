package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    final ArrayList<MealModel> productModels;
    final Context context;

    public FoodAdapter(ArrayList<MealModel> productModels, Context context) {
        this.productModels = productModels;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_meal_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        MealModel currentItem = productModels.get(position);
        holder.txtname.setText(currentItem.getName());
        holder.txtCategory.setText(currentItem.getCategory());

        // Concatenate the price integer value with the Kshs. tag
        String price = "Kshs. " + currentItem.getPrice() + ".00";
        holder.txtPrice.setText(price);
        holder.txtCategory.setText(currentItem.getCategory());

        // Use Picasso library to load the image
        Picasso.get().load(currentItem.getImage()).into(holder.imageView);
        holder.imageView.setOnClickListener(v ->{
            Intent intent=new Intent(context, MealDetails.class);
            intent.putExtra("id",currentItem.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardView;
        MaterialTextView txtname, txtPrice, txtCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.img_product_image);
            txtname = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtCategory = itemView.findViewById(R.id.txt_category);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}