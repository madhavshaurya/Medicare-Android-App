package com.example.medicalapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeVisitActivity extends AppCompatActivity {

    private Spinner spinnerDepartment;
    private Spinner spinnerDoctor;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText editTextReason;
    private Button buttonRequest;

    private HashMap<String, List<String>> doctorMap;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_visit);

        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        if (uid == null) {
            finish();
            return;
        }

        notificationManager = NotificationManagerCompat.from(this);

        databaseReference = FirebaseDatabase.getInstance("https://medicare-5ad77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("HomeVisitRequests")
                .child(uid);

        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        editTextReason = findViewById(R.id.editTextReason);
        buttonRequest = findViewById(R.id.buttonRequest);

        initDoctorMap();

        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(doctorMap.keySet()));
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(departmentAdapter);

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

        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDepartment = spinnerDepartment.getSelectedItem().toString();
                String selectedDoctor = spinnerDoctor.getSelectedItem().toString();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String reason = editTextReason.getText().toString();

                HomeVisitRequest visitRequest = new HomeVisitRequest(selectedDepartment, selectedDoctor, year, month, day, hour, minute, reason);
                String requestId = databaseReference.push().getKey();
                if (requestId != null) {
                    databaseReference.child(requestId).setValue(visitRequest);
                    Toast.makeText(HomeVisitActivity.this, "Home visit request sent", Toast.LENGTH_SHORT).show();
                    sendNotification("Home Visit Request Sent", "Your home visit request has been submitted.");
                } else {
                    Toast.makeText(HomeVisitActivity.this, "Failed to send home visit request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initDoctorMap() {
        doctorMap = new HashMap<>();
        // Add departments and doctors
        // ...
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

    private void sendNotification(String title, String message) {
        String channelId = "home_visit_channel";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create a notification channel for Android Oreo and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence channelName = "Home Visit Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
