package com.example.medicalapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button viewScheduledTestsButton;
    private TextView totalAmountTextView;
    private Button confirmButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private List<TestItem> selectedTests;
    private TestAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);

        recyclerView = findViewById(R.id.recyclerView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        confirmButton = findViewById(R.id.confirmButton);
        viewScheduledTestsButton = findViewById(R.id.viewScheduledTestsButton);

        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        if (uid == null) {
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicare-5ad77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Tests")
                .child(uid);

        selectedTests = new ArrayList<>();
        testAdapter = new TestAdapter(selectedTests);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testAdapter);

        loadTestItems();
        calculateTotalAmount();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTests.isEmpty()) {
                    Toast.makeText(TestActivity.this, "No tests selected", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmationDialog();
                }
            }
        });

        viewScheduledTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, ScheduledTestActivity.class));
            }
        });
    }

    private void loadTestItems() {
        // Load test items from your data source
        List<TestItem> testItems = new ArrayList<>();
        testItems.add(new TestItem("Test 1", "Description 1", 50));
        testItems.add(new TestItem("Test 2", "Description 2", 60));
        testItems.add(new TestItem("Test 3", "Description 3", 70));
        testItems.add(new TestItem("Test 4", "Description 4", 80));
        testItems.add(new TestItem("Test 5", "Description 5", 90));

        testAdapter = new TestAdapter(testItems);
        recyclerView.setAdapter(testAdapter);

        testAdapter.setOnTakeItClickListener(new TestAdapter.OnTakeItClickListener() {
            @Override
            public void onTakeItClick(int position) {
                TestItem selectedTest = testItems.get(position);
                selectedTests.add(selectedTest);
                testAdapter.notifyDataSetChanged();
                calculateTotalAmount();
            }
        });
    }

    private void calculateTotalAmount() {
        int totalAmount = 0;
        for (TestItem testItem : selectedTests) {
            totalAmount += testItem.getPrice();
        }
        totalAmountTextView.setText("Total Amount: $" + totalAmount);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Purchase");
        builder.setMessage("Do you want to confirm the purchase?");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_purchase, null);
        builder.setView(dialogView);

        final EditText dateEditText = dialogView.findViewById(R.id.dateEditText);
        final EditText timeEditText = dialogView.findViewById(R.id.timeEditText);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                if (date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(TestActivity.this, "Please enter date and time", Toast.LENGTH_SHORT).show();
                } else {
                    saveConfirmedTests(date, time);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void saveConfirmedTests(String date, String time) {
        String uid = firebaseAuth.getUid();
        if (uid != null) {
            DatabaseReference userReference = databaseReference.child(uid);
            for (TestItem testItem : selectedTests) {
                String key = userReference.child("confirmedTests").push().getKey();
                if (key != null) {
                    userReference.child("confirmedTests").child(key).setValue(testItem);
                }
            }
            userReference.child("date").setValue(date);
            userReference.child("time").setValue(time);
            Toast.makeText(this, "Tests confirmed and saved", Toast.LENGTH_SHORT).show();

            sendNotification("Tests Confirmed", "Your selected tests have been confirmed.");
        }
    }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
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
