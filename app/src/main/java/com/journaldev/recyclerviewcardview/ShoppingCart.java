package com.journaldev.recyclerviewcardview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ShoppingCart extends AppCompatActivity {

    private static RecyclerView recyclerViewShoppingCart;
    private static ArrayList<MyDishes> listDishes;
    public static ImageButton btnSort, btnAllSelection, btnDelete;
    //------------------------------------------------
    TextView titlepage, shoppingCartItemsNumber;
    SearchView searchBarView;
    String URL = "https://applista-ed210-default-rtdb.europe-west1.firebasedatabase.app/";
    DatabaseReference databaseReference;
    CustomAdapterShoppingCart customAdapterShoppingCart;
    //--------------------------------------------------
    ImageButton btnFinish, btnDeleteAll;
    boolean allSelected, sorted;

    private void search(String str) {
        ArrayList<MyDishes> searchBarDishesList = new ArrayList<>();
        for (MyDishes dish : listDishes) {
            if (dish.getDishName().toLowerCase().contains(str.toLowerCase()) || dish.getDishDesc().toLowerCase().contains(str.toLowerCase())) {
                searchBarDishesList.add(dish);
            }
        }

        // reset data in adapter and not re-creating adapter:
        customAdapterShoppingCart.setDataSet(searchBarDishesList);
        ShoppingCart.this.runOnUiThread(new Runnable() {
            @Override
            public void run() { customAdapterShoppingCart.notifyDataSetChanged();     }
        });

        //doesAdapter = new DoesAdapter(MainActivity.this, list);
        recyclerViewShoppingCart.setAdapter(customAdapterShoppingCart);
        customAdapterShoppingCart.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_shopping_cart);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        searchBarView = findViewById(R.id.searchBarView);
        recyclerViewShoppingCart = findViewById(R.id.my_recycler_view_shopping_cart);
        btnFinish = findViewById(R.id.btnFinish);
        btnSort = findViewById(R.id.btnSort);
        btnAllSelection = findViewById(R.id.btnAllSelection);
        btnDelete = findViewById(R.id.btnDelete);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        BottomNavigationView bottomNav = findViewById(R.id.navbar);
        bottomNav.setSelectedItemId(R.id.navigation_shoppingcart);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
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
                        return true;
                    case R.id.navigation_grocerylist:
                        startActivity(new Intent(getApplicationContext(), GroceryList.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        if (searchBarView != null) {
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

        //working with data
        recyclerViewShoppingCart.setLayoutManager(new LinearLayoutManager(this));

        listDishes = new ArrayList<MyDishes>();

        recyclerViewShoppingCart.setLayoutManager(new LinearLayoutManager(ShoppingCart.this));
        customAdapterShoppingCart = new CustomAdapterShoppingCart(listDishes, this);
        recyclerViewShoppingCart.setAdapter(customAdapterShoppingCart);
        recyclerViewShoppingCart.scheduleLayoutAnimation();

        //get data from firebase
        databaseReference = FirebaseDatabase.getInstance(URL).getReference().child("ListApp");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    MyDishes dish = dataSnapshot1.getValue(MyDishes.class);
                    if (dish.getInCart()){
                        listDishes.add(dish);
                    }
                }

                // reset data in adapter and not re-creating adapter:
                customAdapterShoppingCart.setDataSet(listDishes);
                ShoppingCart.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() { customAdapterShoppingCart.notifyDataSetChanged();     }
                });

                //doesAdapter = new DoesAdapter(MainActivity.this, list);
                recyclerViewShoppingCart.setAdapter(customAdapterShoppingCart);
                customAdapterShoppingCart.notifyDataSetChanged();
                recyclerViewShoppingCart.scheduleLayoutAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //set code to show an error
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listDishes.size() != 0) {
                    if (sorted) {
                        Collections.sort(listDishes, Collections.reverseOrder());
                        // reset data in adapter and not re-creating adapter:
                        customAdapterShoppingCart.setDataSet(listDishes);
                        ShoppingCart.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customAdapterShoppingCart.notifyDataSetChanged();
                            }
                        });
                        recyclerViewShoppingCart.setAdapter(customAdapterShoppingCart);
                        customAdapterShoppingCart.notifyDataSetChanged();
                        sorted = false;
                        btnSort.setBackgroundResource(R.drawable.btnsortup);
                    } else {
                        Collections.sort(listDishes);
                        // reset data in adapter and not re-creating adapter:
                        customAdapterShoppingCart.setDataSet(listDishes);
                        ShoppingCart.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customAdapterShoppingCart.notifyDataSetChanged();
                            }
                        });
                        recyclerViewShoppingCart.setAdapter(customAdapterShoppingCart);
                        customAdapterShoppingCart.notifyDataSetChanged();
                        sorted = true;
                        btnSort.setBackgroundResource(R.drawable.btnsortdown);
                    }
                }
            }
        });

        btnAllSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listDishes.size() != 0) {
                    if (allSelected) {
                        for (MyDishes dish : listDishes) {
                            dish.setSelected(false);
                        }
                        allSelected = false;
                        //CardView cardView = findViewById(R.id.card_view);
                        //cardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_white));
                        btnAllSelection.setBackgroundResource(R.drawable.allselectiondeselected);
                        btnDelete.setVisibility(View.INVISIBLE);
                        btnDelete.setClickable(false);
                        btnAllSelection.setVisibility(View.INVISIBLE);
                        btnAllSelection.setClickable(false);
                    } else {
                        for (MyDishes dish : listDishes) {
                            dish.setSelected(true);
                            //CardView cardView = findViewById(R.id.card_view);
                            //cardView.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_selection));
                        }
                        allSelected = true;
                        btnAllSelection.setBackgroundResource(R.drawable.allselectionselected);
                        btnDelete.setVisibility(View.VISIBLE);
                        btnDelete.setClickable(true);
                        btnAllSelection.setVisibility(View.VISIBLE);
                        btnAllSelection.setClickable(true);
                    }
                    customAdapterShoppingCart.setDataSet(listDishes);
                    ShoppingCart.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customAdapterShoppingCart.notifyDataSetChanged();
                        }
                    });
                    recyclerViewShoppingCart.setAdapter(customAdapterShoppingCart);
                    customAdapterShoppingCart.notifyDataSetChanged();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MyDishes> temp = new ArrayList<>();
                for (MyDishes dish : listDishes) {
                    if (dish.isSelected()) {
                        FirebaseDatabase.getInstance(URL).getReference().child("ListApp").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    if (ds.getKey().contains(dish.getKey())){
                                        ds.getRef().child("inCart").setValue(false);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        temp.add(dish);
                    }
                }
                listDishes.removeAll(temp);
                customAdapterShoppingCart.multiSelection = false;
                btnDelete.setVisibility(View.INVISIBLE);
                btnDelete.setClickable(false);
                btnAllSelection.setVisibility(View.INVISIBLE);
                btnAllSelection.setClickable(false);
                btnAllSelection.setBackgroundResource(R.drawable.allselectiondeselected);
                customAdapterShoppingCart.setDataSet(listDishes);
                recyclerViewShoppingCart.setAdapter(customAdapterShoppingCart);
                customAdapterShoppingCart.notifyDataSetChanged();
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder removeAllDishAlert = new AlertDialog.Builder(ShoppingCart.this);
                removeAllDishAlert.setTitle("Remove All Dish From Cart?");
                removeAllDishAlert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance(URL).getReference().child("ListApp").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    for (MyDishes dish : listDishes){
                                        if (ds.getKey().contains(dish.getKey())){
                                            ds.getRef().child("inCart").setValue(false);
                                        }
                                    }
                                }
                                listDishes.clear();
                                startActivity(new Intent(getApplicationContext(), Main.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                removeAllDishAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                removeAllDishAlert.show();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("togrocerylistforlistdishes", (Serializable) listDishes);
                Intent intent = new Intent(getApplicationContext(), GroceryList.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

}
