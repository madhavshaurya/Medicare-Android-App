package com.example.medicalapp;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScheduledTestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScheduledTestAdapter testAdapter;
    private List<TestItem> scheduledTests;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make the activity full-screen (no status bar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scheduled_test);

        recyclerView = findViewById(R.id.scheduledRecyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        if (uid == null) {
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicare-5ad77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Tests")
                .child(uid);

        scheduledTests = new ArrayList<>();
        testAdapter = new ScheduledTestAdapter(scheduledTests);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testAdapter);

        loadScheduledTests();
    }

    private void loadScheduledTests() {
        String uid = firebaseAuth.getUid();
        DatabaseReference userReference = databaseReference.child(uid).child("confirmedTests");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduledTests.clear();
                for (DataSnapshot testSnapshot : snapshot.getChildren()) {
                    TestItem testItem = testSnapshot.getValue(TestItem.class);
                    if (testItem != null) {
                        scheduledTests.add(testItem);
                    }
                }
                testAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
