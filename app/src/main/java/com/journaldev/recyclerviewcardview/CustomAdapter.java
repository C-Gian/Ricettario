package com.journaldev.recyclerviewcardview;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<MyDishes> dataSet;
    Context context;
    String URL = "https://applista-ed210-default-rtdb.europe-west1.firebasedatabase.app/";
    //private int counterOnCreateViewHolder = 0;
    //private int counterOnBindViewHolder = 0;
    //private static final String LOG_TAG = "RecyclerViewAdapter";
    public static boolean multiSelection;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dishName, dishDesc, ingredients, dishNumberIngredients;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.dishName = (TextView) itemView.findViewById(R.id.dishName);
            this.dishDesc = (TextView) itemView.findViewById(R.id.dishDesc);
            this.ingredients = (TextView) itemView.findViewById(R.id.dishIngredients);
            this.dishNumberIngredients = (TextView) itemView.findViewById(R.id.dishNumberIngredients);
        }
    }

    public void setDataSet(ArrayList<MyDishes> ds) { this.dataSet = ds; }

    public CustomAdapter(ArrayList<MyDishes> data, Context context) {
        this.dataSet = data;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(LOG_TAG, "onCreateViewHolder (" + ++counterOnCreateViewHolder + ")");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        view.setOnClickListener(Main.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        //Log.d(LOG_TAG, "counterOnBindViewHolder (" + ++counterOnBindViewHolder + ")");
        holder.dishName.setText(dataSet.get(listPosition).getDishName());
        holder.dishDesc.setText(dataSet.get(listPosition).getDishDesc());
        holder.dishNumberIngredients.setText((dataSet.get(listPosition).getIngredients()).size() + "");

        final String getDIshName = dataSet.get(listPosition).getDishName();
        final String getDishDesc = dataSet.get(listPosition).getDishDesc();
        final ArrayList<String> getIngredients = dataSet.get(listPosition).getIngredients();
        final String getKey = dataSet.get(listPosition).getKey();
        final String getType = dataSet.get(listPosition).getType();
        final String getDate = dataSet.get(listPosition).getDate();
        final int color = dataSet.get(listPosition).getColor();

        holder.itemView.findViewById(R.id.viewMain).setBackgroundColor(color);
        CardView cardView = holder.itemView.findViewById(R.id.card_view);
        ImageView imageState = holder.itemView.findViewById(R.id.imageView3);
        if (dataSet.get(listPosition).isSelected()) {
            imageState.setVisibility(View.VISIBLE);
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_selection));
        }
        else {
            imageState.setVisibility(View.INVISIBLE);
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
        }

        if (!multiSelection) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dataSet.get(listPosition).setSelected(true);
                    multiSelection = true;
                    Main.btnDelete.setVisibility(View.VISIBLE);
                    Main.btnDelete.setClickable(true);
                    Main.btnAllSelection.setVisibility(View.VISIBLE);
                    Main.btnAllSelection.setClickable(true);
                    Main.btnAddToCart.setVisibility(View.VISIBLE);
                    Main.btnAddToCart.setClickable(true);
                    notifyDataSetChanged();
                    return true;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("toshowdishforlistdishes", (Serializable) dataSet.get(listPosition));
                    Intent intent = new Intent(context, ShowDish.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!dataSet.get(listPosition).isSelected()) {
                        dataSet.get(listPosition).setSelected(true);
                    } else {
                        dataSet.get(listPosition).setSelected(false);
                        boolean temp = false;
                        for (MyDishes dish : dataSet) {
                            if (dish.isSelected()) temp = true;
                        }
                        if (!temp) {
                            multiSelection = false;
                            Main.btnDelete.setVisibility(View.INVISIBLE);
                            Main.btnAllSelection.setVisibility(View.INVISIBLE);
                            Main.btnAddToCart.setVisibility(View.INVISIBLE);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
