package com.example.cooksmart_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminProfile extends AppCompatActivity {

    TextView profileName, profileEmail, profilePassword;
    Button editProfile;
    ImageView btnBack;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        editProfile = findViewById(R.id.editButton);

        btnBack = (ImageView) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize the Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        firebaseAuth = FirebaseAuth.getInstance();

        showAllUserData();

        Log.d("ADMIN PROFILE ", "EXECUTED");

        /* editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        }); */
    }

    public void showAllUserData(){
        // Intent intent = getIntent();
        // String userEmail = intent.getStringExtra("userEmail"); // Assuming you pass the user's email

        String userId = firebaseAuth.getCurrentUser().getUid();
        currentUser = firebaseAuth.getCurrentUser();
        Log.d("Uid ", userId);

        Log.d("EXECUTED ", "YES");
        // Log.d("userEmail: ", userEmail);

        // Check if userEmail is not null
        // if (userEmail != null) {
            Log.d("EXECUTED 2 ", "YES");
            // Retrieve user data from Firebase
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // String userId = snapshot.getChildren().iterator().next().getKey();

                        // User data found, update UI
                        String nameUser = snapshot.child("fullname").getValue(String.class);
                        // String emailUser = currentUser.getEmail();
                        String emailUser = snapshot.child("signupEmail").getValue(String.class);
                        String passwordUser = snapshot.child("signupPassword").getValue(String.class);
                        // String emailUser = snapshot.child("signupEmail").getValue(String.class);
                        // String passwordUser = snapshot.child("signupPassword").getValue(String.class);

                        profileName.setText(nameUser);
                        profileEmail.setText(emailUser);
                        profilePassword.setText(passwordUser);

                        Log.d("Name: ", nameUser);
                        Log.d("Email: ", emailUser);
                        Log.d("Password: ", passwordUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Toast.makeText(AdminProfile.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        // }
    }

    public void passUserData(){

    }
}