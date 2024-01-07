package com.example.cooksmart_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class ManageRecipe extends AppCompatActivity {

    ImageView btnBack;
    LinearLayout addRecipe, viewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_recipe);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        addRecipe = (LinearLayout) findViewById(R.id.addRecipe);
        viewRecipe = (LinearLayout) findViewById(R.id.viewRecipe);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddRecipe.class);
                startActivity(intent);
                finish();
            }
        });

        viewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListRecipe.class);
                startActivity(intent);
                finish();
            }
        });
    }
}