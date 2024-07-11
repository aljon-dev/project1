package com.example.admsgroup7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class BeneficiaryActivity extends AppCompatActivity {
    private   FloatingActionButton fab;

    private  RecyclerView recyclerView;

    private ArrayList<Users> UserItems;

    private UserAdapter adapter;

    private FirebaseFirestore firebaseFirestore;

    private SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary);

        //Setting up database
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Components Id
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);



        recyclerView = findViewById(R.id.recyclerView);

        //setting up recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserItems = new ArrayList<>();
        adapter = new UserAdapter(this,UserItems);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(users -> {


            AlertDialog.Builder Options = new AlertDialog.Builder(this);
            Options.setTitle("Select Action");
            CharSequence OptionSelect []= {"View Info","Add Subsidy"};
            Options.setItems(OptionSelect, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                            if(which == 0){
                                Intent intent = new Intent(BeneficiaryActivity.this,userInfo.class);
                                intent.putExtra("UserId",users.getKey());
                                startActivity(intent);

                            }else if (which == 1){
                                String userkey = users.getKey();
                                String fullname = users.getFullName();
                                AddSubsidy(userkey,fullname);
                            }
                }
            });
            Options.show();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DisplayUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                DisplayUsers(newText);
                return false;
            }
        });

        DisplayUsers("");





        //Floating action button
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(BeneficiaryActivity.this, RegisterUserActivity.class);
            startActivity(intent);
        });

    }
    private void DisplayUsers(String query){
        firebaseFirestore.collection("Users")
                .whereGreaterThanOrEqualTo("FullName",query)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            UserItems.clear();
                            for(QueryDocumentSnapshot QDS  : task.getResult()){
                                Users user = QDS.toObject(Users.class);
                                user.setKey(QDS.getId());
                                UserItems.add(user);
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(BeneficiaryActivity.this, "Failed to fetch Documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void AddSubsidy(String UserKey,String FullName){
        AlertDialog.Builder AddSubsidies = new AlertDialog.Builder(BeneficiaryActivity.this);
        View view = LayoutInflater.from(BeneficiaryActivity.this).inflate(R.layout.subsidy_form,null,false);
        AddSubsidies.setView(view);
        TextInputEditText  ReceivedDate,InstanceNumber,SponsoredBy,Received;

        ReceivedDate = view.findViewById(R.id.ReceivedDate);
        InstanceNumber = view.findViewById(R.id.InstanceNumber);
        SponsoredBy = view.findViewById(R.id.SponsoredBy);
        Received = view.findViewById(R.id.Received);

       AddSubsidies.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

               String RDate = ReceivedDate.getText().toString();
               String InsNumber = InstanceNumber.getText().toString();
               String SprBy = SponsoredBy.getText().toString();
               String Rcv = Received.getText().toString();
               String key = UserKey;
               String GenerateId = UUID.randomUUID().toString();

               Map<String,Object> Subsidies = new HashMap<>();
               Subsidies.put("Received_Date",RDate);
               Subsidies.put("InstanceNumber",InsNumber);
               Subsidies.put("SponsoredBy",SprBy);
               Subsidies.put("Received",Rcv);
               Subsidies.put("UserKey",key);
               Subsidies.put("FullName",FullName);



               firebaseFirestore.collection("Subsidies").document(GenerateId).set(Subsidies).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(BeneficiaryActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(BeneficiaryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                       }
                   }
               });



           }
       }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
           }
       });
       AddSubsidies.show();




    }


}

