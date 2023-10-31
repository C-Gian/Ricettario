package com.journaldev.recyclerviewcardview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EditRecipe extends AppCompatActivity {

    EditText dishNameEdit, dishDescEdit;
    ImageButton btnSave, btnAddIngredients;
    DatabaseReference databaseReference;
    String URL = "https://applista-ed210-default-rtdb.europe-west1.firebasedatabase.app/", type;
    ArrayList<String> ingredients = new ArrayList<>();
    private ViewPager2 viewPager2;
    MyDishes dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_recipe);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        /*
        getting data from the previous activity
         */
        Bundle bundle = getIntent().getExtras();
        dish = (MyDishes) bundle.getSerializable("toeditrecipefordish");

        /*
        setting vars finding id
         */
        viewPager2 = findViewById(R.id.viewpagerimageslideredit);
        dishNameEdit = findViewById(R.id.dishNameEdit);
        dishDescEdit = findViewById(R.id.dishDescEdit);
        btnSave = findViewById(R.id.btnSave);
        btnAddIngredients = findViewById(R.id.btnAddIngredients);
        TextView noingredientstextedit = findViewById(R.id.noingredientstextedit);
        LinearLayout llingredientsshowedit = findViewById(R.id.llingredientsshowedit);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavBar);
        bottomNav.setSelectedItemId(R.id.navigation_home);

        dishNameEdit.setText(dish.getDishName());
        dishDescEdit.setText(dish.getDishDesc());
        type = dish.getType();
        ingredients = dish.getIngredients();
        final String keydish = dish.getKey();
        databaseReference = FirebaseDatabase.getInstance(URL).getReference().child("ListApp").child("Dish" + keydish);

        List<SlideItem> slideItemList = new ArrayList<>();
        slideItemList.add(new SlideItem(R.drawable.appetizericon)); //2131165288
        slideItemList.add(new SlideItem(R.drawable.pastaicon)); //2131165401
        slideItemList.add(new SlideItem(R.drawable.meaticon)); //2131165376
        slideItemList.add(new SlideItem(R.drawable.sideicon)); //2131165410
        slideItemList.add(new SlideItem(R.drawable.desserticon)); //2131165345
        slideItemList.add(new SlideItem(R.drawable.drinkicon)); //2131165349
        viewPager2.setAdapter(new SliderAdapter(slideItemList, viewPager2));
        if (type != null){
            switch (type){
                case "appetizer" : viewPager2.setCurrentItem(0); break;
                case "first" : viewPager2.setCurrentItem(1); break;
                case "second" : viewPager2.setCurrentItem(2); break;
                case "side" : viewPager2.setCurrentItem(3); break;
                case "dessert" : viewPager2.setCurrentItem(4); break;
                case "drink" : viewPager2.setCurrentItem(5); break;
            }
        }else{
            viewPager2.setCurrentItem(0);
        }
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setFadingEdgeLength(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position*4);
                page.setScaleY(0.85f + r * 0.15f);
                page.setScaleX(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setPadding(300, 0, 300, 0);
        LinearLayout ll = findViewById(R.id.llingredientsshowedit);
        if (ingredients.size() == 0){
            noingredientstextedit.setVisibility(View.VISIBLE);
        }else{
            noingredientstextedit.setVisibility(View.INVISIBLE);
        }
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
                        Intent intent = new Intent(getApplicationContext(), ShoppingCart.class);
                        finish();
                        //intent.putExtra("dishes", list);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
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

        if (ingredients != null){
            for (String s : ingredients){
                TextView textView = buildLabel(s);
                ll.addView(textView);
            }
        }

        llingredientsshowedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(EditRecipe.this, AddIngredients.class);
                intent.putExtra("toaddingredientsforingredients", ingredients);
                intent.putExtra("toaddingredientsfordishname", dishNameEdit.getText().toString());
                intent.putExtra("toaddingredientsfordishdesc", dishDescEdit.getText().toString());
                intent.putExtra("toaddingredientsfordishkey", keydish);
                intent.putExtra("toaddingredientsforhistory", "EditRecipe");
                bundle.putSerializable("toaddingredientsfordish", (Serializable) dish);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        switch (viewPager2.getCurrentItem()){
                            case 0 : type = "appetizer"; break;
                            case 1 : type = "first"; break;
                            case 2 : type = "second"; break;
                            case 3 : type = "side"; break;
                            case 4 : type = "dessert"; break;
                            case 5 : type = "drink"; break;
                        }

                        String dishNameTemp = Character.toUpperCase(dishNameEdit.getText().toString().charAt(0)) + dishNameEdit.getText().toString().substring(1);
                        String dishDescTemp = Character.toUpperCase(dishDescEdit.getText().toString().charAt(0)) + dishDescEdit.getText().toString().substring(1);
                        if (dishNameEdit.getText().toString().equals(null) || dishNameEdit.getText().toString().length() == 0) dataSnapshot.getRef().child("dishName").setValue("NONAME");
                        else dataSnapshot.getRef().child("dishName").setValue(dishNameTemp);

                        if (dishDescEdit.getText().toString().equals(null) || dishDescEdit.getText().toString().length() == 0) dataSnapshot.getRef().child("dishDesc").setValue("NODESC");
                        else dataSnapshot.getRef().child("dishDesc").setValue(dishDescTemp);

                        dataSnapshot.getRef().child("ingredients").setValue(ingredients);
                        dataSnapshot.getRef().child("type").setValue(type);
                        dish.setDishName(dishNameTemp);
                        dish.setDishDesc(dishDescTemp);
                        dish.setIngredients(ingredients);
                        dish.setType(type);
                        Intent intent = new Intent(getApplicationContext(), ShowDish.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("toshowdishforlistdishes", (Serializable) dish);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(EditRecipe.this, AddIngredients.class);
                intent.putExtra("toaddingredientsforingredients", ingredients);
                intent.putExtra("toaddingredientsfordishname", dishNameEdit.getText().toString());
                intent.putExtra("toaddingredientsfordishdesc", dishDescEdit.getText().toString());
                intent.putExtra("toaddingredientsfordishkey", keydish);
                intent.putExtra("toaddingredientsforhistory", "EditRecipe");
                bundle.putSerializable("toaddingredientsfordish", (Serializable) dish);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    /*
    method to build label programmatically
     */
    private TextView buildLabel(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setPadding((int)dpToPx(16), (int)dpToPx(8), (int)dpToPx(16), (int)dpToPx(8));
        textView.setBackgroundResource(R.drawable.label_bg);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        textView.setLayoutParams(params);
        return textView;
    }

    /*
    method to build label programmatically
     */
    private float dpToPx(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}