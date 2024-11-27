package com.example.cooksmart_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class PlannerRecipeList extends Activity {
    private DatabaseReference mDatabase;
    private ListView mRecipeListView;
    private ArrayList<DataClass> mRecipeList;
    private ArrayAdapter<DataClass> mAdapter;
    private List<DataClass> selectedRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_recipe_list);

        ListView recipeListView = findViewById(R.id.recipeListView);
        Button doneButton = findViewById(R.id.doneButton);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("recipes");
        mRecipeListView = findViewById(R.id.recipeListView);
        mRecipeList = new ArrayList<>();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRecipeList);
        mRecipeListView.setAdapter(mAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRecipeList.clear();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    DataClass recipe = recipeSnapshot.getValue(DataClass.class);
                    mRecipeList.add(recipe);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataClass selectedRecipe = (DataClass) parent.getItemAtPosition(position);
                if (selectedRecipes.contains(selectedRecipe)) {
                    selectedRecipes.remove(selectedRecipe);
                } else {
                    selectedRecipes.add(selectedRecipe);
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedRecipes", (ArrayList<DataClass>) selectedRecipes);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
