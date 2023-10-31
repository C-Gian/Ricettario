package com.journaldev.recyclerviewcardview;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import java.util.Calendar;

public class AddDish extends AppCompatActivity{

    EditText dishName, dishDesc;
    ImageButton btnSave, btnCancel, btnAddIngredients, btnresetadddish;
    DatabaseReference databaseReference, referenceforcheck;
    Integer dishNum = new Random().nextInt();
    String URL = "https://applista-ed210-default-rtdb.europe-west1.firebasedatabase.app/";
    String keyDish = Integer.toString(dishNum);
    boolean conditionToCheckDouble, conditionIfNoDouble;
    ArrayList<String> ingredients = new ArrayList<>();
    private RecyclerView recyclerViewImage;
    public ViewPager2 viewPager2;
    String type;

    public int getRandomColorCode(){
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_dish);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        viewPager2 = findViewById(R.id.viewpagerimageslider);
        List<SlideItem> slideItemList = new ArrayList<>();
        slideItemList.add(new SlideItem(R.drawable.appetizericon)); //2131165286
        slideItemList.add(new SlideItem(R.drawable.pastaicon)); //2131165392
        slideItemList.add(new SlideItem(R.drawable.meaticon)); //2131165368
        slideItemList.add(new SlideItem(R.drawable.sideicon)); //2131165400
        slideItemList.add(new SlideItem(R.drawable.desserticon)); //2131165339
        slideItemList.add(new SlideItem(R.drawable.drinkicon)); //2131165341
        viewPager2.setAdapter(new SliderAdapter(slideItemList, viewPager2));
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
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setPadding(300, 0, 300, 0);
        dishName = findViewById(R.id.dishName);
        dishDesc = findViewById(R.id.dishDesc);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        btnresetadddish = findViewById(R.id.btnresetadddish);
        btnAddIngredients = findViewById(R.id.btnAddIngredients);
        TextView noingredientstextadddish = findViewById(R.id.noingredientstextadddish);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavBar);
        bottomNav.setSelectedItemId(R.id.navigation_add);
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
                        return true;
                    case R.id.navigation_shoppingcart:
                        startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                        finish();
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

        //get a value from previous page
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (extras.getStringArrayList("toadddishforingredients") != null) ingredients = extras.getStringArrayList("toadddishforingredients");
            if (extras.getString("toadddishfordishname") != null) dishName.setText(extras.getString("toadddishfordishname"));
            if (extras.getString("toadddishfordishdesc") != null) dishDesc.setText(extras.getString("toadddishfordishdesc"));
            if (extras.getString("toadddishfortype") != null) type = extras.getString("toadddishfortype");
            if (ingredients.size() == 0){
                noingredientstextadddish.setVisibility(View.VISIBLE);
            }else{
                noingredientstextadddish.setVisibility(View.INVISIBLE);
            }
        }

        LinearLayout ingredientsLineaLayout = findViewById(R.id.ingredientsLineaLayout);
        LayoutInflater inflater = LayoutInflater.from(this);

        if (ingredients != null) {
            for (String s : ingredients) {
                TextView textView = buildLabel(s);
                ingredientsLineaLayout.addView(textView);
            }
        }

        ingredientsLineaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDish.this, AddIngredients.class);
                intent.putExtra("toaddingredientsforingredients", ingredients);
                intent.putExtra("toaddingredientsfordishname", dishName.getText().toString());
                intent.putExtra("toaddingredientsfordishdesc", dishDesc.getText().toString());
                intent.putExtra("toaddingredientsforhistory", "AddDish");
                startActivity(intent);
                finish();
            }
        });

        conditionToCheckDouble = false;
        conditionIfNoDouble = false;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = getRandomColorCode();
                //check if dish is already in the list
                referenceforcheck = FirebaseDatabase.getInstance(URL).getReference().child("ListApp");
                referenceforcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String tempNameDish = "";
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dishName.getText().toString().equals(dataSnapshot1.getValue(MyDishes.class).getDishName())) {
                                tempNameDish = dataSnapshot1.getValue(MyDishes.class).getDishName();
                                conditionToCheckDouble = true;
                                break;
                            }
                        }
                        if (conditionToCheckDouble) {
                            AlertDialog.Builder doubleDishConfirm = new AlertDialog.Builder(AddDish.this); //  , R.style.MyAlertDialogStyle
                            doubleDishConfirm.setTitle("Attention! " + tempNameDish + " Already exist");
                            doubleDishConfirm.setMessage("Add Anyway?");
                            doubleDishConfirm.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    conditionToCheckDouble = false;
                                    //insert data to database
                                    databaseReference = FirebaseDatabase.getInstance(URL).getReference().child("ListApp").child("Dish" + dishNum);
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
                                            if (dishName.getText().toString().equals(null) || dishName.getText().toString().length() == 0) dataSnapshot.getRef().child("dishName").setValue("NONAME");
                                            else {
                                                String dishNameTemp = Character.toUpperCase(dishName.getText().toString().charAt(0)) + dishName.getText().toString().substring(1);
                                                dataSnapshot.getRef().child("dishName").setValue(dishNameTemp);
                                            }
                                            if (dishDesc.getText().toString().equals(null) || dishDesc.getText().toString().length() == 0) dataSnapshot.getRef().child("dishDesc").setValue("NODESC");
                                            else {
                                                String dishDescTemp = Character.toUpperCase(dishDesc.getText().toString().charAt(0)) + dishDesc.getText().toString().substring(1);
                                                dataSnapshot.getRef().child("dishDesc").setValue(dishDescTemp);
                                            }
                                            dataSnapshot.getRef().child("ingredients").setValue(ingredients);
                                            dataSnapshot.getRef().child("key").setValue(keyDish);
                                            dataSnapshot.getRef().child("color").setValue(color);
                                            dataSnapshot.getRef().child("inCart").setValue(false);
                                            dataSnapshot.getRef().child("type").setValue(type);
                                            String temp = "";
                                            for (String s : Calendar.getInstance().getTime().toString().split(" ")){
                                                temp += s + " ";
                                            }
                                            dataSnapshot.getRef().child("date").setValue(temp);
                                            Intent intent = new Intent(AddDish.this, Main.class);
                                            conditionIfNoDouble = false;
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });

                            doubleDishConfirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    conditionToCheckDouble = false;
                                    Intent intent = new Intent(AddDish.this, Main.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            doubleDishConfirm.show();

                        } else {
                            conditionIfNoDouble = true;
                        }


                        if (conditionIfNoDouble) {
                            //insert data to database
                            databaseReference = FirebaseDatabase.getInstance(URL).getReference().child("ListApp").child("Dish" + dishNum);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //SlideItem temp = SliderAdapter.getCurrentImageResource(viewPager2.getCurrentItem());
                                    switch (viewPager2.getCurrentItem()){
                                        case 0 : type = "appetizer"; break;
                                        case 1 : type = "first"; break;
                                        case 2 : type = "second"; break;
                                        case 3 : type = "side"; break;
                                        case 4 : type = "dessert"; break;
                                        case 5 : type = "drink"; break;
                                    }
                                    if (dishName.getText().toString().equals(null) || dishName.getText().toString().length() == 0) dataSnapshot.getRef().child("dishName").setValue("NONAME");
                                    else {
                                        String dishNameTemp = Character.toUpperCase(dishName.getText().toString().charAt(0)) + dishName.getText().toString().substring(1);
                                        dataSnapshot.getRef().child("dishName").setValue(dishNameTemp);
                                    }
                                    if (dishDesc.getText().toString().equals(null) || dishDesc.getText().toString().length() == 0) dataSnapshot.getRef().child("dishDesc").setValue("NODESC");
                                    else {
                                        String dishDescTemp = Character.toUpperCase(dishDesc.getText().toString().charAt(0)) + dishDesc.getText().toString().substring(1);
                                        dataSnapshot.getRef().child("dishDesc").setValue(dishDescTemp);
                                    }
                                    dataSnapshot.getRef().child("ingredients").setValue(ingredients);
                                    dataSnapshot.getRef().child("key").setValue(keyDish);
                                    dataSnapshot.getRef().child("color").setValue(color);
                                    dataSnapshot.getRef().child("inCart").setValue(false);
                                    dataSnapshot.getRef().child("type").setValue(type);
                                    String temp = "";
                                    for (String s : Calendar.getInstance().getTime().toString().split(" ")){
                                        temp += s + " ";
                                    }
                                    dataSnapshot.getRef().child("date").setValue(temp);
                                    Intent intent = new Intent(AddDish.this, Main.class);
                                    conditionIfNoDouble = false;
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        btnAddIngredients = findViewById(R.id.btnAddIngredients);

        btnAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager2.getCurrentItem()){
                    case 0 : type = "appetizer"; break;
                    case 1 : type = "first"; break;
                    case 2 : type = "second"; break;
                    case 3 : type = "side"; break;
                    case 4 : type = "dessert"; break;
                    case 5 : type = "drink"; break;
                }
                Intent intent = new Intent( AddDish.this, AddIngredients.class);
                intent.putExtra("toaddingredientsforingredients", ingredients);
                intent.putExtra("toaddingredientsfordishname", dishName.getText().toString());
                intent.putExtra("toaddingredientsfordishdesc", dishDesc.getText().toString());
                intent.putExtra("toaddingredientsforhistory", "AddDish");
                intent.putExtra("toaddingredientsfortype", type);
                startActivity(intent);
                finish();
            }
        });

        btnresetadddish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredients.clear();
                ingredientsLineaLayout.removeAllViews();
                dishName.setText("");
                dishDesc.setText("");
                viewPager2.setCurrentItem(0);
            }
        });
    }

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

    private float dpToPx(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}