package com.example.kaimosifriendsuniversityordersanddeliveryapplication;

import android.os.Build;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static DatabaseReference getDb() {
        return FirebaseDatabase.getInstance().getReference();
    }

    // Function to format input date
    public static String formatDate(String inputDate) {
        String formatDate = null;
        // Define input and output date formats
        DateTimeFormatter inputFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            inputFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        }
        DateTimeFormatter outputFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            outputFormat = DateTimeFormatter.ofPattern("d MMMM, yyyy");
        }

        try {
            // Parse the input date string to LocalDateTime
            LocalDateTime date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                date = LocalDateTime.parse(inputDate, inputFormat);
            }
            // Format the date to the desired output format
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatDate = outputFormat.format(date);
            }
            return formatDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Date";
        }
    }

    public String getDate() {
        String date = null;

        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        }
        LocalDateTime now = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = dtf.format(now);
        }

        return date;
    }
}
