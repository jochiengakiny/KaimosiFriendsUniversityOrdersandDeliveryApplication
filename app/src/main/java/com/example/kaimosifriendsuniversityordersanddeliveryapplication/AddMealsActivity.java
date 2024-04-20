package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddMealsActivity extends AppCompatActivity {

    ImageView imgProductImage;
    TextInputEditText txtProductName, txtProductPrice,txtDescription;
    MaterialAutoCompleteTextView dropdownProductCategory;

    //Holds the value of the selected category
    private String selectedCategory;

    // Set up the database reference
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri uri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meals);


//        Hooks
        imgProductImage = findViewById(R.id.productImage);
        dropdownProductCategory = findViewById(R.id.dpProductCategory);
        txtProductName = findViewById(R.id.edProductName);
        txtProductPrice = findViewById(R.id.edPrice);
        txtDescription = findViewById(R.id.edDescription);

        // Logic for selecting an image
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uri = (data != null) ? data.getData() : null;
                        imgProductImage.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imgProductImage.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        // Setting up the dropdown
        // An array to hold the product categories
        String[] categories = {"Breakfast", "Soft Drinks", "Main Foods"};
        dropdownProductCategory.setSimpleItems(categories);
        // Initialising the selected category value
        dropdownProductCategory.setText(categories[0], false);
        selectedCategory = categories[0];
        dropdownProductCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory=categories[position];
            }
        });

        // functionality of now adding the products to database
        findViewById(R.id.btnAddProduct).setOnClickListener(v -> uploadProduct());
    }

    private void uploadProduct() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Product");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Save the image URL or perform any additional logic if needed
        String uniqueId = databaseReference.push().getKey();
        String productName = txtProductName.getText().toString().trim();
        String productPrice = txtProductPrice.getText().toString().trim();
        String description = txtDescription.getText().toString().trim();

        if (uri != null && !productPrice.isEmpty() && !productName.isEmpty()) {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("products")
                    .child(uri.getLastPathSegment());

            storageReference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        uriTask.addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri getImageUrl = task.getResult();
                                MealModel mealModel = new MealModel(uniqueId, productName, productPrice, selectedCategory,description, getImageUrl.toString(), new Constants().getDate());

                                databaseReference.child("products").child(uniqueId)
                                        .setValue(mealModel)
                                        .addOnSuccessListener(aVoid -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddMealsActivity.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(
                                                    AddMealsActivity.this,
                                                    "Failed. Kindly try again later",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddMealsActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                            }
                        });

                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(AddMealsActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(AddMealsActivity.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
        }
    }
}