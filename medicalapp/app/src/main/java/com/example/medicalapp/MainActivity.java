package com.example.medicalapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final long NOTIFICATION_INTERVAL = 1 * 60 * 1000; // 1 minute
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user is authenticated
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already authenticated, proceed to main content
            startActivity(new Intent(MainActivity.this, DashboardActivity.class)); // Change to your main content activity
            finish();
        } else {
            // User is not authenticated, show login/signup screen
            AppCompatButton getStartedButton = findViewById(R.id.button);
            getStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, IntroActivity.class));
                    finish();
                }
            });
        }

        // Schedule periodic notifications
        scheduleNotification(this, "HYDRATE_ACTION", NOTIFICATION_INTERVAL);
        scheduleNotification(this, "DINNER_ACTION", NOTIFICATION_INTERVAL);
    }

    private void scheduleNotification(Context context, String action, long interval) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); // Add the FLAG_IMMUTABLE flag

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long triggerTime = System.currentTimeMillis() + interval;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, pendingIntent);
    }

}
