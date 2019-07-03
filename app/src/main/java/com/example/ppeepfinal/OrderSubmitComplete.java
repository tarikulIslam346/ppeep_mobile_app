package com.example.ppeepfinal;

import android.content.Intent;
import android.graphics.drawable.Animatable;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import cdflynn.android.library.checkview.CheckView;

public class OrderSubmitComplete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_submit_complete);
        ImageView mImgCheck = (ImageView) findViewById(R.id.imageView);
        ((Animatable) mImgCheck.getDrawable()).start();




    }
}
