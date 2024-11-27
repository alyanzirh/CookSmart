package com.example.cooksmart_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailRecipe extends AppCompatActivity {

    TextView detailTitle, cookTime, detailCals, detailPerson, detailLang, detailIng, detailDir;
    ImageView detailImage, btnBack;
    FloatingActionButton deleteButton, editButton, favButton;
    String key = "";
    String imageUrl = "";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mealPlanDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_recipe);

        cookTime = findViewById(R.id.cookTime);
        detailPerson = findViewById(R.id.detailPerson);
        detailIng = findViewById(R.id.detailIng);
        detailDir = findViewById(R.id.detailDir);

        detailCals = findViewById(R.id.detailCals);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        favButton = findViewById(R.id.favButton);
        // mealPlanDatabase = FirebaseDatabase.getInstance().getReference().child("mealPlan");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // String date = "2024-02-9";
        mealPlanDatabase = FirebaseDatabase.getInstance().getReference().child("mealPlan").child(userId);

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMealSelectionDialog();
            }
        });
        detailLang = findViewById(R.id.detailLang);

        // btnBack = (ImageView) findViewById(R.id.btnBack);

        /* btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListRecipe.class);
                startActivity(intent);
                finish();
            }
        }); */

        String from = getIntent().getStringExtra("from");

        /* btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equals("recommended")) {
                    // User came from RecommendedRecipe page, so go back to that page
                    Intent intent = new Intent(getApplicationContext(), RecommendedRecipe.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (from.equals("list")) {
                    // User came from ListRecipe page, so go back to that page
                    Intent intent = new Intent(getApplicationContext(), UserListRecipe.class);
                    startActivity(intent);
                }
                finish();
            }
        }); */

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailCals.setText(bundle.getString("Calories"));
            detailTitle.setText(bundle.getString("Recipe Name"));
            detailLang.setText(bundle.getString("Diet Type"));

            cookTime.setText(bundle.getString("Cooking Time"));
            detailPerson.setText(bundle.getString("Person"));
            detailIng.setText(bundle.getString("Ingredients"));
            detailDir.setText(bundle.getString("Directions"));

            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        if (user != null) {
            String userEmail = user.getEmail();
            if ("nutritionist@cooksmart.com".equals(userEmail)) {
                // Show the buttons for the nutritionist
                deleteButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.VISIBLE);
                favButton.setVisibility(View.GONE);
            } else {
                // Hide the buttons for general users
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                favButton.setVisibility(View.VISIBLE);
            }
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailRecipe.this);
                builder.setTitle("Delete Recipe");
                builder.setMessage("Are you sure you want to delete this recipe?");

                // Add a positive button to confirm deletion
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("recipes");
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                reference.child(key).removeValue();
                                Toast.makeText(DetailRecipe.this, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ListRecipe.class));
                                finish();
                            }
                        });
                    }
                });

                // Add a negative button to cancel deletion
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailRecipe.this, EditRecipe.class)
                        .putExtra("Recipe Name", detailTitle.getText().toString())
                        .putExtra("Diet Type", detailLang.getText().toString())
                        .putExtra("Calories", detailCals.getText().toString())
                        .putExtra("Cooking Time", cookTime.getText().toString())
                        .putExtra("Person", detailPerson.getText().toString())
                        .putExtra("Ingredients", detailIng.getText().toString())
                        .putExtra("Directions", detailDir.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
    }

    public void showMealSelectionDialog() {
        final String[] meals = {"Breakfast", "Lunch", "Dinner", "Snack"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select meal")
                .setSingleChoiceItems(meals, -1, null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView listView = ((AlertDialog) dialog).getListView();
                        int selectedPosition = listView.getCheckedItemPosition();
                        if (selectedPosition != ListView.INVALID_POSITION) {
                            String selectedMeal = meals[selectedPosition];
                            showConfirmDialog(selectedMeal);
                        } else {
                            // Handle no meal selected
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showConfirmDialog(final String selectedMeal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm add to meal planner")
                .setMessage("Are you sure you want to add this recipe to your " + selectedMeal + " meal?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDatePicker(selectedMeal);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showDatePicker(String selectedMeal) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String selectedDateString = format.format(selectedDate.getTime());

                // Save the selected date and the recipe details in the database
                saveMealPlanEntry(selectedDateString, selectedMeal);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Set minimum date for the DatePicker to today's date
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    // no targetCals
    /* public void saveMealPlanEntry(String date) {
        Bundle bundle = getIntent().getExtras();
        String mealPlanKey = mealPlanDatabase.push().getKey();
        String recipeName = bundle.getString("Recipe Name");
        String dietType = bundle.getString("Diet Type");
        String calories = bundle.getString("Calories");
        String cookingTime = bundle.getString("Cooking Time");
        String person = bundle.getString("Person");
        String ingredients = bundle.getString("Ingredients");
        String directions = bundle.getString("Directions");
        String imageUrl = bundle.getString("Image");

        DatabaseReference dateRef = mealPlanDatabase.child(date);
        dateRef.child(mealPlanKey).setValue(new MealPlanEntry(mealPlanKey, recipeName, dietType, calories, cookingTime, person, ingredients, directions, imageUrl, date));

        Toast.makeText(this, "Recipe added to meal planner", Toast.LENGTH_SHORT).show();
    } */

    // with targetCals but duplicate recipes entry
    /* public void saveMealPlanEntry(String date) {
        // Retrieve the user's target calories from the database
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int targetCals = dataSnapshot.child("targetCals").getValue(Integer.class);
                    Log.d("TargetCals", "Target Calories: " + targetCals);

                    // Check if the dateRef already contains data for the selected date
                    DatabaseReference dateRef = mealPlanDatabase.child(date);
                    dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int totalCalories = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MealPlanEntry mealPlanEntry = snapshot.getValue(MealPlanEntry.class);
                                // Calculate the total calories of all recipes for that date
                                totalCalories += Integer.parseInt(mealPlanEntry.getCalories());
                            }

                            // Compare the total calories with the user's target calories
                            if (totalCalories <= targetCals) {
                                // Proceed with adding the recipe to the meal planner
                                Bundle bundle = getIntent().getExtras();
                                String mealPlanKey = mealPlanDatabase.push().getKey();
                                String recipeName = bundle.getString("Recipe Name");
                                String dietType = bundle.getString("Diet Type");
                                String calories = bundle.getString("Calories");
                                String cookingTime = bundle.getString("Cooking Time");
                                String person = bundle.getString("Person");
                                String ingredients = bundle.getString("Ingredients");
                                String directions = bundle.getString("Directions");
                                String imageUrl = bundle.getString("Image");

                                // Save the meal plan entry
                                DatabaseReference mealPlanEntryRef = dateRef.child(mealPlanKey);
                                mealPlanEntryRef.setValue(new MealPlanEntry(mealPlanKey, recipeName, dietType, calories, cookingTime, person, ingredients, directions, imageUrl, date));

                                Toast.makeText(DetailRecipe.this, "Recipe added to meal planner", Toast.LENGTH_SHORT).show();
                            } else {
                                // Display a toast message indicating that the user has exceeded their target calories
                                Toast.makeText(DetailRecipe.this, "You have exceeded your target calories.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    } */

    // with targetCals and no duplicate but totalCals in meal plan still exceed targetCals
    /* public void saveMealPlanEntry(String date) {
        // Retrieve the user's target calories from the database
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int targetCals = dataSnapshot.child("targetCals").getValue(Integer.class);

                    // Check if the dateRef already contains data for the selected date
                    DatabaseReference dateRef = mealPlanDatabase.child(date);
                    dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int totalCalories = 0;
                            boolean recipeExists = false;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MealPlanEntry mealPlanEntry = snapshot.getValue(MealPlanEntry.class);
                                // Calculate the total calories of all recipes for that date
                                totalCalories += Integer.parseInt(mealPlanEntry.getCalories());

                                Bundle bundle = getIntent().getExtras();
                                // Check if the recipe is already present for that date
                                if (mealPlanEntry.getRecipeName().equals(bundle.getString("Recipe Name"))) {
                                    recipeExists = true;
                                    break;
                                }
                            }

                            // If the recipe does not already exist and total calories <= target calories, add the recipe
                            if (!recipeExists && totalCalories <= targetCals) {
                                // Proceed with adding the recipe to the meal planner
                                Bundle bundle = getIntent().getExtras();
                                String mealPlanKey = mealPlanDatabase.push().getKey();
                                String recipeName = bundle.getString("Recipe Name");
                                String dietType = bundle.getString("Diet Type");
                                String calories = bundle.getString("Calories");
                                String cookingTime = bundle.getString("Cooking Time");
                                String person = bundle.getString("Person");
                                String ingredients = bundle.getString("Ingredients");
                                String directions = bundle.getString("Directions");
                                String imageUrl = bundle.getString("Image");

                                // Save the meal plan entry
                                DatabaseReference mealPlanEntryRef = dateRef.child(mealPlanKey);
                                mealPlanEntryRef.setValue(new MealPlanEntry(mealPlanKey, recipeName, dietType, calories, cookingTime, person, ingredients, directions, imageUrl, date));

                                Toast.makeText(DetailRecipe.this, "Recipe added to meal planner", Toast.LENGTH_SHORT).show();
                            } else if (recipeExists) {
                                // Display a toast message indicating that the recipe has already been added for that date
                                Toast.makeText(DetailRecipe.this, "Recipe already exists in meal planner for this date", Toast.LENGTH_SHORT).show();
                            } else {
                                // Display a toast message indicating that the user has exceeded their target calories
                                Toast.makeText(DetailRecipe.this, "You have exceeded your target calories.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    } */

    public void saveMealPlanEntry(String date, String meal) {
        // Retrieve the user's target calories from the database
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String targetCalsTxt = dataSnapshot.child("targetCals").getValue(String.class);
                    int targetCals = Integer.parseInt(targetCalsTxt);

                    // Check if the dateRef already contains data for the selected date
                    DatabaseReference dateRef = mealPlanDatabase.child(date);
                    dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int totalCalories = 0;
                            boolean recipeExists = false;
                            Bundle bundle = getIntent().getExtras();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MealPlanEntry mealPlanEntry = snapshot.getValue(MealPlanEntry.class);
                                // Calculate the total calories of all recipes for that date
                                totalCalories += Integer.parseInt(mealPlanEntry.getCalories());
                                // Check if the recipe is already present for that date
                                /* if (mealPlanEntry.getRecipeName().equals(bundle.getString("Recipe Name"))) {
                                    recipeExists = true;
                                    break;
                                } */
                            }

                            // Calculate the calories of the new recipe to be added
                            int newRecipeCalories = Integer.parseInt(bundle.getString("Calories"));

                            // If the recipe does not already exist and total calories + new recipe calories <= target calories, add the recipe
                            // if (!recipeExists && totalCalories + newRecipeCalories <= targetCals)
                            if (totalCalories + newRecipeCalories <= targetCals) {
                                // Proceed with adding the recipe to the meal planner
                                // Bundle bundle = getIntent().getExtras();
                                String mealPlanKey = mealPlanDatabase.push().getKey();
                                String recipeName = bundle.getString("Recipe Name");
                                String dietType = bundle.getString("Diet Type");
                                String calories = bundle.getString("Calories");
                                String cookingTime = bundle.getString("Cooking Time");
                                String person = bundle.getString("Person");
                                String ingredients = bundle.getString("Ingredients");
                                String directions = bundle.getString("Directions");
                                String imageUrl = bundle.getString("Image");


                                // Save the meal plan entry
                                DatabaseReference mealPlanEntryRef = dateRef.child(mealPlanKey);
                                mealPlanEntryRef.setValue(new MealPlanEntry(mealPlanKey, recipeName, dietType, calories, cookingTime, person, ingredients, directions, imageUrl, date, meal));
                                Toast.makeText(DetailRecipe.this, "Recipe added to meal planner", Toast.LENGTH_SHORT).show();
                            }
                            /* else if (recipeExists) {
                                // Display a toast message indicating that the recipe has already been added for that date
                                Toast.makeText(DetailRecipe.this, "Recipe already exists in meal planner for this date", Toast.LENGTH_SHORT).show();
                            } */
                            else {
                                // Display a toast message indicating that adding the new recipe would exceed the user's target calories
                                Toast.makeText(DetailRecipe.this, "Adding this recipe would exceed your target calories", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

}