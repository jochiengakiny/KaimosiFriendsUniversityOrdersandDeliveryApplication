package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminDashboard.this);
                //title
                alertDialogBuilder.setTitle("Confirm");
                //message
                alertDialogBuilder.setMessage("Are you sure you want to logout?");

                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPrefManager.getInstance(AdminDashboard.this).logOut();
                                Toast.makeText(AdminDashboard.this, "Logged Out Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminDashboard.this, login.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                // create the alertDialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                //show the dialog
                alertDialog.show();
            }
        });

        Button loginButton = findViewById(R.id.buttonViewOrders);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the login activity
                Intent intent = new Intent(AdminDashboard.this, ViewAllOrders.class);
                startActivity(intent);
            }
        });


        Button buttonAddmeal = findViewById(R.id.buttonAddmeal);

        buttonAddmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the login activity
                Intent intent = new Intent(AdminDashboard.this, AddMealsActivity.class);
                startActivity(intent);
            }
        });
        Button buttonManageUsers = findViewById(R.id.buttonAddmeal);

        buttonManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the login activity
                Intent intent = new Intent(AdminDashboard.this, AddMealsActivity.class);
                startActivity(intent);
            }
        });
    }
}