package com.example.medicalapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private Spinner spinnerDepartment;
    private Spinner spinnerDoctor;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText editTextReason;
    private Button buttonSubmit;

    private HashMap<String, List<String>> doctorMap;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity full-screen (no status bar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schedule);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Get the user's UID
        String uid = firebaseAuth.getUid();
        if (uid == null) {
            // Handle case where user is not authenticated
            finish();
            return;
        }

        // Initialize Firebase Database reference with the provided link
        databaseReference = FirebaseDatabase.getInstance("https://medicare-5ad77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Appointments")
                .child(uid);

        // Initialize UI components
        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        editTextReason = findViewById(R.id.editTextReason);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Initialize department and doctor data
        initDoctorMap();

        // Set up department spinner
        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(doctorMap.keySet()));
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(departmentAdapter);

        // Department spinner item selection listener
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDepartment = (String) parentView.getItemAtPosition(position);
                updateDoctorSpinner(selectedDepartment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Submit button click listener
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected values
                String selectedDepartment = spinnerDepartment.getSelectedItem().toString();
                String selectedDoctor = spinnerDoctor.getSelectedItem().toString();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String reason = editTextReason.getText().toString();

                // Save appointment data to Firebase
                Appointment appointment = new Appointment(selectedDepartment, selectedDoctor, year, month, day, hour, minute, reason);
                String appointmentId = databaseReference.push().getKey();
                if (appointmentId != null) {
                    databaseReference.child(appointmentId).setValue(appointment);
                    // Show appointment scheduled notification
                    showAppointmentNotification();
                    Toast.makeText(ScheduleActivity.this, "Appointment saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScheduleActivity.this, "Failed to save appointment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initDoctorMap() {
        doctorMap = new HashMap<>();

        // Cardiology Department
        List<String> cardiologyDoctors = new ArrayList<>();
        cardiologyDoctors.add("Dr. Smith");
        cardiologyDoctors.add("Dr. Johnson");
        cardiologyDoctors.add("Dr. Anderson");
        cardiologyDoctors.add("Dr. Martinez");
        cardiologyDoctors.add("Dr. White");
        doctorMap.put("Cardiology", cardiologyDoctors);

        // Dermatology Department
        List<String> dermatologyDoctors = new ArrayList<>();
        dermatologyDoctors.add("Dr. Brown");
        dermatologyDoctors.add("Dr. Davis");
        dermatologyDoctors.add("Dr. Wilson");
        dermatologyDoctors.add("Dr. Taylor");
        dermatologyDoctors.add("Dr. Harris");
        doctorMap.put("Dermatology", dermatologyDoctors);

        // Orthopedics Department
        List<String> orthopedicsDoctors = new ArrayList<>();
        orthopedicsDoctors.add("Dr. Johnson");
        orthopedicsDoctors.add("Dr. Allen");
        orthopedicsDoctors.add("Dr. Lewis");
        orthopedicsDoctors.add("Dr. Turner");
        orthopedicsDoctors.add("Dr. Parker");
        doctorMap.put("Orthopedics", orthopedicsDoctors);

        // Gynecology Department
        List<String> gynecologyDoctors = new ArrayList<>();
        gynecologyDoctors.add("Dr. Clark");
        gynecologyDoctors.add("Dr. King");
        gynecologyDoctors.add("Dr. Baker");
        gynecologyDoctors.add("Dr. Miller");
        gynecologyDoctors.add("Dr. Adams");
        doctorMap.put("Gynecology", gynecologyDoctors);

        // Neurology Department
        List<String> neurologyDoctors = new ArrayList<>();
        neurologyDoctors.add("Dr. Ward");
        neurologyDoctors.add("Dr. Scott");
        neurologyDoctors.add("Dr. Gonzalez");
        neurologyDoctors.add("Dr. Hall");
        neurologyDoctors.add("Dr. Allen");
        doctorMap.put("Neurology", neurologyDoctors);

        // Add more departments and doctors as needed
    }

    private void updateDoctorSpinner(String selectedDepartment) {
        List<String> doctors = doctorMap.get(selectedDepartment);
        if (doctors != null && !doctors.isEmpty()) {
            ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctors);
            doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDoctor.setAdapter(doctorAdapter);
            spinnerDoctor.setVisibility(View.VISIBLE);
        } else {
            spinnerDoctor.setVisibility(View.GONE);
        }
    }

    private void showAppointmentNotification() {
        // Create a notification channel if Android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "appointments";
            String channelName = "Appointments";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Appointment notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification builder
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, "appointments");
        } else {
            builder = new Notification.Builder(this);
        }

        // Set notification properties
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Appointment Scheduled")
                .setContentText("Your appointment has been scheduled successfully!")
                .setAutoCancel(true);

        // Show the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());
    }
}
