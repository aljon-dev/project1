package com.example.admsgroup7;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Announcement extends AppCompatActivity {

    private RecyclerView AnnouncementList;
    private AnnouncementAdapter adapter;
    private ArrayList<AnnouncementInfo> AnnouncementItems;

    private FloatingActionButton AddAnnouncement;

    private FirebaseFirestore firebaseFirestore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        //Setting up firestore
        firebaseFirestore = FirebaseFirestore.getInstance();


        AddAnnouncement = findViewById(R.id.addAnnouncement);

        //Recyclerview
        AnnouncementList = findViewById(R.id.AnnouncementList);
        AnnouncementItems = new ArrayList<>();
        AnnouncementList.setLayoutManager(new LinearLayoutManager(this));
        adapter =new AnnouncementAdapter(this,AnnouncementItems);
        AnnouncementList.setAdapter(adapter);



        firebaseFirestore.collection("Announcement").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                AnnouncementItems.clear();
                for(QueryDocumentSnapshot QDS : task.getResult()){
                    AnnouncementInfo announcementInfo = QDS.toObject(AnnouncementInfo.class);
                    AnnouncementItems.add(announcementInfo);
                }
                adapter.notifyDataSetChanged();
            }
        });






        AddAnnouncement.setOnClickListener(v ->{
            AnnouncementDialog();

        });

    }

    private void AnnouncementDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_dialog_announcement,null,false);
        alertDialog.setView(view);
        alertDialog.setTitle("Add Announcements");

        //EditText
        TextInputEditText titleSet,AnnouncementDescription;

        titleSet = view.findViewById(R.id.titleSet);
        AnnouncementDescription = view.findViewById(R.id.AnnouncementDescription);

        alertDialog.setPositiveButton("Add", (dialog, which) -> {
            String title = titleSet.getText().toString();
            String Description = AnnouncementDescription.getText().toString();
            AddAnnouncement(title,Description);

        }).setNegativeButton("Cancel", (dialog, which) -> {

        });
        alertDialog.show();

    }
    private void AddAnnouncement(String title,String description){

        String RandomID = UUID.randomUUID().toString();

            Map<String,Object> AnnouncementInfo = new HashMap<>();
            AnnouncementInfo.put("Title",title);
            AnnouncementInfo.put("Description",description);

        firebaseFirestore.collection("Announcement").document(RandomID).set(AnnouncementInfo);
        recreate();
    }
}