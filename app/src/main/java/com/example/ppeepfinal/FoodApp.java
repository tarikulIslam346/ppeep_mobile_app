package com.example.ppeepfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class FoodApp extends AppCompatActivity {
Toolbar foodToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_app);
        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);

    }


}
