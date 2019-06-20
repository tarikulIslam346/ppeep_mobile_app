package com.example.ppeepfinal;

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
import android.widget.Toast;

import com.example.ppeepfinal.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TabFragmentNearby extends Fragment {
    View v;
    private  static final int NUM_LIST_ITEM = 100;
    private TabFragmentNearbyAdapter tabFragmentNearbyAdapter;
    private RecyclerView mNumberOfRestaurant;
    List<String> allNames = new ArrayList<String>();


    public TabFragmentNearby(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v=inflater.inflate(R.layout.activity_tab_fragment_nearby,container,false);
        URL restaurantListUrl = NetworkUtils.buildRestaurantUrl();
        new RestaurantListTask().execute(restaurantListUrl);

            mNumberOfRestaurant = (RecyclerView)v.findViewById(R.id.rv_numbers);




        return v;

    }

    public class RestaurantListTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
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

                List<String> allNames = new ArrayList<String>();
                List<String> OpeningTimes = new ArrayList<String>();

                String json = RestaurantResults;
                JSONObject restaurantList = null;
                JSONArray jsonArray=null;
                String name=null;
                String openingTime;

                try {
                    restaurantList = new JSONObject(json);
                    jsonArray = restaurantList.getJSONArray("restaurnat_list");
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject restaurant = jsonArray.getJSONObject(i);
                        name = restaurant.getString("restaurant_name");
                        openingTime = restaurant.getString("opening");
                        allNames.add(name);
                        OpeningTimes.add(openingTime);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
                mNumberOfRestaurant.setLayoutManager(layoutManager);

                mNumberOfRestaurant.setHasFixedSize(true);

                tabFragmentNearbyAdapter = new TabFragmentNearbyAdapter(allNames,OpeningTimes);
                mNumberOfRestaurant.setAdapter(tabFragmentNearbyAdapter);
              //  mSearchResultsTextView.setText(allNames.get(8));

            }else{
                Toast.makeText(getContext(), "No restaurant", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
