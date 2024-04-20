package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class login extends AppCompatActivity {

    TextInputEditText edEmail, edPassword;
    //Initialize the Database Instance
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        GifImageView gifImageView = findViewById(R.id.gifImageView);

        // Define the URL or resource ID of the GIF image
        String gifUrl = "https://tenor.com/view/kfc-fried-chicken-kentucky-fried-chicken-food-yummy-gif-17870467";

        // Create a Picasso target to load the GIF into the GifImageView
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(android.graphics.Bitmap bitmap, Picasso.LoadedFrom from) {
                // Load the bitmap into the GifImageView as a GifDrawable
                try {
                    gifImageView.setImageDrawable(new GifDrawable(bitmap.getNinePatchChunk()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, android.graphics.drawable.Drawable errorDrawable) {
                // Handle bitmap loading failure
            }

            @Override
            public void onPrepareLoad(android.graphics.drawable.Drawable placeHolderDrawable) {
                // Prepare loading, if needed
            }
        };

        // Use Picasso to load the GIF into the custom target
        Picasso.get().load(gifUrl).into(target);

        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password from EditText fields
                String email = edEmail.getText().toString().trim();
                String password = edPassword.getText().toString().trim();

                // Validate email
                if (!isValidEmail(email)) {
                    edEmail.setError("Invalid email address");
                    return;
                }

                // Perform login logic here
                // For simplicity, just show a toast message
                Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
            }

            private boolean isValidEmail(String email) {
                // Use Patterns.EMAIL_ADDRESS to validate email format
                return Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }
        });

        //Hooks
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);


        //Escape to the Dashboard Activity if the user is already loggedin
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (SharedPrefManager.getInstance(this).getRole().equals("admin")) {
                startActivity(new Intent(this, AdminDashboard.class));
            } else {
                startActivity(new Intent(this, UserDashboard.class));
            }
            finish();
        }

        ProgressDialog progressDialog = new ProgressDialog(this);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, Register.class));
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display the progress dialog
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                //get the text from the input fields
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "One of the field is empty", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {

                    //Checking is the user with that email exists
                    Query query = databaseReference.child("users").orderByChild("email").equalTo(email);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //This means that the user with that email exists

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    // loops through each attributes of a user from the id up to
                                    // the date but first lets begin with the password
                                    String getPassword = dataSnapshot.child("password").getValue(String.class);
                                    if (password.equals(getPassword)) {
                                        Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                        // Now get the user id and the role and store them using the Shared Preference Manager
                                        String getId = dataSnapshot.child("id").getValue(String.class);
                                        String getRole = dataSnapshot.child("role").getValue(String.class);

                                        SharedPrefManager.getInstance(login.this).userLogin(getId, getRole);
                                        // The most exiting part. Route the logged in user to different activities once
                                        // they are loggedin based on their roles.
                                        //Make sure to have the USerDashboard and AdminDashboard created
                                        if (getRole.equals("admin")) {
                                            startActivity(new Intent(login.this, AdminDashboard.class));
                                            finish();
                                        } else {
                                            startActivity(new Intent(login.this, UserDashboard.class));
                                            finish();
                                        }

                                        progressDialog.dismiss();
                                    } else {
                                        // If a wrong password was entered
                                        Toast.makeText(login.this, "Login Failed. Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                }

                            } else {
                                // This means that the user with that email doesn't exist
                                Toast.makeText(login.this, "User doesn't exist. Kindly Register first to proceed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                }
            }
        });


        // Find the GifImageView

    }
}