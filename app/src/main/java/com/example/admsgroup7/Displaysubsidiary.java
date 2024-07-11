package com.example.admsgroup7;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Displaysubsidiary extends AppCompatActivity {

    private RecyclerView subsidiaryView;
    private SubsidiesAdapter adapter;
    private ArrayList<getSubsidies> SubsidiesList;
    private FirebaseFirestore firebaseFirestore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subsidiarylist);

        firebaseFirestore = FirebaseFirestore.getInstance();

        subsidiaryView = findViewById(R.id.subsidiaryView);
        subsidiaryView.setLayoutManager(new LinearLayoutManager(this));


        SubsidiesList = new ArrayList<>();
        adapter = new SubsidiesAdapter(this,SubsidiesList);
        subsidiaryView.setAdapter(adapter);

        adapter.setOnItemClickListener(GetSubsidies -> {
            AlertDialog.Builder Action = new AlertDialog.Builder(Displaysubsidiary.this);
            CharSequence options [] = {"Delete", "Do Nothing"};
            Action.setTitle("Choose Action");
            Action.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        Confirmation(GetSubsidies);
                    }else if (which == 1){
                        dialog.dismiss();
                    }
                }
            });
            Action.show();
        });

        firebaseFirestore.collection("Subsidies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot QDS : task.getResult()){
                                getSubsidies GetSubsidies = QDS.toObject(getSubsidies.class);
                                GetSubsidies.setKey(QDS.getId());
                                SubsidiesList.add(GetSubsidies);
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(Displaysubsidiary.this, "failed to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    private void Confirmation(getSubsidies GetSubsidies){

        AlertDialog.Builder Action = new AlertDialog.Builder(Displaysubsidiary.this);
        CharSequence options [] = {"Yes Delete it ", "Do Nothing"};
        Action.setTitle("Are you sure");
        Action.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){

                    firebaseFirestore.collection("Subsidies").document(GetSubsidies.getKey())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Displaysubsidiary.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        recreate();
                                    }else{
                                        Toast.makeText(Displaysubsidiary.this, "Failed to delete ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else if (which == 1){
                    dialog.dismiss();
                }
            }
        });
        Action.show();

    }
}