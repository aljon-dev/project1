package com.example.admsgroup7;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DisplayUserAnnouncement extends AppCompatActivity {


    private RecyclerView AnnouncementList;
    private AnnouncementAdapter adapter;
    private ArrayList<AnnouncementInfo> AnnouncementItems;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_announcement);

        //Setting up firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

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
                    announcementInfo.setAnnouncementKey(QDS.getId());
                    AnnouncementItems.add(announcementInfo);
                }
                adapter.notifyDataSetChanged();
            }
        });


    }
}