package com.example.cooksmart_v2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddRecipe extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cooksmart-testv2-default-rtdb.firebaseio.com/");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private static final int PICK_IMAGE_REQUEST = 1;

    public EditText recipeName, recipeType, cookTime, numCal, perPerson, ingredients, directions;
    Button btnAddRecipe, btnSelectImage;
    Uri imageUri;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        recipeName = findViewById(R.id.recipeName);
        recipeType = findViewById(R.id.recipeType);
        cookTime = findViewById(R.id.cookTime);
        numCal = findViewById(R.id.numCal);
        perPerson = findViewById(R.id.perPerson);
        ingredients = findViewById(R.id.ingredients);
        directions = findViewById(R.id.directions);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to select an image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            }
        });

        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all fields are filled
                if (areAllFieldsFilled()) {
                    if (imageUri != null) {
                        String recipeKey = databaseReference.child("recipes").push().getKey();
                        String recipeNameTxt = recipeName.getText().toString();
                        String recipeTypeTxt = recipeType.getText().toString();
                        String cookTimeTxt = cookTime.getText().toString();
                        String numCalTxt = numCal.getText().toString();
                        String perPersonTxt = perPerson.getText().toString();
                        String ingrdntTxt = ingredients.getText().toString();
                        String drctnTxt = directions.getText().toString();

                        StorageReference imageRef = storageReference.child("recipe_images").child(recipeKey + ".jpg");

                        imageRef.putFile(imageUri).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Get the download URL of the uploaded image
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    // Save the recipe details along with the image URL in the Realtime Database
                                    databaseReference.child("recipes").child(recipeKey).child("recipeName").setValue(recipeNameTxt);
                                    databaseReference.child("recipes").child(recipeKey).child("dietType").setValue(recipeTypeTxt);
                                    databaseReference.child("recipes").child(recipeKey).child("cookTime").setValue(cookTimeTxt);
                                    databaseReference.child("recipes").child(recipeKey).child("numCal").setValue(numCalTxt);
                                    databaseReference.child("recipes").child(recipeKey).child("mealPerPerson").setValue(perPersonTxt);
                                    databaseReference.child("recipes").child(recipeKey).child("ingredients").setValue(ingrdntTxt);
                                    databaseReference.child("recipes").child(recipeKey).child("directions").setValue(drctnTxt);
                                    databaseReference.child("recipes").child(recipeKey).child("imageUrl").setValue(imageUrl);

                                    Toast.makeText(AddRecipe.this, "Successfully Added Recipe.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ManageRecipe.class);
                                    startActivity(intent);
                                    finish();
                                });
                            } else {
                                Toast.makeText(AddRecipe.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(AddRecipe.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddRecipe.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }

                /* databaseReference.child("recipes").child(recipeKey).child("recipeName").setValue(recipeNameTxt);
                databaseReference.child("recipes").child(recipeKey).child("dietType").setValue(recipeTypeTxt);
                databaseReference.child("recipes").child(recipeKey).child("cookTime").setValue(cookTimeTxt);
                databaseReference.child("recipes").child(recipeKey).child("numCal").setValue(numCalTxt);
                databaseReference.child("recipes").child(recipeKey).child("mealPerPerson").setValue(perPersonTxt);
                databaseReference.child("recipes").child(recipeKey).child("ingredients").setValue(ingrdntTxt);
                databaseReference.child("recipes").child(recipeKey).child("directions").setValue(drctnTxt);
                databaseReference.child("recipes").child(recipeKey).child("imageUrl").setValue(imageUrl);

                Toast.makeText(AddRecipe.this, "Successfully Added Recipe.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ManageRecipe.class);
                startActivity(intent);
                finish(); */
            }
        });
    }

    private boolean areAllFieldsFilled() {
        return !recipeName.getText().toString().isEmpty() &&
                !recipeType.getText().toString().isEmpty() &&
                !cookTime.getText().toString().isEmpty() &&
                !numCal.getText().toString().isEmpty() &&
                !perPerson.getText().toString().isEmpty() &&
                !ingredients.getText().toString().isEmpty() &&
                !directions.getText().toString().isEmpty();
    }
}

