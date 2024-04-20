package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    TextInputEditText edEmail, edPassword, edConfirmPassword;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hooks
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);

        ProgressDialog progressDialog = new ProgressDialog(this);

        // Set onClick listener to the Register Button
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Terminates the current register activity
            }
        });

        // Set onClick listener to the Register Button
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display the progress dialog
                progressDialog.setMessage("Registering User...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                //Get a unique id from Firebase
                String id = databaseReference.push().getKey();
                //get the text from the input fields
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();

                // Check if email is valid
                if (!isValidEmail(email)) {
                    edEmail.setError("Invalid email address");
                    progressDialog.dismiss();
                    return;
                }

                // Check if password length is exactly 5 characters
                if (password.length() != 5) {
                    edPassword.setError("Password must be exactly 5 characters long");
                    progressDialog.dismiss();
                    return;
                }

                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Register.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "The passwords do not match", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    UserModel user = new UserModel(id, email, password, new Constants().getDate(), "student");

                    // Ensures that a user with the same email cannot register more than once
                    Query query = databaseReference.child("users").orderByChild("email").equalTo(email);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(Register.this, "That email has already been taken", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                databaseReference.child("users").child(id).setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    // Method to validate email address using Android Patterns class
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}