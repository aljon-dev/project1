package com.example.admsgroup7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {
    private CardView beneficiaryCard,settings,SubsidyStatus,announcement;
    private Button logoutButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

       auth = FirebaseAuth.getInstance();
       auth.getCurrentUser();

        beneficiaryCard = findViewById(R.id.beneficiaryCard);
        logoutButton = findViewById(R.id.LogoutBttn);
        settings = findViewById(R.id.settings);
        SubsidyStatus = findViewById(R.id.SubsidyStatus);
        announcement = findViewById(R.id.announcement);


        // Set up the beneficiary card click listener
        beneficiaryCard.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, BeneficiaryActivity.class);
            startActivity(intent);
        });
        //Set up card on listener
        settings.setOnClickListener(v ->{
            Intent intent = new Intent(AdminDashboardActivity.this,settings.class);
            startActivity(intent);
        });

        announcement.setOnClickListener(v ->{
            Intent intent = new Intent(AdminDashboardActivity.this,Announcement.class);
            startActivity(intent);
        });

        SubsidyStatus.setOnClickListener(v ->{
            Intent intent = new Intent(AdminDashboardActivity.this, Displaysubsidiary.class);
            startActivity(intent);

        });



        // Set up the logout button click listener
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });



    }
}
