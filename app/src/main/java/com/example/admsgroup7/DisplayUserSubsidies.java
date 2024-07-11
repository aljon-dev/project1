package com.example.admsgroup7;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DisplayUserSubsidies extends AppCompatActivity {


    private RecyclerView subsidiaryView;
    private SubsidiesAdapter adapter;
    private ArrayList<getSubsidies> SubsidiesList;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_subsidies);

        String UserId = getIntent().getStringExtra("UserId");


        firebaseFirestore = FirebaseFirestore.getInstance();

        subsidiaryView = findViewById(R.id.subsidiaryView);
        subsidiaryView.setLayoutManager(new LinearLayoutManager(this));


        SubsidiesList = new ArrayList<>();
        adapter = new SubsidiesAdapter(this,SubsidiesList);
        subsidiaryView.setAdapter(adapter);


    }






}