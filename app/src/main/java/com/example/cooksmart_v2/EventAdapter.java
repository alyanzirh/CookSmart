package com.example.cooksmart_v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

/* public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.planner_cell, parent, false);

        // TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        TextView breakfastTV = convertView.findViewById(R.id.breakfastTV);
        TextView lunchTV = convertView.findViewById(R.id.lunchTV);
        TextView dinnerTV = convertView.findViewById(R.id.dinnerTV);

        // String eventTitle = event.getName() +" "+ CalendarUtils.formattedTime(event.getTime());
        // String eventTitle = event.getName();
        String eventTitle = event.getName();
        /* if(breakfastTV == null){
            breakfastTV.setText(eventTitle);
        } else if(lunchTV == null){
            lunchTV.setText(eventTitle);
        }
        else if(dinnerTV == null){
            dinnerTV.setText(eventTitle);
        }
        breakfastTV.setText(eventTitle);
        // eventCellTV.setText(eventTitle);
        return convertView;
    }
} */

public class EventAdapter extends ArrayAdapter<MealPlanEntry> {

    public EventAdapter(@NonNull Context context, List<MealPlanEntry> mealPlanEntries) {
        super(context, 0, mealPlanEntries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        MealPlanEntry currentMealPlanEntry = getItem(position);

        // Find the ShapeableImageView in the list_item layout
        ShapeableImageView recImage = listItemView.findViewById(R.id.recImage);

        // Get the image URL from the current MealPlanEntry
        String imageUrl = currentMealPlanEntry.getImageUrl();

        // Load image into the ShapeableImageView using Glide
        Glide.with(getContext())
                .load(imageUrl)
                .into(recImage);


        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentMealPlanEntry.getRecipeName());

        TextView dietTypeTV = listItemView.findViewById(R.id.dietType);
        dietTypeTV.setText(currentMealPlanEntry.getDietType());

        TextView caloriesTextView = listItemView.findViewById(R.id.calories_text_view);
        caloriesTextView.setText(String.valueOf(currentMealPlanEntry.getCalories()) + " Calories");

        return listItemView;
    }
}
