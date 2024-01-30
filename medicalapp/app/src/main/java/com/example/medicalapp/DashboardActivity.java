package com.example.medicalapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Make the activity full-screen (no status bar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        NavigationView navigationView = findViewById(R.id.nav_view); // Retrieve the NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        usernameTextView = findViewById(R.id.username);



        // Initialize the ImageSlider object
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        List<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.CENTER_INSIDE));
        imageSlider.setImageList(imageList);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getDisplayName();
            String email = currentUser.getEmail(); // Get the user's email from Firebase Authentication
            if (username != null && !username.isEmpty()) {
                usernameTextView.setText("   Hi, " + username + "!");
            }

            // Update the navigation header with user's name and email
            View headerView = navigationView.getHeaderView(0);
            TextView navHeaderUsername = headerView.findViewById(R.id.nav_header_username);
            TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);

            navHeaderUsername.setText(username);
            navHeaderEmail.setText(email);
        }

        /// Find your buttons and set click listeners
        findViewById(R.id.schedule_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ScheduleActivity.class));
            }
        });

        findViewById(R.id.Home_visit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, HomeVisitActivity.class));
            }
        });

        findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, TestActivity.class));
            }
        });
        findViewById(R.id.prescription_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AppointmentActivity.class));
            }
        });
        findViewById(R.id.dashboard_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
            }
        });
        findViewById(R.id.chat_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ChatActivity.class));
            }
        });
        findViewById(R.id.history_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
            }
        });
        findViewById(R.id.profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
            }
        });
        Button overflowButton = findViewById(R.id.overflow_button);
        overflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DashboardActivity", "Overflow button clicked");
                // Open the navigation drawer
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // Set click listeners for other buttons similarly
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            // Handle Settings click
            Log.d("DashboardActivity", "Settings clicked");
            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
            return true;
        } else if (id == R.id.menu_about_us) {
            // Handle About Us click
            startActivity(new Intent(DashboardActivity.this, AboutUsActivity.class));
            return true;
        } else if (id == R.id.menu_report) {
            // Handle Report click
            return true;
        } else if (id == R.id.menu_help) {
            // Handle Help click by sending an email to support
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:support@medicare.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help Request");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
