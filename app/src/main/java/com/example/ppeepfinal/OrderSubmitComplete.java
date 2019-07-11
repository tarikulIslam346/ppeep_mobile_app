package com.example.ppeepfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
//import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cdflynn.android.library.checkview.CheckView;



public class OrderSubmitComplete extends AppCompatActivity {

    TextView driverName,driverContact;

    ImageView driverImage;

    ProgressBar orderConfirmProgressbar;

    private  String userName,userPhoneNo;

    private UserDatabase mdb;


    Button sendSMS;
    EditText inputSMS;
    String message;
    TextView displayText,driverFound;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_submit_complete);




        mdb =  UserDatabase.getInstance(getApplicationContext());

        List<UserModel> user = mdb.userDAO().loadPhone();
        if(user.size() !=0 ){
            userName = user.get(0).getName();
            userPhoneNo = user.get(0).getPhone();
        }

//       message = inputSMS.getText().toString();
        LinearLayout parent = (LinearLayout) findViewById(R.id.show_snacbar_order_confirm);

        ImageView mImgCheck = (ImageView) findViewById(R.id.imageView);

        ((Animatable) mImgCheck.getDrawable()).start();

        driverName = (TextView) findViewById(R.id.tv_driver_name);

        driverContact = (TextView) findViewById(R.id.tv_contact);

        driverImage = (ImageView) findViewById(R.id.iv_driver_image);
         driverFound = (TextView) findViewById(R.id.tv_driver_found);
         driverFound.setVisibility(View.INVISIBLE);

        orderConfirmProgressbar = (ProgressBar) findViewById(R.id.progressBar_order_confirm) ;

        driverName.setVisibility(View.INVISIBLE);
        driverContact.setVisibility(View.INVISIBLE);

        Intent orderSubmitInten = getIntent();

        String driver = orderSubmitInten.getStringExtra("driver_name");

        String contact = orderSubmitInten.getStringExtra("contact");

        String imageUrl = orderSubmitInten.getStringExtra("profile_pic");


        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        Pusher pusher = new Pusher("6211c9a7cfb062fa410d", options);


        Channel channel = pusher.subscribe("my-channel");

        channel.bind("my-order-confirm-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                //Toast.makeText(getContext(),"Event : data",Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //JSONObject driverInfo;
                        ///try {
                           // driverInfo = new JSONObject(data);
                           // String phone = driverInfo.getString("driver");
                           // if(phone.equals(userPhoneNo)){
                                Toast.makeText(getApplicationContext()," Order confirm : ",Toast.LENGTH_LONG).show();
                        if(imageUrl != null){
                            URL getimageUrl = NetworkUtils.buildDriverIamgeUrl(imageUrl);
                            // new ImageLoadTask(driverImage).execute(getimageUrl);
                            Picasso.get().load(getimageUrl.toString()).into(driverImage);
                            orderConfirmProgressbar.setVisibility(View.INVISIBLE);
                            driverName.setText(driver);
                            driverContact.setText(contact);
                            driverName.setVisibility(View.VISIBLE);
                            driverContact.setVisibility(View.VISIBLE);
                            driverFound.setVisibility(View.VISIBLE);


                        }

                                //Snackbar.make(parent,"order confirm",Snackbar.LENGTH_INDEFINITE).show();
                                //mOrderNotification.setVisibility(View.VISIBLE);
                           // }

                       // } catch (JSONException e) {
                           // e.printStackTrace();
                      //  }


                        // System.out.println("Received event with data: " + data);
                        // mAccept.setText("ok");
                        // Log.d(TAG,"Call pusher ");
                    }
                });

            }
        });
        pusher.connect();









        if(driver!= null){
            driverName.setText(driver);
        }
        if(contact!=null){
            driverContact.setText(contact);
        }




    }



    @Override
    public void onDestroy() {
        super.onDestroy();



    }





}
