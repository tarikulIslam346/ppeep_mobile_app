package com.example.ppeepfinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable;
//import android.support.v7.app.AppCompatActivity;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.ppeepfinal.data.UserCurrentOrder;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.ImageCircleOfPicasso;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.card.MaterialCardView;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.util.HttpAuthorizer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;


public class OrderSubmitComplete extends AppCompatActivity {

    TextView driverName,driverContact;

    ImageView driverImage,driverCallIcon;

    ProgressBar orderConfirmProgressbar;

    private  String userName,userPhoneNo,OrderId=null;

    private UserDatabase mdb;
    MaterialCardView orderConfirm,orderDeliver/*,orderOnTheWay*/;


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

        driverCallIcon = (ImageView) findViewById(R.id.callDriverId);

         driverFound = (TextView) findViewById(R.id.tv_driver_found);
         driverFound.setVisibility(View.INVISIBLE);

        orderConfirmProgressbar = (ProgressBar) findViewById(R.id.progressBar_order_confirm) ;

        orderConfirm = (MaterialCardView)findViewById(R.id.order_confirm_card);
        orderConfirm.setVisibility(View.INVISIBLE);
        orderDeliver=(MaterialCardView)findViewById(R.id.order_deliver_card);
        orderDeliver.setVisibility(View.INVISIBLE);
        //orderOnTheWay=(MaterialCardView)findViewById(R.id.food_on_the_way);
       // orderOnTheWay.setVisibility(View.INVISIBLE);

        driverName.setVisibility(View.INVISIBLE);
        driverContact.setVisibility(View.INVISIBLE);
        driverImage.setVisibility(View.INVISIBLE);
        driverCallIcon.setVisibility(View.INVISIBLE);




        Intent orderSubmitInten = getIntent();

        String driver = orderSubmitInten.getStringExtra("driver_name");

        String orderId = orderSubmitInten.getStringExtra("orderId");

        String message = orderSubmitInten.getStringExtra("message");

        String contact = orderSubmitInten.getStringExtra("contact");

        String imageUrl = orderSubmitInten.getStringExtra("profile_pic");


        if(orderId !=null){
            /* Sent firebase notification to driver if order has exist*/
            OrderId = orderId;
            URL sendNotificationURL = NetworkUtils.buildUrl(NetworkUtils.SENT_DRIVER_NOTIFICATION);
            new SentNotificationTask().execute(sendNotificationURL);
        }




        if(message==null)pusherConnection( orderId,driver, imageUrl,  contact);
        else{
            orderConfirmProgressbar.setVisibility(View.INVISIBLE);
        }


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /* Trying to update through broadcast receiver .
         * Broadcast receiver unregister here */

       /* IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.ppeepfinal.CUSTOM_INTENT");
        OrderReciver receiver = new OrderReciver();
        registerReceiver(receiver, intentFilter);*/
    }

    private  void pusherConnection( String orderId,String driver, String imageUrl, String contact){

        /* Fix it
         * This pusher connection using public channel
         * Need to setup private channel
         * Like this..

        HttpAuthorizer authorizer = new HttpAuthorizer("https://foodexpress.com.bd/ppeep/public/broadcasting/auth");

        Toast.makeText(getApplicationContext(), ""+authorizer, Toast.LENGTH_SHORT).show();

        PusherOptions options = new PusherOptions().setAuthorizer(authorizer);*/

        UserCurrentOrder userCurrentOrder = new UserCurrentOrder(Integer.valueOf(orderId),0);
        mdb.userCurrentOrderDAO().insertCurrentOrder(userCurrentOrder);

        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        Pusher pusher = new Pusher("6211c9a7cfb062fa410d", options);



        Channel channel = pusher.subscribe("ppeep-order."+orderId);

        // PrivateChannel channel2 = pusher.subscribePrivate("private-orderConfirm."+orderId);


        channel.bind("my-order-confirm-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                //Toast.makeText(getContext(),"Event : data",Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject driverInfo;
                        int OrderId = 0;

                        try {
                            driverInfo = new JSONObject(data);
                            OrderId = driverInfo.getInt("order_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addNotification("Your order has been accepted");
                        UserCurrentOrder userCurrentOrder = mdb.userCurrentOrderDAO().loadCurrentOrderById(Integer.valueOf(orderId));
                        userCurrentOrder.setOrderStatus(1);
                        mdb.userCurrentOrderDAO().updateCurrentOrder(userCurrentOrder);

                        // Toast.makeText(getApplicationContext()," Order confirm : ",Toast.LENGTH_LONG).show();
                        if(imageUrl != null){
                            driverImage.setVisibility(View.VISIBLE);
                            URL getimageUrl = NetworkUtils.buildDriverIamgeUrl(imageUrl);

                            Picasso.get().load(getimageUrl.toString()).transform(new ImageCircleOfPicasso()).into(driverImage);

                            orderConfirmProgressbar.setVisibility(View.INVISIBLE);
                            driverName.setText(driver);
                            driverContact.setText(contact);
                            driverName.setVisibility(View.VISIBLE);
                            driverContact.setVisibility(View.VISIBLE);
                            driverFound.setVisibility(View.VISIBLE);
                            // Initialize an intent to open dialer app with specified phone number
                            // It open the dialer app and allow user to call the number manually
                            driverCallIcon.setVisibility(View.VISIBLE);
                            driverCallIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    // Send phone number to intent as data
                                    intent.setData(Uri.parse("tel:" + "contact"));
                                    // Start the dialer app activity with number
                                    startActivity(intent);
                                }
                            });



                        }

                    }
                });

            }
        });

        channel.bind("driver-order-confirm-event", new SubscriptionEventListener() {


            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject driverInfo;
                        int OrderId = 0;

                        try {
                            driverInfo = new JSONObject(data);
                            OrderId = driverInfo.getInt("order_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(OrderId != 0) {

                            UserCurrentOrder userCurrentOrder = mdb.userCurrentOrderDAO().loadCurrentOrderById(Integer.valueOf(orderId));
                            userCurrentOrder.setOrderStatus(2);
                            mdb.userCurrentOrderDAO().updateCurrentOrder(userCurrentOrder);

                            orderConfirm.setVisibility(View.VISIBLE);
                        }


                        Toast.makeText(getApplicationContext()," Order deliver By driver ",Toast.LENGTH_LONG).show();
                        //Intent homePageIntent = new Intent(OrderSubmitComplete.this,FoodApp.class);
                        if(OrderId != 0) {
                            addNotification("Your order has been deliverd");

                            //homePageIntent.putExtra("order_id",String.valueOf(OrderId));
                        }
                        //homePageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        //startActivity(homePageIntent);


                    }
                });

            }
        });

        channel.bind("my-order-deliver-event", new SubscriptionEventListener() {


            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject driverInfo;
                        int OrderId = 0;

                        try {
                            driverInfo = new JSONObject(data);
                            OrderId = driverInfo.getInt("order_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        UserCurrentOrder userCurrentOrder = mdb.userCurrentOrderDAO().loadCurrentOrderById(Integer.valueOf(orderId));
                       // userCurrentOrder.setOrderStatus(3);
                        if(userCurrentOrder != null)mdb.userCurrentOrderDAO().deleteCurrentOrder(userCurrentOrder);
                        orderDeliver.setVisibility(View.VISIBLE);
                        // }


                        Toast.makeText(getApplicationContext()," Order confirm By driver ",Toast.LENGTH_LONG).show();
                        //Intent homePageIntent = new Intent(OrderSubmitComplete.this,FoodApp.class);
                        if(OrderId != 0) {
                            addNotification("Your order has been confirmed");

                            //homePageIntent.putExtra("order_id",String.valueOf(OrderId));
                        }
                        //homePageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        //startActivity(homePageIntent);


                    }
                });

            }
        });






        pusher.connect();
    }

    private void addNotification(String text) {
        Intent it = new Intent(this, OrderSubmitComplete.class);
        // Snackbar.make(R.id.layout_home_page),"Order_has",Snackbar.LENGTH_INDEFINITE).show();
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), it, 0);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int ico_notification = R.drawable.ic_account_circle_black_24dp;
        int color = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);

        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "orderconfirm_channel";
        CharSequence name = "Channel Order";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,CHANNEL_ID)
                        .setSmallIcon(ico_notification)
                        .setContentTitle(getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text))
                        .setSound(soundUri)
                        .setColor(color)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000})
                        .setContentText(text);

        mBuilder.setContentIntent(contentIntent);
        Notification notification = mBuilder.build();

        mNotificationManager.notify(0, notification);

    }
    public class SentNotificationTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String DriverResults = null;
            try {
                DriverResults = NetworkUtils.sentDriverNotificationFromHttpUrl(searchUrl,OrderId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return DriverResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String DriverResults) {

            if (DriverResults != null && !DriverResults.equals("")) {

                //  mSearchResultsTextView.setText(allNames.get(8));
            }else{
                Toast.makeText(getApplicationContext(), " Net connection error", Toast.LENGTH_SHORT).show();
            }
        }
    }





}
