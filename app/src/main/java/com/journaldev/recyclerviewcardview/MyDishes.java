package com.journaldev.recyclerviewcardview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyDishes implements Serializable, Comparable<MyDishes> {

    String dishName;
    String dishDesc;
    String key;
    String numberOfIngredients;
    ArrayList<String> ingredients = new ArrayList<>();
    boolean inCart;
    int color;
    boolean selected;
    String type;
    String date;

    public MyDishes(){

    }

    public MyDishes(String dishName, String dishDesc, String key, ArrayList<String> ingredients, String numberOfIngredients, boolean flag, int color) {
        this.dishName = dishName;
        this.dishDesc = dishDesc;
        this.key = key;
        this.ingredients = ingredients;
        this.numberOfIngredients = numberOfIngredients;
        this.inCart = flag;
        this.color=color;
        this.selected=false;
        this.type="all";
        date = Calendar.getInstance().getTime().toString();
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getType() { return type;  }

    public void setType(String type) { this.type = type; }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean getInCart() {
        return inCart;
    }

    public void setInCart(boolean flag) {
        this.inCart = flag;
    }

    public String getNumberOfIngredients() {
        return numberOfIngredients;
    }

    public void setNumberOfIngredients(String numeroingredientidoes) { this.numberOfIngredients = numeroingredientidoes; }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishDesc() {
        return dishDesc;
    }

    public void setDishDesc(String dishDesc) {
        this.dishDesc = dishDesc;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + " Dish Name: " + dishName + "\n").append(" Dish Desc: " + dishDesc + "\n").append(" Dish Ingredients: " + ingredients.toString() + "\n")
        .append(" color: " + color + "\n").append(" selected: " + selected + "\n").append(" inCart: " + inCart).append(" type: " + type + "\n").append(" date: " + date + "\n");
        return sb.toString();
    }

    @Override
    public int compareTo(MyDishes myDishes) {
        return this.getDishName().compareTo(myDishes.getDishName());
    }
}
