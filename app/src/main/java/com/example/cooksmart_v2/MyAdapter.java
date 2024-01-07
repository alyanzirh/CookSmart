package com.example.cooksmart_v2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;
    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImageUrl()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getRecipeName());
        holder.recPriority.setText(dataList.get(position).getDietType());
        holder.recDesc.setText(dataList.get(position).getNumCal() + " Calories");

        final int currentPosition = holder.getAdapterPosition();

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRecipe.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImageUrl());
                intent.putExtra("Calories", dataList.get(holder.getAdapterPosition()).getNumCal());
                intent.putExtra("Recipe Name", dataList.get(holder.getAdapterPosition()).getRecipeName());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Diet Type", dataList.get(holder.getAdapterPosition()).getDietType());

                intent.putExtra("Cooking Time", dataList.get(currentPosition).getCookTime());
                intent.putExtra("Person", dataList.get(currentPosition).getMealPerPerson());
                intent.putExtra("Ingredients", dataList.get(currentPosition).getIngredients());
                intent.putExtra("Directions", dataList.get(currentPosition).getDirections());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recTitle, recPriority, recDesc;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recTitle = itemView.findViewById(R.id.recTitle);
        recPriority = itemView.findViewById(R.id.recPriority);
        recDesc = itemView.findViewById(R.id.recDesc);
    }
}
