package com.example.medicalapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryActivity extends AppCompatActivity {

    private TextView fullNameTxt, dobTxt, genderTxt, phoneNumberTxt, emailTxt, addressTxt,
            emergencyNameTxt, relationshipTxt, emergencyPhoneNumberTxt,
            medicalHistoryTxt, otherMedicalHistoryTxt, currentMedicationsTxt,
            pastSurgeriesTxt, chronicConditionsTxt, currentSymptomsTxt,
            painDescriptionTxt, additionalInformationTxt;

    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make the activity full-screen (no status bar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_history);

        // Initialize Firebase Authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Initialize Firebase
        String customDatabaseUrl = "https://medicare-5ad77-default-rtdb.asia-southeast1.firebasedatabase.app/";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(customDatabaseUrl);
        databaseReference = firebaseDatabase.getReference("PatientMedicalInfo");

        // Initialize views
        fullNameTxt = findViewById(R.id.idTxtFullName);
        dobTxt = findViewById(R.id.idTxtDateOfBirth);
        genderTxt = findViewById(R.id.idTxtGender);
        phoneNumberTxt = findViewById(R.id.idTxtPhoneNumber);
        emailTxt = findViewById(R.id.idTxtEmail);
        addressTxt = findViewById(R.id.idTxtAddress);
        emergencyNameTxt = findViewById(R.id.idTxtEmergencyName);
        relationshipTxt = findViewById(R.id.idTxtRelationship);
        emergencyPhoneNumberTxt = findViewById(R.id.idTxtEmergencyPhoneNumber);
        medicalHistoryTxt = findViewById(R.id.idTxtMedicalHistory);
        otherMedicalHistoryTxt = findViewById(R.id.idTxtOtherMedicalHistory);
        currentMedicationsTxt = findViewById(R.id.idTxtCurrentMedications);
        pastSurgeriesTxt = findViewById(R.id.idTxtPastSurgeries);
        chronicConditionsTxt = findViewById(R.id.idTxtChronicConditions);
        currentSymptomsTxt = findViewById(R.id.idTxtCurrentSymptoms);
        painDescriptionTxt = findViewById(R.id.idTxtPainDescription);
        additionalInformationTxt = findViewById(R.id.idTxtAdditionalInformation);

        Button createModifyButton = findViewById(R.id.idBtnCreateModify);
        createModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to HistoryEditActivity
                Intent intent = new Intent(HistoryActivity.this, HistoryEditActivity.class);
                startActivity(intent);
            }
        });

        // Check if the user is authenticated
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve and display the user's medical history using the User UID
        String userUid = currentUser.getUid();
        DatabaseReference userNode = databaseReference.child(userUid);
        userNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PatientMedicalInfo medicalInfo = dataSnapshot.getValue(PatientMedicalInfo.class);
                    displayMedicalInfo(medicalInfo);
                } else {
                    medicalHistoryTxt.setText("No data present. Please create a medical history.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryActivity.this, "Error retrieving data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMedicalInfo(PatientMedicalInfo medicalInfo) {
        fullNameTxt.setText(medicalInfo.getFullName());
        dobTxt.setText(medicalInfo.getDateOfBirth());
        genderTxt.setText(medicalInfo.getGender());
        phoneNumberTxt.setText(medicalInfo.getPhoneNumber());
        emailTxt.setText(medicalInfo.getEmail());
        addressTxt.setText(medicalInfo.getAddress());
        emergencyNameTxt.setText(medicalInfo.getEmergencyContactName());
        relationshipTxt.setText(medicalInfo.getEmergencyContactRelationship());
        emergencyPhoneNumberTxt.setText(medicalInfo.getEmergencyContactPhoneNumber());
        medicalHistoryTxt.setText(medicalInfo.getMedicalHistory());
        otherMedicalHistoryTxt.setText(medicalInfo.getOtherMedicalHistory());
        currentMedicationsTxt.setText(medicalInfo.getCurrentMedications());
        pastSurgeriesTxt.setText(medicalInfo.getPastSurgeries());
        chronicConditionsTxt.setText(medicalInfo.getChronicConditions());
        currentSymptomsTxt.setText(medicalInfo.getCurrentSymptoms());
        painDescriptionTxt.setText(medicalInfo.getPainDescription());
        additionalInformationTxt.setText(medicalInfo.getAdditionalInformation());
    }
}
