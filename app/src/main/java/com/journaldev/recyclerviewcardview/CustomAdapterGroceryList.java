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

public class CustomAdapterGroceryList extends RecyclerView.Adapter<CustomAdapterGroceryList.MyViewHolder> {

    Context context;
    ArrayList<MyList> ingredients;
    boolean added, removed;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName, ingredientValue, ingredientType;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.ingredientName = (TextView) itemView.findViewById(R.id.ingredientName);
            this.ingredientValue = (TextView) itemView.findViewById(R.id.ingredientValue);
            this.ingredientType = (TextView) itemView.findViewById(R.id.ingredientType);
        }
    }

    public void setDataSet(ArrayList<MyList> ingredients) {this.ingredients = ingredients; }

    public CustomAdapterGroceryList(ArrayList<MyList> ingredients, Context context) {
        this.ingredients=ingredients;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout_grocerylist, parent, false);
        view.setOnClickListener(Main.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.ingredientName.setText(ingredients.get(listPosition).getIngredientName());
        holder.ingredientValue.setText(ingredients.get(listPosition).getIngredientValue().trim());
        holder.ingredientType.setText(ingredients.get(listPosition).getIngredientMeasureMethod().trim());
        //holder.itemView.findViewById(R.id.vieww).setBackgroundColor(ingredients.get(listPosition).getColor());
        ImageButton ingredientAdded, ingredientRemoved;
        ingredientAdded = (ImageButton) holder.itemView.findViewById(R.id.ingredientAdd);
        ingredientRemoved = (ImageButton) holder.itemView.findViewById(R.id.ingredientRemove);
        if (ingredients.get(listPosition).isIngredientAdded()) {
            ConstraintLayout temp1 = holder.itemView.findViewById(R.id.linearLayout29);
            int light_green = context.getResources().getColor(R.color.light_green);
            temp1.setBackgroundColor(light_green);
            holder.itemView.findViewById(R.id.vieww).setBackgroundColor(Color.GREEN);
            ingredientAdded.setBackgroundResource(R.drawable.addedicon);
            ingredientRemoved.setVisibility(holder.itemView.INVISIBLE);
            View tempRemovedLine1 = holder.itemView.findViewById(R.id.removedline);
            tempRemovedLine1.setVisibility(View.INVISIBLE);
        }
        else if (ingredients.get(listPosition).isIngredientRemoved()) {
            ConstraintLayout temp2 = holder.itemView.findViewById(R.id.linearLayout29);
            int light_red = context.getResources().getColor(R.color.light_red);
            holder.itemView.findViewById(R.id.vieww).setBackgroundColor(Color.RED);
            temp2.setBackgroundColor(light_red);
            ingredientRemoved.setBackgroundResource(R.drawable.removedicon);
            ingredientAdded.setVisibility(holder.itemView.INVISIBLE);
            View tempRemovedLine2 = holder.itemView.findViewById(R.id.removedline);
            tempRemovedLine2.setVisibility(View.VISIBLE);
        }
        else{
            ConstraintLayout temp3 = holder.itemView.findViewById(R.id.linearLayout29);
            int white = context.getResources().getColor(R.color.color_white);
            temp3.setBackgroundColor(white);
            ingredientAdded.setBackgroundResource(R.drawable.addicon);
            ingredientRemoved.setBackgroundResource(R.drawable.removeicon);
            ingredientAdded.setVisibility(View.VISIBLE);
            ingredientRemoved.setVisibility(View.VISIBLE);
            View tempRemovedLine3 = holder.itemView.findViewById(R.id.removedline);
            tempRemovedLine3.setVisibility(View.INVISIBLE);
        }
        ingredientAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredients.get(listPosition).isIngredientAdded()) {
                    ingredients.get(listPosition).setIngredientAdded(false);
                    holder.itemView.findViewById(R.id.vieww).setBackgroundColor(context.getResources().getColor(R.color.secondaryorange));
                }else{
                    ingredients.get(listPosition).setIngredientAdded(true);
                }
                notifyDataSetChanged();
            }
        });

        ingredientRemoved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredients.get(listPosition).isIngredientRemoved()) {
                    ingredients.get(listPosition).setIngredientRemoved(false);
                    holder.itemView.findViewById(R.id.vieww).setBackgroundColor(context.getResources().getColor(R.color.secondaryorange));
                }else{
                    ingredients.get(listPosition).setIngredientRemoved(true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
