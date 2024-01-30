package com.example.medicalapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class HistoryEditActivity extends AppCompatActivity {

    private EditText fullNameEdt, dobEdt, phoneNumberEdt, emailEdt, addressEdt,
            emergencyNameEdt, relationshipEdt, emergencyPhoneNumberEdt,
            currentMedicationsEdt, pastSurgeriesEdt, chronicConditionsEdt,
            currentSymptomsEdt, painDescriptionEdt, additionalInformationEdt;

    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton, otherRadioButton;

    private CheckBox allergiesCheckBox, asthmaCheckBox, diabetesCheckBox,
            heartDiseaseCheckBox, highBloodPressureCheckBox;
    private EditText otherMedicalHistoryEdt;

    private CheckBox consentCheckBox;
    private Button submitFormBtn;

    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make the activity full-screen (no status bar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_historyedit);


        // Initialize Firebase Authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Initialize Firebase
        String customDatabaseUrl = "https://medicare-5ad77-default-rtdb.asia-southeast1.firebasedatabase.app/";
        firebaseDatabase = FirebaseDatabase.getInstance(customDatabaseUrl);
        databaseReference = firebaseDatabase.getReference("PatientMedicalInfo");

        // Initialize views
        fullNameEdt = findViewById(R.id.idEdtFullName);
        dobEdt = findViewById(R.id.idEdtDateOfBirth);
        genderRadioGroup = findViewById(R.id.idRadioGroupGender);
        maleRadioButton = findViewById(R.id.idRadioMale);
        femaleRadioButton = findViewById(R.id.idRadioFemale);
        otherRadioButton = findViewById(R.id.idRadioOther);
        phoneNumberEdt = findViewById(R.id.idEdtPhoneNumber);
        emailEdt = findViewById(R.id.idEdtEmail);
        addressEdt = findViewById(R.id.idEdtAddress);
        emergencyNameEdt = findViewById(R.id.idEdtEmergencyName);
        relationshipEdt = findViewById(R.id.idEdtRelationship);
        emergencyPhoneNumberEdt = findViewById(R.id.idEdtEmergencyPhoneNumber);
        allergiesCheckBox = findViewById(R.id.idChkAllergies);
        asthmaCheckBox = findViewById(R.id.idChkAsthma);
        diabetesCheckBox = findViewById(R.id.idChkDiabetes);
        heartDiseaseCheckBox = findViewById(R.id.idChkHeartDisease);
        highBloodPressureCheckBox = findViewById(R.id.idChkHighBloodPressure);
        otherMedicalHistoryEdt = findViewById(R.id.idEdtOtherMedicalHistory);
        currentMedicationsEdt = findViewById(R.id.idEdtCurrentMedications);
        pastSurgeriesEdt = findViewById(R.id.idEdtPastSurgeries);
        chronicConditionsEdt = findViewById(R.id.idEdtChronicConditions);
        currentSymptomsEdt = findViewById(R.id.idEdtCurrentSymptoms);
        painDescriptionEdt = findViewById(R.id.idEdtPainDescription);
        additionalInformationEdt = findViewById(R.id.idEdtAdditionalInformation);
        consentCheckBox = findViewById(R.id.idChkConsent);
        submitFormBtn = findViewById(R.id.idBtnSendData);

        // Set click listener for Submit button
        submitFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is authenticated
                if (currentUser == null) {
                    Toast.makeText(HistoryEditActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate and retrieve data from the form
                String fullName = fullNameEdt.getText().toString().trim();
                String dateOfBirth = dobEdt.getText().toString().trim();
                String gender = getSelectedGender();
                String phoneNumber = phoneNumberEdt.getText().toString().trim();
                String email = emailEdt.getText().toString().trim();
                String address = addressEdt.getText().toString().trim();
                String emergencyName = emergencyNameEdt.getText().toString().trim();
                String relationship = relationshipEdt.getText().toString().trim();
                String emergencyPhoneNumber = emergencyPhoneNumberEdt.getText().toString().trim();
                String medicalHistory = getMedicalHistory();
                String otherMedicalHistory = otherMedicalHistoryEdt.getText().toString().trim();
                String currentMedications = currentMedicationsEdt.getText().toString().trim();
                String pastSurgeries = pastSurgeriesEdt.getText().toString().trim();
                String chronicConditions = chronicConditionsEdt.getText().toString().trim();
                String currentSymptoms = currentSymptomsEdt.getText().toString().trim();
                String painDescription = painDescriptionEdt.getText().toString().trim();
                String additionalInformation = additionalInformationEdt.getText().toString().trim();

                // Validate form data
                if (fullName.isEmpty() || dateOfBirth.isEmpty() || gender.isEmpty() ||
                        phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty() ||
                        emergencyName.isEmpty() || relationship.isEmpty() || emergencyPhoneNumber.isEmpty() ||
                        medicalHistory.isEmpty()) {
                    Toast.makeText(HistoryEditActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a unique reference for each patient based on their UID
                DatabaseReference patientNode = databaseReference.child(currentUser.getUid());

                // Create a PatientMedicalInfo object and populate its fields
                PatientMedicalInfo medicalInfo = new PatientMedicalInfo();
                medicalInfo.setFullName(fullName);
                medicalInfo.setDateOfBirth(dateOfBirth);
                medicalInfo.setGender(gender);
                medicalInfo.setPhoneNumber(phoneNumber);
                medicalInfo.setEmail(email);
                medicalInfo.setAddress(address);
                medicalInfo.setEmergencyContactName(emergencyName);
                medicalInfo.setEmergencyContactRelationship(relationship);
                medicalInfo.setEmergencyContactPhoneNumber(emergencyPhoneNumber);
                medicalInfo.setMedicalHistory(medicalHistory);
                medicalInfo.setOtherMedicalHistory(otherMedicalHistory);
                medicalInfo.setCurrentMedications(currentMedications);
                medicalInfo.setPastSurgeries(pastSurgeries);
                medicalInfo.setChronicConditions(chronicConditions);
                medicalInfo.setCurrentSymptoms(currentSymptoms);
                medicalInfo.setPainDescription(painDescription);
                medicalInfo.setAdditionalInformation(additionalInformation);

                // Use setValue() to store the patient's medical info in Firebase
                patientNode.setValue(medicalInfo, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@NonNull DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(HistoryEditActivity.this, "Medical form submitted successfully", Toast.LENGTH_SHORT).show();
                            clearFormFields();
                        } else {
                            Toast.makeText(HistoryEditActivity.this, "Failed to submit medical form: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // Set click listener for Date of Birth field
        dobEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private String getSelectedGender() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == maleRadioButton.getId()) {
            return "Male";
        } else if (selectedId == femaleRadioButton.getId()) {
            return "Female";
        } else if (selectedId == otherRadioButton.getId()) {
            return "Other";
        } else {
            return "";
        }
    }

    private String getMedicalHistory() {
        StringBuilder medicalHistory = new StringBuilder();
        if (allergiesCheckBox.isChecked()) {
            medicalHistory.append("Allergies, ");
        }
        // ... (Continuation of getMedicalHistory)
        if (asthmaCheckBox.isChecked()) {
            medicalHistory.append("Asthma, ");
        }
        if (diabetesCheckBox.isChecked()) {
            medicalHistory.append("Diabetes, ");
        }
        if (heartDiseaseCheckBox.isChecked()) {
            medicalHistory.append("Heart Disease, ");
        }
        if (highBloodPressureCheckBox.isChecked()) {
            medicalHistory.append("High Blood Pressure, ");
        }
        String otherHistory = otherMedicalHistoryEdt.getText().toString().trim();
        if (!otherHistory.isEmpty()) {
            medicalHistory.append(otherHistory);
        }

        // Remove trailing comma and spaces
        if (medicalHistory.length() > 0 && medicalHistory.charAt(medicalHistory.length() - 1) == ' ') {
            medicalHistory.delete(medicalHistory.length() - 2, medicalHistory.length());
        }

        return medicalHistory.toString();
    }

    private void clearFormFields() {
        // Clear all form fields here
        fullNameEdt.getText().clear();
        dobEdt.getText().clear();
        genderRadioGroup.clearCheck();
        phoneNumberEdt.getText().clear();
        emailEdt.getText().clear();
        addressEdt.getText().clear();
        emergencyNameEdt.getText().clear();
        relationshipEdt.getText().clear();
        emergencyPhoneNumberEdt.getText().clear();
        allergiesCheckBox.setChecked(false);
        asthmaCheckBox.setChecked(false);
        diabetesCheckBox.setChecked(false);
        heartDiseaseCheckBox.setChecked(false);
        highBloodPressureCheckBox.setChecked(false);
        otherMedicalHistoryEdt.getText().clear();
        currentMedicationsEdt.getText().clear();
        pastSurgeriesEdt.getText().clear();
        chronicConditionsEdt.getText().clear();
        currentSymptomsEdt.getText().clear();
        painDescriptionEdt.getText().clear();
        additionalInformationEdt.getText().clear();
        consentCheckBox.setChecked(false);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(HistoryEditActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    dobEdt.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
