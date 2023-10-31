package com.journaldev.recyclerviewcardview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroceryList extends AppCompatActivity {

    private static RecyclerView recyclerViewGroceryList;
    private static ArrayList<MyDishes> listDishes = new ArrayList<>();
    //------------------------------------------------
    TextView titlepage, shoppingCartItemsNumber;
    SearchView searchBarView;
    CustomAdapterGroceryList customAdapterGroceryList;
    //--------------------------------------------------
    static ArrayList<MyList> groceryList = new ArrayList<>();
    ImageButton btnDeleteAllgrocery, btnSaveSgrocery;

    private void search(String str){
        ArrayList<MyList> newIngredientList = new ArrayList<>();
        for (MyList ingredient : groceryList){
            if(ingredient.getIngredientName().toLowerCase().contains(str.toLowerCase()) || ingredient.getIngredientValue().toLowerCase().contains(str.toLowerCase()) || ingredient.getIngredientMeasureMethod().toLowerCase().contains(str.toLowerCase())){
                newIngredientList.add(new MyList(ingredient.getIngredientName(), ingredient.getIngredientValue(), ingredient.getIngredientMeasureMethod(), ingredient.color));
            }
        }
        // reset data in adapter and not re-creating adapter:
        customAdapterGroceryList.setDataSet(newIngredientList);
        GroceryList.this.runOnUiThread(new Runnable() {
            @Override
            public void run() { customAdapterGroceryList.notifyDataSetChanged(); }
        });
        recyclerViewGroceryList.setAdapter(customAdapterGroceryList);
        customAdapterGroceryList.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_grocery_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        searchBarView = findViewById(R.id.searchBarView);
        recyclerViewGroceryList = findViewById(R.id.my_recycler_view_shopping_cart);
        btnDeleteAllgrocery = findViewById(R.id.btnDeleteAllgrocery);
        btnSaveSgrocery = findViewById(R.id.btnSaveSgrocery);
        BottomNavigationView bottomNav = findViewById(R.id.navbar);
        bottomNav.setSelectedItemId(R.id.navigation_grocerylist);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), Main.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_add:
                        startActivity(new Intent(getApplicationContext(), AddDish.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_shoppingcart:
                        startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_grocerylist:
                        return true;
                }
                return false;
            }
        });

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
        if (extras != null) {
            Bundle bundle = getIntent().getExtras();
            listDishes = (ArrayList<MyDishes>) bundle.getSerializable("togrocerylistforlistdishes");
        }

        if (groceryList.size() == 0) {
            for (MyDishes dish : listDishes) {
                for (String stringa : dish.getIngredients()) {
                    String[] temp = stringa.split(" ");
                    groceryList.add(new MyList(temp[0], temp[1], temp[2], dish.getColor()));
                }
            }
        }

        btnDeleteAllgrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder removeAllIngredientAlert = new AlertDialog.Builder(GroceryList.this);
                removeAllIngredientAlert.setTitle("Clear The Grocery List?");
                removeAllIngredientAlert.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groceryList.clear();
                        listDishes.clear();
                        customAdapterGroceryList.notifyDataSetChanged();
                    }
                });

                removeAllIngredientAlert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                removeAllIngredientAlert.show();
            }
        });

        btnSaveSgrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                to implement
                 */
            }
        });

        //working with data
        //recyclerViewGroceryList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGroceryList.setLayoutManager(new LinearLayoutManager(GroceryList.this));
        customAdapterGroceryList = new CustomAdapterGroceryList(groceryList, this);
        recyclerViewGroceryList.setAdapter(customAdapterGroceryList);
        recyclerViewGroceryList.scheduleLayoutAnimation();
    }

}
