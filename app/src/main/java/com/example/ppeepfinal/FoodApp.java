package com.example.ppeepfinal;

import android.content.Intent;
/*import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;*/
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.github.florent37.bubbletab.BubbleTab;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_app);
        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);
/*
        getSupportFragmentManager().beginTransaction().replace(R.id.food_app_layout, new CuisineSearchFragment()).addToBackStack(null).commit();
*/


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
       // restaurantSearchView = (SearchView)  findViewById(R.id.sv_for_restaurant);
        mAddress = (TextView) findViewById(R.id.tv_delivery_address);
        addresslayout = (LinearLayout) findViewById(R.id.layout_address);

        addresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryMap = new Intent(FoodApp.this,UserAutoCompleteAdress.class);
                deliveryMap.putExtra("activity","food");
                startActivity(deliveryMap);
            }
        });

        mdb = UserDatabase.getInstance(getApplicationContext());

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





        adapter.AddFragment(new TabFragmentRecommended(),"Home");
        adapter.AddFragment(new TabFragmentNearby(),"Nearby");
        //  adapter.AddFragment(new FragmentNotification() ,"Promotion");
        adapter.AddFragment(new TabFragmentPopular(),"Popular");
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
}



