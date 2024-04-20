package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UserDashboard extends AppCompatActivity {



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_dashboard);

            Toast.makeText(this, "Role: "+SharedPrefManager.getInstance(this).getRole(), Toast.LENGTH_SHORT).show();


            findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserDashboard.this);
                    //title
                    alertDialogBuilder.setTitle("Confirm");
                    //message
                    alertDialogBuilder.setMessage("Are you sure you want to logout?");

                    alertDialogBuilder.setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPrefManager.getInstance(UserDashboard.this).logOut();
                                    Toast.makeText(UserDashboard.this, "Logged Out Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserDashboard.this,login.class));
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
                    AlertDialog alertDialog=alertDialogBuilder.create();

                    //show the dialog
                    alertDialog.show();
                }
            });

            Button loginButton = findViewById(R.id.buttonViewmakeorders);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open the login activity
                    Intent intent = new Intent(UserDashboard.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            Button viewOrdersButton = findViewById(R.id.buttonViewOrders);
            viewOrdersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open the activity to view orders
                    Intent intent = new Intent(UserDashboard.this, ViewMyOrders.class);
                    startActivity(intent);
                }
            });

            findViewById(R.id.buttonMyCart).setOnClickListener(v->{
                startActivity(new Intent(UserDashboard.this,CartActivity.class));
            });

        }

    }