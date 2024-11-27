package com.example.cooksmart_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cooksmart-testv2-default-rtdb.firebaseio.com/");

    LinearLayout mealPlanner, manageProfile, viewRecipe, btnLogOut;
    TextView txtUser;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        firebaseAuth = FirebaseAuth.getInstance();
        btnLogOut = (LinearLayout) findViewById(R.id.btnLogOut);
        manageProfile = (LinearLayout) findViewById(R.id.manageProfile);
        mealPlanner = (LinearLayout) findViewById(R.id.mealPlanner);
        viewRecipe = (LinearLayout) findViewById(R.id.viewRecipe);
        txtUser = (TextView) findViewById(R.id.txtUser);
        user = firebaseAuth.getCurrentUser();

        if (user == null ) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Log.d("User Page", "EXECUTED");
            txtUser.setText(user.getEmail());
        }

        manageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent I = new Intent(UserActivity.this, LoginActivity.class);
                //startActivity(I);
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                finish();

            }
        });

        viewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent I = new Intent(UserActivity.this, LoginActivity.class);
                //startActivity(I);
                Intent intent = new Intent(getApplicationContext(), UserRecipeSection.class);
                startActivity(intent);
                finish();

            }
        });

        mealPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent I = new Intent(UserActivity.this, LoginActivity.class);
                //startActivity(I);
                Intent intent = new Intent(getApplicationContext(), WeekCalendar.class);
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