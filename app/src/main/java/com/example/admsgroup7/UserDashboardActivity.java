package com.example.admsgroup7;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDashboardActivity extends AppCompatActivity {

    private FirebaseAuth Auth;

    private CardView settings,SubsidyStatus,FillInfo;
    private int Notification_Permission = 2;

    private int InitialSize = -1;

    private FirebaseFirestore firebaseFirestore;
    private Set<String> previousAnnouncementIds = new HashSet<>();

    private ArrayList<AnnouncementInfo> AnnouncementItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Auth = FirebaseAuth.getInstance();
        Auth.getCurrentUser();
        FirebaseUser AuthUser = Auth.getCurrentUser();

        Button logout = findViewById(R.id.LogoutBtn);

        settings = findViewById(R.id.settings);
        SubsidyStatus = findViewById(R.id.SubsidyStatus);
        FillInfo = findViewById(R.id.FillInfo);
        createNotificationChannel(this);

        AnnouncementItems = new ArrayList<>();

        logout.setOnClickListener(view -> {
            Auth.signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        });


        settings.setOnClickListener(v ->{
            Intent intent = new Intent(UserDashboardActivity.this,settings.class);
            startActivity(intent);

        });

        FillInfo.setOnClickListener(v ->{
            Intent intent = new Intent(UserDashboardActivity.this, user_info_forms.class);
            intent.putExtra("UserId",AuthUser.getUid());
            startActivity(intent);
        });
        SubsidyStatus.setOnClickListener(v ->{
            Intent intent = new Intent(UserDashboardActivity.this, DisplayUserSubsidies.class);
            intent.putExtra("UserId",AuthUser.getUid());
            startActivity(intent);
        });

        permissions();

        firebaseFirestore.collection("Announcement").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                AnnouncementItems.clear();
                Set<String> currentAnnouncementIds = new HashSet<>();
                List<AnnouncementInfo> newAnnouncements = new ArrayList<>();
                for(QueryDocumentSnapshot QDS : task.getResult()){
                    AnnouncementInfo announcementInfo = QDS.toObject(AnnouncementInfo.class);

                    if (announcementInfo != null) {
                        AnnouncementItems.add(announcementInfo);
                        String id = QDS.getId();  // Get the unique ID for each announcement
                        currentAnnouncementIds.add(id);

                        if (!previousAnnouncementIds.contains(id)) {
                            newAnnouncements.add(announcementInfo);
                        }
                    }
                }
                if (InitialSize == -1) {

                    InitialSize =    AnnouncementItems.size();

                } else {
                    if (!newAnnouncements.isEmpty()) {
                        new Handler().postDelayed(() -> showNotification(UserDashboardActivity.this), 1000);
                    }
                }
                previousAnnouncementIds.clear();
                previousAnnouncementIds.addAll(currentAnnouncementIds);

            }
        });



    }
    private void permissions (){
        //Check Permission for Notification
        if(CheckPostNotification()){
            Toast.makeText(this, " Notification Access ", Toast.LENGTH_SHORT).show();
        }else{
            NotificationPermission();
        }
    }


    private Boolean CheckPostNotification(){
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private void NotificationPermission(){
        ActivityCompat.requestPermissions(this,new String []{Manifest.permission.POST_NOTIFICATIONS},Notification_Permission);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Notification_Permission) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showNotification(Context context){
        Intent intent = new Intent(context,UserDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_IMMUTABLE);

        String channelId = "AnnouncementNotification";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Announcement")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    private void createNotificationChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "AnnouncementNotification";
            CharSequence channelName = "Notification Announcement";
            String Channel_Description = "Notification for Announcement";
            int Importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel chanel = new NotificationChannel(channelId,channelName,Importance);
            chanel.setDescription(Channel_Description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(chanel);

        }
    }


}