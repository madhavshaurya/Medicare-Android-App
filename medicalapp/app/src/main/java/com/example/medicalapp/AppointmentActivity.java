package com.example.medicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointmentActivity extends AppCompatActivity {

    private LinearLayout appointmentsLayout;
    private Button scheduleButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity full-screen (no status bar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_appointment);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Check if the user is authenticated, else redirect to login
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize Firebase Database reference with the provided link
        databaseReference = FirebaseDatabase.getInstance("https://medicare-5ad77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Appointments")
                .child(user.getUid());

        appointmentsLayout = findViewById(R.id.appointmentsLayout);
        scheduleButton = findViewById(R.id.scheduleButton);

        // Fetch and display user's appointments
        fetchAppointments();

        // Schedule button click listener
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppointmentActivity.this, ScheduleActivity.class));
            }
        });
    }

    private void fetchAppointments() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentsLayout.removeAllViews();

                if (snapshot.exists()) {
                    for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                        Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                        if (appointment != null) {
                            addAppointmentView(appointment);
                        }
                    }
                } else {
                    TextView noAppointmentsText = new TextView(AppointmentActivity.this);
                    noAppointmentsText.setText("No Appointments made");
                    appointmentsLayout.addView(noAppointmentsText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void addAppointmentView(Appointment appointment) {
        View appointmentView = getLayoutInflater().inflate(R.layout.item_appointment, appointmentsLayout, false);
        TextView departmentText = appointmentView.findViewById(R.id.departmentText);
        TextView doctorText = appointmentView.findViewById(R.id.doctorText);
        TextView dateText = appointmentView.findViewById(R.id.dateText);
        TextView timeText = appointmentView.findViewById(R.id.timeText);

        departmentText.setText("Department: " + appointment.getDepartment());
        doctorText.setText("Doctor: " + appointment.getDoctor());
        dateText.setText("Date: " + appointment.getFormattedDate());
        timeText.setText("Time: " + appointment.getFormattedTime());

        appointmentsLayout.addView(appointmentView);
    }

}
