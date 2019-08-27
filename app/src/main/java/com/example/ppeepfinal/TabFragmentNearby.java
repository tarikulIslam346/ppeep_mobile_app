package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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

public class TabFragmentNearby extends Fragment {
    View v;
    //private  static final int NUM_LIST_ITEM = 100;
    private TabFragmentNearbyAdapter tabFragmentNearbyAdapter;
    private RecyclerView mNumberOfRestaurant;
    ProgressBar mProgressbar;
    List<UserModel> user;
    private UserDatabase mdb;
    String lat,lng;
    //ViewPager viewPager;


    public TabFragmentNearby(){ }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v=inflater.inflate(R.layout.activity_tab_fragment_nearby,container,false);
        //viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerId) ;
        URL restaurantListUrl = NetworkUtils.buildNearByRestaurantUrl();
        mProgressbar = (ProgressBar) v.findViewById(R.id.pv_restaurant_menu) ;
        mProgressbar.setVisibility(View.VISIBLE);

        mdb = UserDatabase.getInstance(getContext());

        user = mdb.userDAO().loadPhone();

        mNumberOfRestaurant = (RecyclerView)v.findViewById(R.id.rv_numbers);

        if(user.size() !=0){
           // Toast.makeText(getContext()," " + String.valueOf(user.get(0).getLat()), Toast.LENGTH_LONG).show();
            lat  = String.valueOf(user.get(0).getLat());
            lng = String.valueOf(user.get(0).getLng());
            new RestaurantListTask().execute(restaurantListUrl);

        }


        return v;

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
                RestaurantResults = NetworkUtils.getNearByRestaurantFromHttpUrl(searchUrl,lat,lng);
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

                String name,closingTime,openingTime,cusine, message = null,logo,banner;
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
                try {
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

                if(message != null ){
                    Snackbar.make(v.findViewById(R.id.layout_nearby_tab), " "+message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }

                if(jsonArray!= null){
                    LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
                    mNumberOfRestaurant.setLayoutManager(layoutManager);

                    mNumberOfRestaurant.setHasFixedSize(true);

                    tabFragmentNearbyAdapter = new TabFragmentNearbyAdapter(allNames,OpeningTimes,ClosingTimes,Cusines,logos,banners,  this);

                    mNumberOfRestaurant.setAdapter(tabFragmentNearbyAdapter);
                }








            }else{
                Toast.makeText(getContext(), "No restaurant found or network not available", Toast.LENGTH_LONG).show();
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
           Intent foodmenuIntent = new Intent(getContext(),RestaurantMenuPage.class);
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
