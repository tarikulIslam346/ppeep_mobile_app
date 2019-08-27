package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
/*import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;*/
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.adapter.TabFragmentNearbyAdapter;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchRestaurant extends AppCompatActivity {
    private TabFragmentNearbyAdapter tabFragmentNearbyAdapter;
    private RecyclerView mListOfRestaurant;
    ProgressBar mProgressbar;
    String search;
    TextView searchPageResult;
    Toolbar foodToolbar;
    private UserDatabase mdb;
    TextView mAddress;
    LinearLayout addresslayout;
    //android.widget.SearchView restaurantSearchView;
    String address, lat,lng;
    List<UserModel> user;
   SearchView restaurantSearchView;
    String searchText = null;
    LinearLayout mAddressChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);
        foodToolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(foodToolbar);
        searchPageResult = (TextView) findViewById(R.id.tv_search_result_for);
        mAddress = (TextView) findViewById(R.id.tv_delivery_address);

        restaurantSearchView = (SearchView) findViewById(R.id.searchItems);
        mProgressbar = (ProgressBar) findViewById(R.id.pv_restaurant_list_search) ;

        mAddressChange = (LinearLayout) findViewById(R.id.layout_map_address);
        searchPageResult.setVisibility(View.INVISIBLE);
        mAddressChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getApplicationContext(),UserAutoCompleteAdress.class);
                mapIntent.putExtra("activity","search");

                startActivity(mapIntent);
            }
        });


        mListOfRestaurant = (RecyclerView) findViewById(R.id.rv_search_restaurant);
        URL restaurantSearchListUrl = NetworkUtils.buildSearchRestaurantUrl();

        mdb = UserDatabase.getInstance(getApplicationContext());

        user = mdb.userDAO().loadPhone();

        Intent intentAddressData = getIntent();

        address = intentAddressData.getStringExtra("address");
        lat = intentAddressData.getStringExtra("lat");
        lng = intentAddressData.getStringExtra("lng");

        Intent intent = getIntent();
        search = intent.getStringExtra("search");


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
            if(user.size()!=0) {
                mAddress.setText(user.get(0).getAddress());
                lat = String.valueOf(user.get(0).getLat());
                lng = String.valueOf(user.get(0).getLng());
               //Toast.makeText(getApplicationContext(),""+lat,Toast.LENGTH_LONG).show();
            }
        }

        if( search != null){
            searchText = search;
            mProgressbar.setVisibility(View.VISIBLE);
            new SearchRestaurant.RestaurantListTask().execute(restaurantSearchListUrl);
        }



        restaurantSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                searchText = newText;
                //startActivity(intent);
                if(searchText.length()>2) {
                   // Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_LONG).show();


                    mProgressbar.setVisibility(View.VISIBLE);
                    new SearchRestaurant.RestaurantListTask().execute(restaurantSearchListUrl);
                }


                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchText = query;

                if(searchText.length()>2){
                    mProgressbar.setVisibility(View.VISIBLE);
                    new SearchRestaurant.RestaurantListTask().execute(restaurantSearchListUrl);
                }
                return false;
            }

        });

       // if(searchText !=null){

        //}






    }
    public class RestaurantListTask extends AsyncTask<URL, Void, String> implements   TabFragmentNearbyAdapter.ListItemClickListener {

        List<String> allNames = new ArrayList<String>();
        List<String> logos = new ArrayList<String>();
        List<String> banners = new ArrayList<String>();
        List<String> OpeningTimes = new ArrayList<String>();
        List<String> ClosingTimes = new ArrayList<String>();
        List<String> Cusines = new ArrayList<String>();
        List<Integer> MerchantId = new ArrayList<Integer>();
        List<Integer> VatList = new ArrayList<Integer>();
        List<Integer> deliveryChargList = new ArrayList<Integer>();

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String RestaurantResults = null;
            try {
                RestaurantResults = NetworkUtils.getRestaurantSearchFromHttpUrl(searchUrl,searchText,lat,lng);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return RestaurantResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String RestaurantResults) {
            if (RestaurantResults != null && !RestaurantResults.equals("")) {



                String json = RestaurantResults;
                JSONObject restaurantList = null;
                JSONArray jsonArray=null;

                String name,closingTime,openingTime,cusine,message = null,logo,banner;
                int vatOfRestaurant,deliverChargeOfRestaurant;
                int merchantId;


                try {
                    restaurantList = new JSONObject(json);
                    jsonArray = restaurantList.getJSONArray("restaurnat_list");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject restaurant = jsonArray.getJSONObject(i);
                        name = restaurant.getString("restaurant_name");
                        openingTime = restaurant.getString("opening");
                        closingTime = restaurant.getString("closing");
                        cusine = restaurant.getString("cuisine");
                        merchantId = restaurant.getInt("merchant_id");
                        vatOfRestaurant = restaurant.getInt("vat");
                        deliverChargeOfRestaurant = restaurant.getInt("delivery_charges");
                        logo = restaurant.getString("logo");
                        banner = restaurant.getString("cover_image");

                        allNames.add(name);
                        logos.add(logo);
                        banners.add(banner);
                        OpeningTimes.add(openingTime);
                        ClosingTimes.add(closingTime);
                        Cusines.add(cusine);
                        MerchantId.add(merchantId);
                        VatList.add(vatOfRestaurant);
                        deliveryChargList.add(deliverChargeOfRestaurant);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{
                    restaurantList = new JSONObject(json);
                    message = restaurantList.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressbar.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams layoutParams = mProgressbar.getLayoutParams();

                layoutParams.height = 0;
                layoutParams.width = 0;
                mProgressbar.setLayoutParams(layoutParams);
                if(message==null){
                    searchPageResult.setVisibility(View.VISIBLE);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    mListOfRestaurant.setLayoutManager(layoutManager);

                    mListOfRestaurant.setHasFixedSize(true);

                    tabFragmentNearbyAdapter = new TabFragmentNearbyAdapter(allNames,OpeningTimes,ClosingTimes,Cusines,logos,banners,  this);

                    mListOfRestaurant.setAdapter(tabFragmentNearbyAdapter);
                }else{
                    Snackbar.make(findViewById(R.id.layout_snackbar), " "+message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }





            }else{
                Toast.makeText(getApplicationContext(), "No restaurant found or network not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            int clickedRestaurnat = MerchantId.get(clickedItemIndex).intValue();
            String restaurantName = allNames.get(clickedItemIndex);
            String cusine = Cusines.get(clickedItemIndex);
            String logo = logos.get(clickedItemIndex);
            String banner = banners.get(clickedItemIndex);
            int vat = VatList.get(clickedItemIndex);
            int deliveryCharge = deliveryChargList.get(clickedItemIndex);
            //Toast.makeText(getContext(),"restaurant id" +clickedRestaurnat ,Toast.LENGTH_SHORT).show();
            Intent foodmenuIntent = new Intent(getApplicationContext(),RestaurantMenuPage.class);
            foodmenuIntent.putExtra("mercahnt_Id",String.valueOf(clickedRestaurnat));
            foodmenuIntent.putExtra("restaurant_name",restaurantName);
            foodmenuIntent.putExtra("cuisine",cusine);
            foodmenuIntent.putExtra("vat",String.valueOf(vat));
            foodmenuIntent.putExtra("deliveryCharge",String.valueOf(deliveryCharge));
            foodmenuIntent.putExtra("logo",logo);
            foodmenuIntent.putExtra("cover_image",banner);
            startActivity(foodmenuIntent);



        }
    }
}
