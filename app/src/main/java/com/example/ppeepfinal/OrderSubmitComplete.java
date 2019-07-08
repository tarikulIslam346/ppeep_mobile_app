package com.example.ppeepfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
//import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.ppeepfinal.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import cdflynn.android.library.checkview.CheckView;

public class OrderSubmitComplete extends AppCompatActivity {

    TextView driverName,driverContact;
    ImageView driverImage;
    ProgressBar orderConfirmProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_submit_complete);
        ImageView mImgCheck = (ImageView) findViewById(R.id.imageView);
        ((Animatable) mImgCheck.getDrawable()).start();
        driverName = (TextView) findViewById(R.id.tv_driver_name);
        driverContact = (TextView) findViewById(R.id.tv_contact);
        driverImage = (ImageView) findViewById(R.id.iv_driver_image);
        orderConfirmProgressbar = (ProgressBar) findViewById(R.id.progressBar_order_confirm) ;
        Intent orderSubmitInten = getIntent();
        String driver = orderSubmitInten.getStringExtra("driver_name");
        String contact = orderSubmitInten.getStringExtra("contact");
        String imageUrl = orderSubmitInten.getStringExtra("profile_pic");
        if(imageUrl != null){
            URL getimageUrl = NetworkUtils.buildDriverIamgeUrl(imageUrl);
           // new ImageLoadTask(driverImage).execute(getimageUrl);
            Picasso.get().load(getimageUrl.toString()).into(driverImage);
            orderConfirmProgressbar.setVisibility(View.INVISIBLE);


        }

        if(driver!= null){
            driverName.setText(driver);
        }
        if(contact!=null){
            driverContact.setText(contact);
        }




    }


}
