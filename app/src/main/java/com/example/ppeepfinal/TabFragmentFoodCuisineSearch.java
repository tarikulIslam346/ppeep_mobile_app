package com.example.ppeepfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

public class TabFragmentFoodCuisineSearch  extends Fragment {

    private static final String TAG = "Cuisine Search";
    private UserDatabase mdb;
    List<UserModel> user;
    String lat,lng,search;
    View view;
    private TabFragmentNearbyAdapter tabFragmentNearbyAdapter;
    private RecyclerView mListOfRestaurant;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater
                .inflate(R.layout.tab_fragment_food_cuisine_search, container, false);
        mdb = UserDatabase.getInstance(getContext());
        TextView cuisineText=(TextView) view.findViewById(R.id.cuiseIdSearch);
        cuisineText.setVisibility(View.INVISIBLE);



        Bundle bundle = this.getArguments();

        if(bundle != null){
            search = bundle.getString("search"); // Key, default value

            cuisineText.setText(search);
            cuisineText.setVisibility(View.VISIBLE);
           // Toast.makeText(getContext(),""+txt,Toast.LENGTH_LONG).show();
        }
       // Toast.makeText(getContext(),""+search,Toast.LENGTH_LONG).show();

        user = mdb.userDAO().loadPhone();
        mListOfRestaurant = (RecyclerView) view.findViewById(R.id.rv_search_restaurant);
        if(user.size()!=0) {
            //mAddress.setText(user.get(0).getAddress());
            ShowLoder("Loding...");
            lat = String.valueOf(user.get(0).getLat());
            lng = String.valueOf(user.get(0).getLng());
            URL restaurantSearchListUrl = NetworkUtils.buildUrl(NetworkUtils.RESTAURANT_CUISINE_SEARCH_URL);
            new RestaurantListTask().execute(restaurantSearchListUrl);
            //Toast.makeText(getApplicationContext(),""+lat,Toast.LENGTH_LONG).show();
        }



        return view;
    }
    public void ShowLoder(String message){
       dialog = ProgressDialog.show(getContext(), "",
                message, true);
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
                RestaurantResults = NetworkUtils.getRestaurantSearchFromHttpUrl(searchUrl,"chin",lat,lng);
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

                dialog.dismiss();

                if(message==null){

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    mListOfRestaurant.setLayoutManager(layoutManager);

                    mListOfRestaurant.setHasFixedSize(true);

                    tabFragmentNearbyAdapter = new TabFragmentNearbyAdapter(allNames,OpeningTimes,ClosingTimes,Cusines,logos,banners,  this);

                    mListOfRestaurant.setAdapter(tabFragmentNearbyAdapter);
                }else{

                    Snackbar snackbar = Snackbar.make(view.findViewById(R.id.layout_snackbar), " "+message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            /* Fix it
                             * Change Action text color
                             * setActionTextColor(Color.RED)
                             * */
                            .setActionTextColor(ContextCompat.getColor(getContext(), R.color.yellow));

                    View sbView = snackbar.getView();

                    /* Fix it
                     * Change  text coler
                     * */
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(android.R.color.black ));

                    /* Fix it
                     * Change background  color
                     * */
                    sbView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    snackbar.show();
                }





            }else{
                dialog.dismiss();
                Toast.makeText(getContext(), "No restaurant found or network not available", Toast.LENGTH_SHORT).show();
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

