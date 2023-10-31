package com.journaldev.recyclerviewcardview;

import java.io.Serializable;

public class MyList implements Serializable {

    String ingredientName;
    String ingredientValue;
    String ingredientMeasureMethod;
    boolean ingredientAdded;
    boolean ingredientRemoved;
    int color;

    public MyList(){

    }

    public MyList(String ingredientName, String ingredientValue, String ingredientType, int color) {
        this.ingredientName = ingredientName;
        this.ingredientValue = ingredientValue;
        this.ingredientMeasureMethod = ingredientType;
        ingredientAdded=false;
        ingredientRemoved=false;
        this.color=color;
    }

    public boolean isIngredientAdded() { return ingredientAdded; }

    public void setIngredientAdded(boolean ingredientAdded) { this.ingredientAdded = ingredientAdded; }

    public int getColor() { return color; }

    public void setColor(int color) { this.color = color; }

    public boolean isIngredientRemoved() { return ingredientRemoved; }

    public void setIngredientRemoved(boolean ingredientRemoved) { this.ingredientRemoved = ingredientRemoved; }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientValue() {
        return ingredientValue;
    }

    public void setIngredientValue(String ingredientValue) {
        this.ingredientValue = ingredientValue;
    }

    public String getIngredientMeasureMethod() {
        return ingredientMeasureMethod;
    }

    public void setIngredientMeasureMethod(String ingredientMeasureMethod) {
        this.ingredientMeasureMethod = ingredientMeasureMethod;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + " Ingredient Name: " + ingredientName + "\n").append(" Ingredient Weight: " + ingredientValue + "\n").append(" Ingredient measure method: " + ingredientMeasureMethod + "\n")
        .append("\n" + " color: " + color);
        if (ingredientAdded) sb.append("\n" + " status: added"  + "\n");
        else if (ingredientRemoved) sb.append("\n" + " status: removed"  + "\n");
        else sb.append("\n" + " status: not touched" + "\n");
        return sb.toString();
    }
}
