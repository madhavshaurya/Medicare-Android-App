package com.example.medicalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText fullNameEditText, dobEditText, phoneNumberEditText;
    private Button editButton;

    private Button logoutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make the activity full-screen (no status bar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        fullNameEditText = findViewById(R.id.fullNameEditText);
        dobEditText = findViewById(R.id.dobEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        editButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logoutButton);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://medicare-5ad77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("PatientMedicalInfo");

        loadUserData();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the edit activity
                startActivity(new Intent(ProfileActivity.this, HistoryEditActivity.class));
            }
        });

        // Set click listener for Logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Logout button click
                FirebaseAuth.getInstance().signOut();
                clearCachedData();
                navigateToMainActivity();
            }
        });
    }


    private void loadUserData() {
        if (currentUser != null) {
            DatabaseReference userReference = databaseReference.child(currentUser.getUid());
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PatientMedicalInfo medicalInfo = snapshot.getValue(PatientMedicalInfo.class);
                    if (medicalInfo != null) {
                        fullNameEditText.setText(medicalInfo.getFullName());
                        dobEditText.setText(medicalInfo.getDateOfBirth());
                        phoneNumberEditText.setText(medicalInfo.getPhoneNumber());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }
    private void navigateToMainActivity() {
        Intent intent = new Intent(ProfileActivity.this, IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear all previous activities
        startActivity(intent);
        finish(); // Finish the current activity
    }
    private void clearCachedData() {
        // Clear any locally cached data related to the user's session
        // For example, if you're using SharedPreferences, clear them like this:
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all stored preferences
        editor.apply(); // Apply changes

        // After clearing cached data, navigate to the login screen
        navigateToMainActivity();
    }

}
