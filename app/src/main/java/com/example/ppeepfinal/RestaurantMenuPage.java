package com.example.ppeepfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
/*import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
/*import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;*/
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ppeepfinal.adapter.ExpandableListAdapter;
import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.utilities.ImageCircleOfPicasso;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.example.ppeepfinal.utilities.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantMenuPage extends AppCompatActivity {
    private int mNotificationsCount = 0;
    Toolbar foodToolbar;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild,listDataChildPrice;
    HashMap<String,List<Integer>>listDataChildId;
    TextView mRestaurantName,mCusine;
    ImageView mRestaurnatLogo;
     LinearLayout mRestaurantBanner;
    String merchantId;
    URL restaurantMenuListUrl;
    List<String> Menus;
    ProgressBar mProgressbar;
    private UserDatabase mdb;
    int vat,deliveryCharge;
    private Menu mMenu = null;
    private static RestaurantMenuPage mThis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_page);
        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);
        mThis = this;
       // new FetchCountTask().execute();
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        mRestaurantName = (TextView) findViewById(R.id.tv_restaurnat_name) ;
        mCusine = (TextView) findViewById(R.id.tv_cusine);
        mProgressbar = (ProgressBar)findViewById(R.id.pv_restaurant_menu_item);
        mRestaurnatLogo = (ImageView) findViewById(R.id.restaurant_logo) ;
        mRestaurantBanner = (LinearLayout) findViewById(R.id.restaurant_banner);

        Intent intent = getIntent();
        merchantId = intent.getStringExtra("mercahnt_Id");
        String restaurantName = intent.getStringExtra("restaurant_name");
        String cusine = intent.getStringExtra("cuisine");
        String logo = intent.getStringExtra("logo");
        String banner = intent.getStringExtra("cover_image");
        vat = Integer.valueOf(intent.getStringExtra("vat"));
        deliveryCharge = Integer.valueOf( intent.getStringExtra("deliveryCharge"));


        mdb = UserDatabase.getInstance(getApplicationContext());// create db instance


        if(restaurantName != null){
            mRestaurantName.setText(restaurantName);
        }
        if(logo != null){
            URL getimageUrl = NetworkUtils.buildLoadIamgeUrl(NetworkUtils.LOGO_URL+logo);
            Picasso.get().load(getimageUrl.toString()).transform(new ImageCircleOfPicasso()).into(mRestaurnatLogo);
        }
        if(banner != null){
            URL getimageUrl = NetworkUtils.buildLoadIamgeUrl(NetworkUtils.LOGO_URL+banner);
            Picasso.get().load(getimageUrl.toString()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mRestaurantBanner.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
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

                    listAdapter = new ExpandableListAdapter(
                            RestaurantMenuPage.this,
                            listDataHeader,
                            listDataChild,
                            listDataChildPrice,
                            listDataChildId,
                            Integer.valueOf(merchantId),
                            vat,deliveryCharge,
                            mRestaurantName.getText().toString()
                           );// setting list adapter
                    expListView.setAdapter(listAdapter);

                    View parentLayout = findViewById(R.id.lvExp);
                    List<OrderModel> order =  mdb.orderDAO().loadOrder();
                    int count = order.size();
                    updateNotificationsBadge(count);

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
        MenuItem  foodCart= (MenuItem) menu.findItem(R.id.action_drawer_cart);

        LayerDrawable icon = (LayerDrawable) foodCart.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);


        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        List<OrderModel> order =  mdb.orderDAO().loadOrder();
        int count = order.size();
        updateNotificationsBadge(count);
        if(order.size() == 0){
            new RestaurantMenuListTask().execute(restaurantMenuListUrl);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        mMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    public Menu getMenu()
    {
        return mMenu;
    }


    public static RestaurantMenuPage getThis()
    {
        return mThis;
    }




}


