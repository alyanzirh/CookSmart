package com.example.cooksmart_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cooksmart-testv2-default-rtdb.firebaseio.com/");

    LinearLayout manageMenu, btnLogOut, manageProfile;
    TextView txtUser, txtMessage;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        firebaseAuth = FirebaseAuth.getInstance();
        manageMenu = (LinearLayout) findViewById(R.id.manageMenu);
        manageProfile = (LinearLayout) findViewById(R.id.manageProfile);
        btnLogOut = (LinearLayout) findViewById(R.id.btnLogOut);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtUser = (TextView) findViewById(R.id.txtUser);
        user = firebaseAuth.getCurrentUser();

        if (user == null ) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Log.d("Admin Page", "EXECUTED");
            // txtMessage.setText("HELLO ADMIN");
            txtUser.setText(user.getEmail());
        }

        manageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent I = new Intent(UserActivity.this, LoginActivity.class);
                //startActivity(I);
                Intent intent = new Intent(getApplicationContext(), ManageRecipe.class);
                startActivity(intent);
                finish();

            }
        });

        manageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent I = new Intent(UserActivity.this, LoginActivity.class);
                //startActivity(I);
                Intent intent = new Intent(getApplicationContext(), AdminProfile.class);
                startActivity(intent);
                finish();

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                //Intent I = new Intent(UserActivity.this, LoginActivity.class);
                //startActivity(I);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}