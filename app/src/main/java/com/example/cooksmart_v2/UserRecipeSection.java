package com.example.cooksmart_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class UserRecipeSection extends AppCompatActivity {

    ImageView btnBack;
    LinearLayout viewAll, viewRecommended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_recipe_section);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        viewAll = (LinearLayout) findViewById(R.id.viewAll);
        viewRecommended = (LinearLayout) findViewById(R.id.viewRecommended);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserListRecipe.class);
                startActivity(intent);
                finish();
            }
        });

        viewRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecommendedRecipe.class);
                startActivity(intent);
                finish();
            }
        });
    }
}