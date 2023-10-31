package com.journaldev.recyclerviewcardview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterShoppingCart extends RecyclerView.Adapter<CustomAdapterShoppingCart.MyViewHolder> {

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

    public CustomAdapterShoppingCart(ArrayList<MyDishes> data, Context context) {
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
        final int color = dataSet.get(listPosition).getColor();

        holder.itemView.findViewById(R.id.viewMain).setBackgroundColor(color);
        CardView cardView = holder.itemView.findViewById(R.id.card_view);
        ImageView imageState = holder.itemView.findViewById(R.id.imageView3);
        boolean allSelected = false;
        if (dataSet.get(listPosition).isSelected()) {
            imageState.setVisibility(View.VISIBLE);
            //holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_selection));
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_selection));
        }
        else {
            imageState.setVisibility(View.INVISIBLE);
            //holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
        }

        if (!multiSelection) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_selection));
                    //cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_selection2));
                    dataSet.get(listPosition).setSelected(true);
                    multiSelection = true;
                    ShoppingCart.btnDelete.setVisibility(View.VISIBLE);
                    ShoppingCart.btnDelete.setClickable(true);
                    ShoppingCart.btnAllSelection.setVisibility(View.VISIBLE);
                    ShoppingCart.btnAllSelection.setClickable(true);
                    notifyDataSetChanged();
                    return true;
                }
            });

        }else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!dataSet.get(listPosition).isSelected()) {
                        //holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_selection));
                        //cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_selection2));
                        //holder.itemView.findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
                        dataSet.get(listPosition).setSelected(true);
                    } else {
                        //holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
                        //cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
                        //holder.itemView.findViewById(R.id.imageView3).setVisibility(View.INVISIBLE);
                        dataSet.get(listPosition).setSelected(false);
                        boolean temp = false;
                        for (MyDishes dish : dataSet) {
                            if (dish.isSelected()) temp = true;
                        }
                        if (!temp) {
                            multiSelection = false;
                            ShoppingCart.btnDelete.setVisibility(View.INVISIBLE);
                            ShoppingCart.btnAllSelection.setVisibility(View.INVISIBLE);
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
