package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {
    final Context context;
    final ArrayList<OrderModel> orders;

    public OrdersAdapter(Context context, ArrayList<OrderModel> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.MyViewHolder holder, int position) {
        OrderModel currentItem = orders.get(position);
        holder.edDate.setText(Constants.formatDate(currentItem.getDate()));
        holder.edPrice.setText("Total Price: Kshs " + currentItem.getTotal());
        holder.edLocation.setText("Location: " + currentItem.getLocation());
        holder.edApproved.setText("Status: " + currentItem.getApproved());

        // Clear the existing views in the LinearLayout
        // Clear the existing views in the LinearLayout
        holder.linearLayout.removeAllViews();

        // Split the meal IDs by commas
        String[] mealIds = currentItem.getMeals().split(",");

        // Add TextViews for each meal ID
        for (String mealId : mealIds) {
            TextView textView = new TextView(context);
            getMealDetails(textView, mealId);
            // Add other styling if needed
            holder.linearLayout.addView(textView);
        }

        holder.details.setOnLongClickListener(v -> {
            if (SharedPrefManager.getInstance(context).getRole().equals("admin")) {
                PopupMenu popupMenu = new PopupMenu(context, holder.details);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    // Toast message on menu item clicked
                    if (menuItem.getItemId() == R.id.mn_approve) {
                        Constants.getDb().child("orders").child(currentItem.getUserId()).child(currentItem.getOrderId())
                                .child("approved").setValue("Approved");
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(currentItem.getPhone(), null, "Your order has been approved. Kindly wait for about 10 mins for you delivery", null, null);

                        Toast.makeText(context, "Order Approved", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });
                // Showing the popup menu
                popupMenu.show();
            }

            return true;
        });

    }

    private void getMealDetails(TextView textView, String mealId) {
        Constants.getDb().child("products").child(mealId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    MealModel model = snapshot.getValue(MealModel.class);
                    if (model != null) {
                        textView.setText(model.getName());
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
        return orders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView edDate, edPrice, edLocation, edApproved;
        LinearLayout linearLayout;
        MaterialCardView details;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            edDate = itemView.findViewById(R.id.date);
            edPrice = itemView.findViewById(R.id.price);
            linearLayout = itemView.findViewById(R.id.mealsOrderedLayout);
            edApproved = itemView.findViewById(R.id.approved);
            edLocation = itemView.findViewById(R.id.location);
            details = itemView.findViewById(R.id.details);
        }
    }
}
