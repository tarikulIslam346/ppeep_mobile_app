package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ppeepfinal.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TabFragmentNearby extends Fragment  {
    View v;
    //private  static final int NUM_LIST_ITEM = 100;
    private TabFragmentNearbyAdapter tabFragmentNearbyAdapter;
    private RecyclerView mNumberOfRestaurant;
    List<String> allNames = new ArrayList<String>();
    ProgressBar mProgressbar;


    public TabFragmentNearby(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v=inflater.inflate(R.layout.activity_tab_fragment_nearby,container,false);
        URL restaurantListUrl = NetworkUtils.buildRestaurantUrl();
        mProgressbar = (ProgressBar) v.findViewById(R.id.pv_restaurant_menu) ;
        mProgressbar.setVisibility(View.VISIBLE);
        new RestaurantListTask().execute(restaurantListUrl);
        mNumberOfRestaurant = (RecyclerView)v.findViewById(R.id.rv_numbers);
        return v;

    }


    public class RestaurantListTask extends AsyncTask<URL, Void, String> implements   TabFragmentNearbyAdapter.ListItemClickListener {

        List<String> allNames = new ArrayList<String>();
        List<String> OpeningTimes = new ArrayList<String>();
        List<String> ClosingTimes = new ArrayList<String>();
        List<String> Cusines = new ArrayList<String>();
        List<Integer> MerchantId = new ArrayList<Integer>();

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String RestaurantResults = null;
            try {
                RestaurantResults = NetworkUtils.getRestaurantFromHttpUrl(searchUrl);
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

                String name,closingTime,openingTime,cusine;
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

                        allNames.add(name);
                        OpeningTimes.add(openingTime);
                        ClosingTimes.add(closingTime);
                        Cusines.add(cusine);
                        MerchantId.add(merchantId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressbar.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams layoutParams = mProgressbar.getLayoutParams();

                layoutParams.height = 0;
                layoutParams.width = 0;
                mProgressbar.setLayoutParams(layoutParams);

                LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
                mNumberOfRestaurant.setLayoutManager(layoutManager);

                mNumberOfRestaurant.setHasFixedSize(true);

                tabFragmentNearbyAdapter = new TabFragmentNearbyAdapter(allNames,OpeningTimes,ClosingTimes,Cusines,  this);

                mNumberOfRestaurant.setAdapter(tabFragmentNearbyAdapter);


            }else{
                Toast.makeText(getContext(), "No restaurant found or network not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            int clickedRestaurnat = MerchantId.get(clickedItemIndex).intValue();
            String restaurantName = allNames.get(clickedItemIndex);
            String cusine = Cusines.get(clickedItemIndex);
            //Toast.makeText(getContext(),"restaurant id" +clickedRestaurnat ,Toast.LENGTH_SHORT).show();
           Intent foodmenuIntent = new Intent(getContext(),RestaurantMenuPage.class);
           foodmenuIntent.putExtra("mercahnt_Id",String.valueOf(clickedRestaurnat));
            foodmenuIntent.putExtra("restaurant_name",restaurantName);
            foodmenuIntent.putExtra("cuisine",cusine);
           startActivity(foodmenuIntent);



        }
    }
}
