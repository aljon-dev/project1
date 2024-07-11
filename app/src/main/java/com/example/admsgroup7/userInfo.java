package com.example.admsgroup7;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class userInfo extends AppCompatActivity {

   private TextView FirstName,MidName,LastName,Nationality,Sex,BirthDay,Contact,Address;
   private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        String UserId = getIntent().getStringExtra("UserId");

        firebaseFirestore = FirebaseFirestore.getInstance();

        FirstName = findViewById(R.id.FirstName);
        MidName = findViewById(R.id.MidName);
        LastName = findViewById(R.id.LastName);
        Nationality = findViewById(R.id.Nationality);
        Sex = findViewById(R.id.Sex);
        BirthDay = findViewById(R.id.BirthDay);
        Contact = findViewById(R.id.Contact);
        Address = findViewById(R.id.Address);

        firebaseFirestore.collection("Users").document(UserId).collection("Info")
                .document("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();

                            if(document.exists()){
                                String fname = document.getString("FirstName");
                                String mname = document.getString("MiddleName");
                                String lname = document.getString("LastName");
                                String dateStr = document.getString("Date");
                                String sex = document.getString("Sex");
                                String nationality = document.getString("Nationality");
                                String addressStr = document.getString("Address");
                                String contactStr = document.getString("Contact");

                                FirstName.setText(fname);
                                MidName.setText(mname);
                                LastName.setText(lname);
                                Sex.setText(sex);
                                Nationality.setText(nationality);
                                BirthDay.setText(dateStr);
                                Address.setText(addressStr);
                                Contact.setText(contactStr);
                            }
                        }
                    }
                });


    }
}