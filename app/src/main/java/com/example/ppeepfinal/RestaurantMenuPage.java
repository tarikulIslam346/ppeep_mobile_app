package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppeepfinal.data.OrderMercahntDAO;
import com.example.ppeepfinal.data.OrderMerchantModel;
import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RestaurantMenuPage extends AppCompatActivity {

    Toolbar foodToolbar;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild,listDataChildPrice;
    HashMap<String,List<Integer>>listDataChildId;
    TextView mRestaurantName,mCusine;
    String merchantId;
    URL restaurantMenuListUrl;
    List<String> Menus;
    ProgressBar mProgressbar;
    private UserDatabase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_page);
        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        mRestaurantName = (TextView) findViewById(R.id.tv_restaurnat_name) ;
        mCusine = (TextView) findViewById(R.id.tv_cusine);
        mProgressbar = (ProgressBar)findViewById(R.id.pv_restaurant_menu_item);

        Intent intent = getIntent();
        merchantId = intent.getStringExtra("mercahnt_Id");
        String restaurantName = intent.getStringExtra("restaurant_name");
        String cusine = intent.getStringExtra("cuisine");

        mdb = UserDatabase.getInstance(getApplicationContext());// create db instance

        if(restaurantName != null){
            mRestaurantName.setText(restaurantName);
        }
        if(cusine !=null){
            mCusine.setText(cusine);
        }
        //get ulr for menu item
        mProgressbar.setVisibility(View.VISIBLE);
        restaurantMenuListUrl = NetworkUtils.buildRestaurantMenuUrl();
        new RestaurantMenuListTask().execute(restaurantMenuListUrl);

    }


    public class RestaurantMenuListTask extends AsyncTask<URL, Void, String>  {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String RestaurantMenuResults = null;
            try {
                RestaurantMenuResults = NetworkUtils.getRestaurantMenuFromHttpUrl(searchUrl,merchantId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return RestaurantMenuResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String RestaurantMenuResults) {

            if (RestaurantMenuResults != null && !RestaurantMenuResults.equals("")) {

                 Menus = new ArrayList<String>();

                 listDataHeader = new ArrayList<String>();

                 listDataChild = new HashMap<String, List<String>>();

                 listDataChildId = new HashMap<String, List<Integer>>();

                 listDataChildPrice = new HashMap<String, List<String>>();


                String json = RestaurantMenuResults;

                JSONObject restaurantMenuList = null;

                JSONObject restaurantFoodMenuList = null;

                JSONArray jsonArray ,jsonFoodMenuArray;

                String menu,foodMenuItem;


                try {

                    restaurantMenuList = new JSONObject(json);

                    jsonArray = restaurantMenuList.getJSONArray("menu");

                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject restaurant = jsonArray.getJSONObject(i);

                        menu = restaurant.getString("category_name");

                        Menus.add(menu);
                    }


                    restaurantFoodMenuList = new JSONObject(json);

                    jsonFoodMenuArray = restaurantFoodMenuList.getJSONArray("menu_wise_food_item");

                    for (int k=0; k<jsonFoodMenuArray.length(); k++) {



                        JSONObject restaurantFoodMenu = jsonFoodMenuArray.getJSONObject(k);

                        foodMenuItem = Menus.get(k);

                        listDataHeader.add(foodMenuItem);


                        JSONArray foodMenu = restaurantFoodMenu.getJSONArray(foodMenuItem);
                        List<String> itemMenu = new ArrayList<String>();
                        List<Integer> itemMenuId = new ArrayList<Integer>();
                        List<String> itemMenuPrice = new ArrayList<String>();

                        for(int j=0;j<foodMenu.length();j++){

                            JSONObject restaurantFoodMenuItem = foodMenu.getJSONObject(j);

                            String itemName = restaurantFoodMenuItem.getString("item_name");

                            String itemPrice = restaurantFoodMenuItem.getString("price");

                            Integer ItemId = restaurantFoodMenuItem.getInt("item_id");

                            itemMenuId.add(ItemId);

                            itemMenuPrice.add(itemPrice);

                            itemMenu.add(itemName);

                        }

                        listDataChild.put(foodMenuItem, itemMenu);
                        listDataChildId.put(foodMenuItem, itemMenuId);
                        listDataChildPrice.put(foodMenuItem, itemMenuPrice);
                    }
                    mProgressbar.setVisibility(View.INVISIBLE);// set progressbar to invisible
                    ViewGroup.LayoutParams layoutParams = mProgressbar.getLayoutParams();// instantiate  layout parameter progressbar
                    layoutParams.height = 0;// set layout height to zero
                    layoutParams.width = 0;// set layout width to zero
                    mProgressbar.setLayoutParams(layoutParams);// set progressbar layout height & width
                    listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild,listDataChildPrice,listDataChildId);// setting list adapter
                    expListView.setAdapter(listAdapter);
                    // Listview Group click listener
                   /* expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int groupPosition, long id) {
                            // Toast.makeText(getApplicationContext(),
                            // "Group Clicked " + listDataHeader.get(groupPosition),
                            // Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                    // Listview Group expanded listener
                    expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                        @Override
                        public void onGroupExpand(int groupPosition) {
                            Toast.makeText(getApplicationContext(),
                                    listDataHeader.get(groupPosition) + " Expanded",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Listview Group collasped listener
                    expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                        @Override
                        public void onGroupCollapse(int groupPosition) {
                            Toast.makeText(getApplicationContext(),
                                    listDataHeader.get(groupPosition) + " Collapsed",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });*/

                    // Listview on child click listener
                    expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v,
                                                    int groupPosition, int childPosition, long id) {

                            List<OrderMerchantModel> orderMerchantModelList =  mdb.orderMercahntDAO().loadOrderMerchant();
                            //add order item if order from same marchant or no add to cart select
                            if(orderMerchantModelList.size() ==0  || Integer.valueOf(merchantId)== orderMerchantModelList.get(0).getMerchantId() ){
                                //Add to merchant list if no order merchant select
                                if(orderMerchantModelList.size() == 0 ){
                                    OrderMerchantModel orderMerchantModel = new OrderMerchantModel(Integer.valueOf(merchantId));
                                    mdb.orderMercahntDAO().insertOrderMerchant(orderMerchantModel);
                                }


                                // Add to phone storage order item
                                Date date = new Date();
                                int ItemId = listDataChildId.get(listDataHeader.get(groupPosition)).get(childPosition);
                                int ItemPrice = Integer.valueOf(listDataChildPrice.get(listDataHeader.get(groupPosition)).get(childPosition));
                                String ItemName =  listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                                OrderModel orderModel = new OrderModel(ItemId,ItemName,ItemPrice,date);
                                mdb.orderDAO().insertOrder(orderModel);
                                //finish();

                            }else {
                                Toast.makeText(getApplicationContext(), "You can only order from same restaurant at a time", Toast.LENGTH_LONG)
                                        .show();
                            }



                            /*Toast.makeText(getApplicationContext(), listDataChildId.get(groupPosition) + " : " +
                                    listDataChildId.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
                                    .show();*/
                            return false;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  mSearchResultsTextView.setText(allNames.get(8));
            }else{
                Toast.makeText(getApplicationContext(), "No restaurant menu found or net connection error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foodappcart, menu);
        //  foodCart= (Menu) menu.findItem(R.id.action_drawer_cart);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_drawer_cart:
                Intent intent = new Intent(this, FoodCartPage.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}


