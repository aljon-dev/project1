package com.example.admsgroup7;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settings extends AppCompatActivity {


    private FirebaseAuth auth;
    private EditText Password,ConfirmPassword;
    private Button Changebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        auth = FirebaseAuth.getInstance();
        auth.getCurrentUser();

        //Edit Text Components
        Password = findViewById(R.id.newPassword);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        //Button
        Changebtn = findViewById(R.id.Changebtn);

        Changebtn.setOnClickListener(v ->{
            String userPassword = Password.getText().toString();
            String ConfirmPass = ConfirmPassword.getText().toString();

            if(userPassword.equals(ConfirmPass)){
                FirebaseUser firebaseUser = auth.getCurrentUser();
                firebaseUser.updatePassword(userPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(settings.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(settings.this, "Failed to changed password", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }else{
                Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            }
        });









    }
}