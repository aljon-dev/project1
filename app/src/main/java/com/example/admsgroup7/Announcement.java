package com.example.admsgroup7;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.time.LocalDate;
import java.time.LocalTime;
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

        adapter.setOnItemClickListener((announcementInfo -> {
            AlertDialog.Builder ActionDialog = new AlertDialog.Builder(this);
            ActionDialog.setTitle("Choose Action");
            CharSequence options [] = {"Delete", "Do Nothing"};
            ActionDialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        Confirmation(announcementInfo);
                    }else if (which == 1 ){
                        dialog.dismiss();
                    }
                }
            });
            ActionDialog.show();



        })
        );



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
        LocalDate Date = LocalDate.now();
        String DateToday = String.valueOf(Date.getMonth());
        String DayToday = String.valueOf(Date.getDayOfMonth());
        String DateYear = String.valueOf(Date.getYear());

        String LocalDateToday = ( (DateToday)+ "-" + (DayToday)+ "-"+ (DateYear));

        LocalTime time = LocalTime.now();
        String TimeToday = String.valueOf(time.getHour());
        String minute = String.valueOf(time.getMinute());


        String PostedTime = (TimeToday) +":"+ (minute);

            Map<String,Object> AnnouncementInfo = new HashMap<>();
            AnnouncementInfo.put("Title",title);
            AnnouncementInfo.put("Description",description);
            AnnouncementInfo.put("DateToday",LocalDateToday);
            AnnouncementInfo.put("Time",PostedTime);

        firebaseFirestore.collection("Announcement").document(RandomID).set(AnnouncementInfo);
        recreate();
    }

    private void Confirmation(AnnouncementInfo announcementInfo){



        AlertDialog.Builder ActionDialog = new AlertDialog.Builder(this);
        ActionDialog.setTitle("Are You Sure");
        CharSequence options [] = {"Yes Delete it ", "Cancel"};
        ActionDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    firebaseFirestore.collection("Announcement")
                            .document(announcementInfo.getAnnouncementKey())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Announcement.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    recreate();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Announcement.this, "Failed To Delete", Toast.LENGTH_SHORT).show();
                                }
                            });

                }else if (which == 1 ){
                    dialog.dismiss();
                }
            }
        });
        ActionDialog.show();




    }
}