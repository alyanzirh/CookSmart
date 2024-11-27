package com.example.cooksmart_v2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditRecipe extends AppCompatActivity {

    Button btnUpdate, btnCancel;
    EditText recipeName, recipeType, cookTime, numCal, perPerson, ingredient, direction;
    ImageView imgRecipe;
    Uri uri;
    String imageURL;
    String key, oldImageURL;
    DatabaseReference dbref;
    StorageReference storef;
    String name, type, time, cal, person, ing, dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_recipe);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        recipeName = (EditText) findViewById(R.id.recipeName);
        recipeType = (EditText) findViewById(R.id.recipeType);
        cookTime = (EditText) findViewById(R.id.cookTime);
        numCal = (EditText) findViewById(R.id.numCal);
        perPerson = (EditText) findViewById(R.id.perPerson);
        ingredient = (EditText) findViewById(R.id.ingredient);
        direction = (EditText) findViewById(R.id.direction);
        imgRecipe = (ImageView) findViewById(R.id.imgRecipe);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            imgRecipe.setImageURI(uri);
                        } else {
                            Toast.makeText(EditRecipe.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(this).load(bundle.getString("Image")).into(imgRecipe);
            recipeName.setText(bundle.getString("Recipe Name"));
            recipeType.setText(bundle.getString("Diet Type"));
            cookTime.setText(bundle.getString("Cooking Time"));
            numCal.setText(bundle.getString("Calories"));
            perPerson.setText(bundle.getString("Person"));
            ingredient.setText(bundle.getString("Ingredients"));
            direction.setText(bundle.getString("Directions"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
        }

        dbref = FirebaseDatabase.getInstance().getReference().child("recipes");

        imgRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                Intent intent = new Intent(EditRecipe.this, ListRecipe.class);
                startActivity(intent);
            }
        });
    }

    public void updateData() {
        name = recipeName.getText().toString().trim();
        type = recipeType.getText().toString().trim();
        time = cookTime.getText().toString().trim();
        cal = numCal.getText().toString().trim();
        person = perPerson.getText().toString().trim();
        ing = ingredient.getText().toString().trim();
        dir = direction.getText().toString().trim();

        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference().child("recipes").child(key);

        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataClass existingRecipe = snapshot.getValue(DataClass.class);

                    if (!name.equals(existingRecipe.getRecipeName())) {
                        existingRecipe.setRecipeName(name);
                    }
                    if (!type.equals(existingRecipe.getDietType())) {
                        existingRecipe.setDietType(type);
                    }
                    if (!time.equals(existingRecipe.getCookTime())) {
                        existingRecipe.setCookTime(time);
                    }
                    if (!cal.equals(existingRecipe.getNumCal())) {
                        existingRecipe.setNumCal(cal);
                    }
                    if (!person.equals(existingRecipe.getMealPerPerson())) {
                        existingRecipe.setMealPerPerson(person);
                    }
                    if (!ing.equals(existingRecipe.getIngredients())) {
                        existingRecipe.setIngredients(ing);
                    }
                    if (!dir.equals(existingRecipe.getDirections())) {
                        existingRecipe.setDirections(dir);
                    }

                    if (uri != null) {
                        StorageReference imgRef = FirebaseStorage.getInstance().getReference().child("recipe_images").child(uri.getLastPathSegment());

                        imgRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            existingRecipe.setImageUrl(uri.toString());

                                            recipeRef.setValue(existingRecipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                                                        ref.delete();
                                                        Toast.makeText(EditRecipe.this, "Updated.", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(EditRecipe.this, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Toast.makeText(EditRecipe.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        recipeRef.setValue(existingRecipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditRecipe.this, "Updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(EditRecipe.this, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditRecipe.this, "Update cancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}