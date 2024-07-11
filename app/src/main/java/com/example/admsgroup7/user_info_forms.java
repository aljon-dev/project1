package com.example.admsgroup7;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class user_info_forms extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;

    EditText FirstName, MidName, LastName, BDate, Sex, Nationality, Address, Contact;

    Button FillButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_forms);

        firebaseFirestore = FirebaseFirestore.getInstance();

        String UserId = getIntent().getStringExtra("UserId");

        FirstName = findViewById(R.id.FirstName);
        MidName = findViewById(R.id.MidName);
        LastName = findViewById(R.id.LastName);
        BDate = findViewById(R.id.BDate);
        Sex = findViewById(R.id.Sex);
        Nationality = findViewById(R.id.Nationality);
        Address = findViewById(R.id.Address);
        Contact = findViewById(R.id.Contact);

        //CalendarPicker
        BDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) -> BDate.setText(dayOfMonth + "-" + (month1 + 1) + "-" + year1), year, month, day);
            datePickerDialog.show();
        });

        Sex.setOnClickListener(v -> {
            AlertDialog.Builder sexDialog = new AlertDialog.Builder(this);
            sexDialog.setTitle("Set Sex");
            CharSequence[] sexOptions = {"Male", "Female"};

            sexDialog.setItems(sexOptions, (dialog, which) -> {
                if (which == 0) {
                    Sex.setText("Male");
                } else if (which == 1) {
                    Sex.setText("Female");
                }
            });

            sexDialog.show();
        });

        firebaseFirestore.collection("Users").document(UserId).collection("Info").document("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Get the values from the document
                                String fname = document.getString("FirstName");
                                String mname = document.getString("MiddleName");
                                String lname = document.getString("LastName");
                                String dateStr = document.getString("Date");
                                String sex = document.getString("Sex");
                                String nationality = document.getString("Nationality");
                                String addressStr = document.getString("Address");
                                String contactStr = document.getString("Contact");

                                // Set the values to the EditText fields
                                FirstName.setText(fname);
                                MidName.setText(mname);
                                LastName.setText(lname);
                                Sex.setText(sex);
                                Nationality.setText(nationality);
                                BDate.setText(dateStr);
                                Address.setText(addressStr);
                                Contact.setText(contactStr);
                            } else {
                                Toast.makeText(user_info_forms.this, "No such document exists", Toast.LENGTH_SHORT).show();
                            }


                        }else{
                            Toast.makeText(user_info_forms.this, "Failed to fetch the data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





        FillButton = findViewById(R.id.FillButton);

        FillButton.setOnClickListener(v -> {

            String Fname = FirstName.getText().toString();
            String Mname = MidName.getText().toString();
            String Lname = LastName.getText().toString();
            String Date = BDate.getText().toString();
            String sex = Sex.getText().toString();
            String UrAddress = Address.getText().toString();
            String UrContact = Contact.getText().toString();
            String UrNationality =   Nationality.getText().toString();

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("FirstName", Fname);
            userInfo.put("MiddleName", Mname);
            userInfo.put("LastName", Lname);
            userInfo.put("Sex",sex);
            userInfo.put("Nationality",UrNationality);
            userInfo.put("Date", Date);
            userInfo.put("Address", UrAddress);
            userInfo.put("Contact", UrContact);

            // Use set with SetOptions.merge() to ensure the document is created or updated
            firebaseFirestore.collection("Users").document(UserId).collection("Info").document("UserInfo")
                    .set(userInfo, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        // Handle success
                        Toast.makeText(this, "DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show();
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.w("Firestore", "Error writing document", e);
                    });
        });


    }
}