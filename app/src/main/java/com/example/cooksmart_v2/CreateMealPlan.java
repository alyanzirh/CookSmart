package com.example.cooksmart_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/* public class CreateMealPlan extends AppCompatActivity {

    private int totalCalories;
    private List<DataClass> recipes;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    // TextView totalCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meal_plan);

        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        this.recipes = new ArrayList<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();

        Log.d("userId: ", userId);

        databaseReference.child("users").child(userId).child("targetCals")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String totalCaloriesString = snapshot.getValue(String.class);
                        if (totalCaloriesString != null) {
                            try {
                                totalCalories = Integer.parseInt(totalCaloriesString);
                                TextView totalCal = findViewById(R.id.totalCalories);
                                totalCal.setText("Total Calories: " + totalCalories);
                            } catch (NumberFormatException e) {
                                Log.e("NumberFormatException", "Could not parse " + totalCaloriesString + " as an integer.");
                            }
                        } else {
                            // Handle the case where totalCaloriesString is null
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Button addBreakfastButton = findViewById(R.id.addBreakfastButton);
        Button addLunchButton = findViewById(R.id.addLunchButton);
        Button addDinnerButton = findViewById(R.id.addDinnerButton);

        /* addBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateMealPlan.this, ListRecipe.class);
                startActivity(intent);
            }
        }); */

        /* @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK) {
                ArrayList<DataClass> selectedRecipes = (ArrayList<DataClass>) data.getSerializableExtra("selectedRecipes");
                // Add selectedRecipes to breakfast
                // Update UI
            }
        }
    }

} */

public class CreateMealPlan extends AppCompatActivity {

    private int totalCalories;
    private List<DataClass> breakfastRecipes;
    private List<DataClass> lunchRecipes;
    private List<DataClass> dinnerRecipes;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextView breakfastCaloriesTextView;
    private TextView lunchCaloriesTextView;
    private TextView dinnerCaloriesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meal_plan);

        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        this.breakfastRecipes = new ArrayList<>();
        this.lunchRecipes = new ArrayList<>();
        this.dinnerRecipes = new ArrayList<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();

        Log.d("userId: ", userId);

        databaseReference.child("users").child(userId).child("targetCals")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String totalCaloriesString = snapshot.getValue(String.class);
                        if (totalCaloriesString != null) {
                            try {
                                totalCalories = Integer.parseInt(totalCaloriesString);
                                TextView totalCal = findViewById(R.id.totalCalories);
                                totalCal.setText("Total Calories: " + totalCalories);
                            } catch (NumberFormatException e) {
                                Log.e("NumberFormatException", "Could not parse " + totalCaloriesString + " as an integer.");
                            }
                        } else {
                            // Handle the case where totalCaloriesString is null
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        /* breakfastCaloriesTextView = findViewById(R.id.breakfastCaloriesTextView);
        lunchCaloriesTextView = findViewById(R.id.lunchCaloriesTextView);
        dinnerCaloriesTextView = findViewById(R.id.dinnerCaloriesTextView); */

        Button addBreakfastButton = findViewById(R.id.addBreakfastButton);
        addBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateMealPlan.this, ListRecipe.class);
                startActivityForResult(intent, 1);
            }
        });

        Button addLunchButton = findViewById(R.id.addLunchButton);
        addLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateMealPlan.this, ListRecipe.class);
                startActivityForResult(intent, 2);
            }
        });

        Button addDinnerButton = findViewById(R.id.addDinnerButton);
        addDinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateMealPlan.this, ListRecipe.class);
                startActivityForResult(intent, 3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            DataClass recipe = (DataClass) data.getSerializableExtra("recipe");
            breakfastRecipes.add(recipe);
            updateBreakfastCalories();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            DataClass recipe = (DataClass) data.getSerializableExtra("recipe");
            lunchRecipes.add(recipe);
            updateLunchCalories();
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            DataClass recipe = (DataClass) data.getSerializableExtra("recipe");
            dinnerRecipes.add(recipe);
            updateDinnerCalories();
        }
    }

    private void updateBreakfastCalories() {
        String totalCal = "";
        int totalCalories = 0, totalCalInt = 0;
        for (DataClass recipe : breakfastRecipes) {
            totalCal = recipe.getNumCal();
            totalCalInt = Integer.parseInt(totalCal);
            totalCalories += totalCalInt;
        }
        breakfastCaloriesTextView.setText("Breakfast Calories: " + totalCalories);
        // checkCalories(totalCalories, "breakfast");
    }

    private void updateLunchCalories() {
        String totalCal = "";
        int totalCalories = 0, totalCalInt = 0;
        for (DataClass recipe : lunchRecipes) {
            totalCal = recipe.getNumCal();
            totalCalInt = Integer.parseInt(totalCal);
            totalCalories += totalCalInt;
        }
        lunchCaloriesTextView.setText("Lunch Calories: " + totalCalories);
        // checkCalories(totalCalories, "lunch");
    }

    private void updateDinnerCalories() {
        String totalCal = "";
        int totalCalories = 0, totalCalInt = 0;
        for (DataClass recipe : dinnerRecipes) {
            totalCal = recipe.getNumCal();
            totalCalInt = Integer.parseInt(totalCal);
            totalCalories += totalCalInt;
        }
        lunchCaloriesTextView.setText("Lunch Calories: " + totalCalories);
        // checkCalories(totalCalories, "lunch");
    }
}