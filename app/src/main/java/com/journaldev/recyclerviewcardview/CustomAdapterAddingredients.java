package com.journaldev.recyclerviewcardview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterAddingredients extends RecyclerView.Adapter<CustomAdapterAddingredients.MyViewHolder> {

    Context context;
    ArrayList<String> ingredients;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName, ingredientValue, ingredientType;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.ingredientName = (TextView) itemView.findViewById(R.id.ingredientName);
            this.ingredientValue = (TextView) itemView.findViewById(R.id.ingredientValue);
            this.ingredientType = (TextView) itemView.findViewById(R.id.ingredientType);
        }
    }

    public void setDataSet(ArrayList<String> ingredients) {this.ingredients = ingredients; }

    public CustomAdapterAddingredients(ArrayList<String> ingredients, Context context) {
        this.ingredients=ingredients;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout_addingredients, parent, false);
        view.setOnClickListener(Main.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        String[] temp = ingredients.get(listPosition).split(" ");
        holder.ingredientName.setText(temp[0]);
        holder.ingredientValue.setText(temp[1].trim());
        holder.ingredientType.setText(temp[2].trim());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
