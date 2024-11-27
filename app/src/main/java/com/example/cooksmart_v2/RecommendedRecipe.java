package com.example.cooksmart_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecommendedRecipe extends AppCompatActivity {

    FloatingActionButton fab;
    DatabaseReference databaseRecipe, databaseUser;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    SearchView searchView;
    ImageView btnBack;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recipe_user);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        // searchView = findViewById(R.id.search);
        // searchView.clearFocus();
        title = (TextView) findViewById(R.id.title);

        title.setText("Recommended Recipes");
        fab.setVisibility(View.GONE);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserRecipeSection.class);
                startActivity(intent);
                finish();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(RecommendedRecipe.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(RecommendedRecipe.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new MyAdapter(RecommendedRecipe.this, dataList);
        recyclerView.setAdapter(adapter);

        databaseRecipe = FirebaseDatabase.getInstance().getReference().child("recipes");
        databaseUser = FirebaseDatabase.getInstance().getReference().child("users");
        dialog.show();

        databaseUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dietType")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dietTypeSnapshot) {
                        String userDietType = dietTypeSnapshot.getValue(String.class);
                        Log.d("Diet Type: ", userDietType);
                        eventListener = databaseRecipe.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                dataList.clear();
                                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                    DataClass dataRecipe = itemSnapshot.getValue(DataClass.class);
                                    if (dataRecipe != null && userDietType != null && userDietType.equals(dataRecipe.getDietType())) {
                                        dataRecipe.setKey(itemSnapshot.getKey());
                                        dataList.add(dataRecipe);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}