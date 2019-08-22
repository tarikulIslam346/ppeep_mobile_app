package com.example.ppeepfinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
/*import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;*/
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.ppeepfinal.data.UserCurrentOrder;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.github.florent37.bubbletab.BubbleTab;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FoodApp extends AppCompatActivity {
Toolbar foodToolbar;
Menu foodCart;
  private BubbleTab tabLayoutId;
    private ViewPager viewPagerId;
    BubbleTab bubbleTab;
    private ViewPagerAdapter adapter;
    SearchView restaurantSearchView;
    private UserDatabase mdb;
    TextView mAddress;
    LinearLayout addresslayout;
    String address, lat,lng;
    List<UserModel> user;
    MaterialCardView bottomSliderCardOrderPlace,bottomSliderCardOrderConfirm,bottomSliderCardOrderDelivr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_app);
        mdb = UserDatabase.getInstance(getApplicationContext());
        foodToolbar = (Toolbar) findViewById(R.id.foodapptoolbar);
        setSupportActionBar(foodToolbar);
/*
        getSupportFragmentManager().beginTransaction().replace(R.id.food_app_layout, new CuisineSearchFragment()).addToBackStack(null).commit();
*/


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
       // restaurantSearchView = (SearchView)  findViewById(R.id.sv_for_restaurant);
        mAddress = (TextView) findViewById(R.id.tv_delivery_address);
        addresslayout = (LinearLayout) findViewById(R.id.layout_address);
        bottomSliderCardOrderPlace = (MaterialCardView)findViewById(R.id.bottomSlider_order_place);
        bottomSliderCardOrderPlace.setVisibility(View.INVISIBLE);
        bottomSliderCardOrderConfirm = (MaterialCardView)findViewById(R.id.bottom_slider_order_confirm);
        bottomSliderCardOrderConfirm.setVisibility(View.INVISIBLE);
        bottomSliderCardOrderDelivr = (MaterialCardView)findViewById(R.id.bottom_slider_order_deliver);
        bottomSliderCardOrderDelivr.setVisibility(View.INVISIBLE);


        loadOrderInfo();

        addresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryMap = new Intent(FoodApp.this,UserAutoCompleteAdress.class);
                deliveryMap.putExtra("activity","food");
                startActivity(deliveryMap);
            }
        });



        user = mdb.userDAO().loadPhone();

        Intent intentAddressData = getIntent();


        address = intentAddressData.getStringExtra("address");
        lat = intentAddressData.getStringExtra("lat");
        lng = intentAddressData.getStringExtra("lng");


        if(address != null &&  lat !=null && lng != null){
            int Id = user.get(0).getId();
            UserModel updateUser = mdb.userDAO().loadUserById(Id);
            updateUser.setLat(Double.valueOf(lat));
            updateUser.setLng(Double.valueOf(lng));
            updateUser.setAddress(address);
            mdb.userDAO().updateUser(updateUser);
            user = mdb.userDAO().loadPhone();
            mAddress.setText(user.get(0).getAddress());
        } else{
            user = mdb.userDAO().loadPhone();
            if(user.size()!=0) mAddress.setText(user.get(0).getAddress());
        }


        Intent OrderConfirmIntent = getIntent();
        String orderId = OrderConfirmIntent.getStringExtra("order_id");
        if(orderId != null){
           // OrderConfirmModel orderConfirmModel = new OrderConfirmModel(Integer.valueOf(orderId),1);
           // mdb.orderConfirmDAO().insertOrderConfirm(orderConfirmModel);
            Snackbar.make(findViewById(R.id.food_app_layout), " 1 order has been placed", Snackbar.LENGTH_INDEFINITE)
                    .setAction("See Details", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(FoodApp.this,CurrentOrderHistory.class);
                            intent.putExtra("order_id",orderId);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

           /* List<OrderConfirmModel> orderConfirmModel = mdb.orderConfirmDAO().loadOrderConfirm();

            if(orderConfirmModel.size() !=0 ){
                Snackbar.make(findViewById(R.id.food_app_layout), " 1 order has been placed", Snackbar.LENGTH_INDEFINITE)
                        .setAction("See Details", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(FoodApp.this,CurrentOrderHistory.class);
                                int order_id = orderConfirmModel.get(0).getOrderId();
                                intent.putExtra("order_id",String.valueOf(order_id));
                                startActivity(intent);
                            }
                        })
                        .show();
            }*/



       /* restaurantSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(getApplicationContext(),SearchRestaurant.class);
                intent.putExtra("search",query);
                startActivity(intent);

                return false;
            }

        });*/


/*
 @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.foodappcart, menu);
            foodCart=menu.findItem(R.id.action_drawer_cart);
            return super.onCreateOptionsMenu(menu);
        }
*/

tabLayoutId = (BubbleTab) findViewById(R.id.tabLayoutId);


        viewPagerId = (ViewPager) findViewById(R.id.viewPagerId);
        tabLayoutId.setupWithViewPager(viewPagerId);





        adapter.AddFragment(new TabFragmentFoodHomeRoot(),"Home");
        adapter.AddFragment(new TabFragmentNearby(),"Nearby");
        //  adapter.AddFragment(new FragmentNotification() ,"Promotion");
        //adapter.AddFragment(new TabFragmentPopular(),"Popular");
        adapter.AddFragment(new TabFragmentOffer(),"Free Delivery");


        viewPagerId.setAdapter(adapter);


       /* tabLayoutId.getTabAt(0).setIcon(R.drawable.homeicon);
        tabLayoutId.getTabAt(1).setIcon(R.drawable.nearbyicon);
        tabLayoutId.getTabAt(2).setIcon(R.drawable.recommendedicon2);
        tabLayoutId.getTabAt(3).setIcon(R.drawable.freedeliveryicon);*/




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_menu_bar) {
            Intent intent = new Intent(getApplicationContext(),SearchRestaurant.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrderInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadOrderInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadOrderInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadOrderInfo();
    }

    private void addNotification(String text) {
        Intent it = new Intent(this, TabFragmentFoodHome.class);
        // Snackbar.make(R.id.layout_home_page),"Order_has",Snackbar.LENGTH_INDEFINITE).show();
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), it, 0);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int ico_notification = R.drawable.ic_account_circle_black_24dp;
        int color = ContextCompat.getColor(this, R.color.colorAccent);

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

    public  void loadOrderInfo(){
        List<UserCurrentOrder> userCurrentOrders = mdb.userCurrentOrderDAO().loadCurrentOrder();
        if(userCurrentOrders.size()!=0){
            int orderId = userCurrentOrders.get(0).getOrderid();
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
                            addNotification("Your order has been accepted");
                            UserCurrentOrder userCurrentOrder = new UserCurrentOrder(Integer.valueOf(orderId),1);
                            bottomSliderCardOrderPlace.setVisibility(View.VISIBLE);

                            // Toast.makeText(getApplicationContext()," Order confirm : ",Toast.LENGTH_LONG).show();


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

                            // if(OrderId != 0) {
                            // UserCurrentOrder userCurrentOrder = mdb.userCurrentOrderDAO().loadCurrentOrderById(OrderId);

                            //  userCurrentOrder.setOrderStatus(2);

                            // UserCurrentOrder userCurrentOrder = new UserCurrentOrder(Integer.valueOf(orderId),1);
                            //  mdb.userCurrentOrderDAO().updateCurrentOrder(userCurrentOrder);
                            bottomSliderCardOrderConfirm.setVisibility(View.VISIBLE);
                           //  }


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

                           // if(OrderId != 0) {
                               // UserCurrentOrder userCurrentOrder = mdb.userCurrentOrderDAO().loadCurrentOrderById(OrderId);

                                //  userCurrentOrder.setOrderStatus(2);

                                // UserCurrentOrder userCurrentOrder = new UserCurrentOrder(Integer.valueOf(orderId),1);
                               // mdb.userCurrentOrderDAO().deleteCurrentOrder(userCurrentOrder);

                            //}
                            bottomSliderCardOrderDelivr.setVisibility(View.VISIBLE);


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
    }
}



