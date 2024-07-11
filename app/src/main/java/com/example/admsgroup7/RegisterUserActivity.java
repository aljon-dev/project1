package com.example.admsgroup7;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUserActivity extends AppCompatActivity {
    EditText fullName, email, password, phone;
    Button registerButton;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isUserBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerFullName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerButton = findViewById(R.id.registerButton);
        isUserBox = findViewById(R.id.isUserBox);

        registerButton.setOnClickListener(view -> {
            // Reset validation flag
            valid = true;

            // Check all fields
            checkField(fullName);
            checkField(email);
            checkField(password);
            checkField(phone);

            // Additional validation for email and password
            if (!isValidEmail(email.getText().toString())) {
                email.setError("Invalid email address");
                valid = false;
            }
            if (!isValidPassword(password.getText().toString())) {
                password.setError("Password must be at least 8 characters long, contain a number, a special character, and no spaces");
                valid = false;
            }

            // Check if the user type is selected
            if (!isUserBox.isChecked()) {
                Toast.makeText(RegisterUserActivity.this, "Please select user type", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            // Proceed if all fields are valid
            if (valid) {
                // Start the user registration process
                fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(authResult -> {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(RegisterUserActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();

                            // Save user info to Firestore
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName", fullName.getText().toString());
                            userInfo.put("UserEmail", email.getText().toString());
                            userInfo.put("PhoneNumber", phone.getText().toString());
                            userInfo.put("isUser", "1");

                            df.set(userInfo)
                                    .addOnSuccessListener(aVoid -> {
                                        // Firestore document successfully written
                                        Toast.makeText(RegisterUserActivity.this, "User Profile Created", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error writing document
                                        Toast.makeText(RegisterUserActivity.this, "Failed to create user profile", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> Toast.makeText(RegisterUserActivity.this, "Failed to Create Account: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Field cannot be empty");
            valid = false;
        }
        return valid;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPassword(String password) {
        // At least 8 characters long, contain a number, a special character, and no spaces
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
