package com.journaldev.recyclerviewcardview;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.flowlayout.FlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ShowDish  extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    TextView dishingnumbershowdish, dishshowdesc, dishnameshowdish, dishshowdate;
    String dishName, dishDesc, dishType, dishKey, dishDate;
    boolean dishInCart;
    ArrayList<String> ingredients = new ArrayList<>();
    FloatingActionButton editdishshowdish, deletedishshowdish;
    ImageButton backarrowbtnshowdish;
    ImageView incartbuttonshowdish;
    String URL = "https://applista-ed210-default-rtdb.europe-west1.firebasedatabase.app/";
    DatabaseReference databaseReference;
    MyDishes dish;
    int colorBg = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_show_dish);
        dishingnumbershowdish = findViewById(R.id.dishingnumbershowdish);
        dishshowdesc = findViewById(R.id.dishshowdesc);
        dishnameshowdish = findViewById(R.id.dishnameshowdish);
        editdishshowdish = findViewById(R.id.editdishshowdish);
        deletedishshowdish = findViewById(R.id.deletedishshowdish);
        backarrowbtnshowdish = findViewById(R.id.backarrowbtnshowdish);
        incartbuttonshowdish = findViewById(R.id.incartbuttonshowdish);
        dishshowdate = findViewById(R.id.dishshowdate);

        //get a value from previous page
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bundle bundle = getIntent().getExtras();
            dish = (MyDishes) bundle.getSerializable("toshowdishforlistdishes");
        }
        dishName = dish.getDishName();
        dishDesc = dish.getDishDesc();
        dishType = dish.getType();
        dishInCart = dish.getInCart();
        ingredients = dish.getIngredients();
        dishKey = dish.getKey();
        dishDate = dish.getDate();
        dishshowdate.setText(dishDate);

        if (dishInCart) incartbuttonshowdish.setBackgroundResource(R.drawable.incartshowdish);
        else incartbuttonshowdish.setBackgroundResource(R.drawable.notincartshowdish);

        //setting the color of background and the type icon
        //System.out.println(dishType);
        switch (dishType){
            case "appetizer" : colorBg = getResources().getColor(R.color.appetizerbackgroundcolor);  findViewById(R.id.imageView3).setBackgroundResource(R.drawable.appetizericon);  break;
            case "first" : colorBg = getResources().getColor(R.color.firstbackgroundcolor);  findViewById(R.id.imageView3).setBackgroundResource(R.drawable.pastaicon); break;
            case "second" : colorBg = getResources().getColor(R.color.secondbackgroundcolor);  findViewById(R.id.imageView3).setBackgroundResource(R.drawable.meaticon); break;
            case "side" : colorBg = getResources().getColor(R.color.sidebackgroundcolor);  findViewById(R.id.imageView3).setBackgroundResource(R.drawable.sideicon); break;
            case "dessert" : colorBg = getResources().getColor(R.color.dessertbackgroundcolor);  findViewById(R.id.imageView3).setBackgroundResource(R.drawable.desserticon); break;
            case "drink" : colorBg = getResources().getColor(R.color.drinkbackgroundcolor); findViewById(R.id.imageView3).setBackgroundResource(R.drawable.drinkicon); break;
            default: colorBg = getResources().getColor(R.color.mainblue);  break;
        }

        findViewById(R.id.backgrounddishshow).setBackgroundColor(colorBg);
        findViewById(R.id.collapsing_toolbar).setBackgroundColor(colorBg);

        ((AppBarLayout) findViewById(R.id.appbar)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;
            CollapsingToolbarLayout toolbarLayout = findViewById(R.id.collapsing_toolbar);
            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    findViewById(R.id.backgrounddishshow).setBackgroundColor(getResources().getColor(R.color.color_white));
                }else{
                    findViewById(R.id.backgrounddishshow).setBackgroundColor(colorBg);
                }
            }
        });

        dishnameshowdish.setText("" + dishName);
        dishshowdesc.setText("" + dishDesc);
        dishingnumbershowdish.setText("" + ingredients.size());
        fillAutoSpacingLayout();

        editdishshowdish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("toeditrecipefordish", (Serializable) dish);
                Intent intent = new Intent(getApplicationContext(), EditRecipe.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        deletedishshowdish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteDishShowDish = new AlertDialog.Builder(ShowDish.this); //  , R.style.MyAlertDialogStyle
                deleteDishShowDish.setTitle("Attention!");
                deleteDishShowDish.setMessage("Are you sure you want delete the dish?");
                deleteDishShowDish.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance(URL).getReference().child("ListApp").child("Dish" + dishKey).removeValue();
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                        finish();
                    }
                });
                deleteDishShowDish.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                deleteDishShowDish.show();
            }
        });

        backarrowbtnshowdish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void fillAutoSpacingLayout() {
        FlowLayout flowLayout = findViewById(R.id.flow);
        for (String text : ingredients) {
            TextView textView = buildLabel(text);
            flowLayout.addView(textView);
        }
    }

    private TextView buildLabel(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setPadding((int)dpToPx(16), (int)dpToPx(8), (int)dpToPx(16), (int)dpToPx(8));
        textView.setBackgroundResource(R.drawable.label_bg);
        return textView;
    }

    private float dpToPx(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int scrollRange = -1;
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.collapsing_toolbar);
        //Initialize the size of the scroll
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        //Check if the view is collapsed
        if (scrollRange + verticalOffset == 0) {
            findViewById(R.id.backgrounddishshow).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_white));
        }else{
            findViewById(R.id.backgrounddishshow).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.secondbackgroundcolor));
        }
    }
}
