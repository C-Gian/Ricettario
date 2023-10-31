package com.journaldev.recyclerviewcardview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class AddIngredients extends AppCompatActivity{

    ImageButton btnAddNewIngredient, btnSave, btnCancel;
    Button btnSaveDialog, btnCancelDialog;
    EditText ingredientName, quantityEditText;
    String dishName, dishDesc, history, dishKey, type;
    Dialog mydialog;
    ArrayList<String> ingredients = new ArrayList<>();
    ArrayList<String> tempIngredients = new ArrayList<>();
    protected ArrayAdapter<String> ingredientAdapter;
    SearchView searchBarView;
    boolean inCart;
    private static RecyclerView recyclerViewAddingredients;
    private static ArrayList<MyDishes> listDishes = new ArrayList<>();
    CustomAdapterAddingredients customAdapterAddingredients;
    MyDishes dish;

    private void search(String str){
        ArrayList<String> newIngredientList = new ArrayList<>();
        for (String ingredient : tempIngredients){
            String[] temp = ingredient.split(" ");
            String ingredientName = temp[0];
            String ingredientValue = temp[1];
            String ingredientType = temp[2];
            if(ingredientName.toLowerCase().contains(str.toLowerCase()) || ingredientValue.toLowerCase().contains(str.toLowerCase()) || ingredientType.toLowerCase().contains(str.toLowerCase())){
                newIngredientList.add(ingredient);
            }
        }
        // reset data in adapter and not re-creating adapter:
        customAdapterAddingredients.setDataSet(newIngredientList);
        AddIngredients.this.runOnUiThread(new Runnable() {
            @Override
            public void run() { customAdapterAddingredients.notifyDataSetChanged(); }
        });
        recyclerViewAddingredients.setAdapter(customAdapterAddingredients);
        customAdapterAddingredients.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        searchBarView = findViewById(R.id.searchBarView);
        recyclerViewAddingredients = findViewById(R.id.my_recycler_view_addingredients);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddNewIngredient = findViewById(R.id.btnAddNewIngredient);

        if (searchBarView != null){
            //searchView.setIconified(false);
            searchBarView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });

        }

        //get a value from previous page
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            ingredients = extras.getStringArrayList("toaddingredientsforingredients");
            tempIngredients = new ArrayList<>(ingredients);
            dishName = extras.getString("toaddingredientsfordishname");
            dishDesc = extras.getString("toaddingredientsfordishdesc");
            history = extras.getString("toaddingredientsforhistory");
            inCart = extras.getBoolean("toaddingredientsforincart");
            dishKey = extras.getString("toaddingredientsfordishkey");
            type = extras.getString("toaddingredientsfortype");
            Bundle bundle = getIntent().getExtras();
            dish = (MyDishes) bundle.getSerializable("toaddingredientsfordish");
        }

        btnAddNewIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mydialog = new Dialog(AddIngredients.this);
                mydialog.setContentView(R.layout.my_dialog);
                Window window = mydialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mydialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                window.setGravity(Gravity.CENTER);
                ingredientName = mydialog.findViewById(R.id.ingredientName);
                quantityEditText = mydialog.findViewById(R.id.quantityEditText);
                btnSaveDialog = mydialog.findViewById(R.id.btnSaveDialog);
                btnCancelDialog = mydialog.findViewById(R.id.btnCancelDialog);
                Spinner spinner = (Spinner) mydialog.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddIngredients.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.font_sizes));
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                btnSaveDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ingredientNameTemp = "";
                        String ingredientQuantityTemp = "";
                        String typeTemp = "";
                        if (ingredientName.getText().toString().length() == 0){
                            ingredientNameTemp = "NONAME";
                        }else{
                            ingredientNameTemp = Character.toUpperCase(ingredientName.getText().toString().charAt(0)) + ingredientName.getText().toString().substring(1);
                        }
                        if (quantityEditText.getText().toString().length() == 0){
                            ingredientQuantityTemp = "0";
                        }else{
                            ingredientQuantityTemp = Character.toUpperCase(quantityEditText.getText().toString().charAt(0)) + quantityEditText.getText().toString().substring(1);
                        }
                        typeTemp = spinner.getSelectedItem().toString();
                        tempIngredients.add(ingredientNameTemp + " " + ingredientQuantityTemp + " " + typeTemp);
                        customAdapterAddingredients.setDataSet(tempIngredients);
                        customAdapterAddingredients.notifyDataSetChanged();
                        mydialog.dismiss();
                    }

                });
                btnCancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });
                mydialog.show();
            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (history.equals("EditRecipe")){
                    dish.setIngredients(tempIngredients);
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(AddIngredients.this, EditRecipe.class);
                    bundle.putSerializable("toeditrecipefordish", (Serializable) dish);
                    intent.putExtras(bundle);
                    System.out.println("add to edit:" + dish.toString());
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(AddIngredients.this, AddDish.class);
                    ingredients = tempIngredients;
                    intent.putExtra("toadddishforingredients", ingredients);
                    intent.putExtra("toadddishfordishname", dishName);
                    intent.putExtra("toadddishfordishdesc", dishDesc);
                    intent.putExtra("toadddishforincart", inCart);
                    intent.putExtra("toadddishfortype", type);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (history.equals("EditRecipe")){
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(AddIngredients.this, EditRecipe.class);
                    bundle.putSerializable("toeditrecipefordish", (Serializable) dish);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(AddIngredients.this, AddDish.class);
                    intent.putExtra("toadddishforingredients", ingredients);
                    intent.putExtra("toadddishfordishname", dishName);
                    intent.putExtra("toadddishfordishdesc", dishDesc);
                    intent.putExtra("toadddishfornonricalcolarekey", "nonricalcolarelaakey");
                    intent.putExtra("toadddishforincart", inCart);
                    intent.putExtra("toadddishfortype", type);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //working with data
        //recyclerViewAddingredients.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAddingredients.setLayoutManager(new LinearLayoutManager(AddIngredients.this));
        customAdapterAddingredients = new CustomAdapterAddingredients(tempIngredients, this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerViewAddingredients);
        recyclerViewAddingredients.setAdapter(customAdapterAddingredients);
        recyclerViewAddingredients.scheduleLayoutAnimation();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            tempIngredients.remove(viewHolder.getAdapterPosition());
            customAdapterAddingredients.setDataSet(tempIngredients);
            customAdapterAddingredients.notifyDataSetChanged();
        }
    };
    
}
