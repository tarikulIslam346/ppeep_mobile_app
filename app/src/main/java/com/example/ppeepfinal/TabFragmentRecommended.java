package com.example.ppeepfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;*/
import android.os.Bundle;
/*import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ppeepfinal.utilities.NetworkUtils;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TabFragmentRecommended extends Fragment {

    View v, nextV;

    SliderLayout sliderLayout;

    TextView mViewAll;

    ViewPager viewPager;

    CardView cardViewForSearchChiniseCusine,cardViewForSearchCFastFoodCusine,cardViewForSearchBanglaCusine,cardViewForBakeryCusine;

    List<String>imgUrl;

   // ProgressBar progressBar;

    ProgressDialog dialog;

    private RecyclerView mNumberOfRestaurant;

    private RecommandedRestaurantListAdapter recommandedRestaurantListAdapter;


    public TabFragmentRecommended(){ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.activity_tab_fragment_recommended,container,false);

        mViewAll = (TextView) v.findViewById(R.id.tv_view_all);

        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerId) ;

        cardViewForSearchChiniseCusine = (CardView) v.findViewById(R.id.cv_chinise);

        cardViewForBakeryCusine = (CardView) v.findViewById(R.id.cv_bakery);

        cardViewForSearchBanglaCusine = (CardView) v.findViewById(R.id.cv_bangla);

        cardViewForSearchCFastFoodCusine = (CardView) v.findViewById(R.id.cv_fastfood);

       // progressBar = (ProgressBar) v.findViewById(R.id.pv_offerr);

        mNumberOfRestaurant = (RecyclerView)v.findViewById(R.id.rv_numbers);




        mViewAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
               viewPager.setCurrentItem(1);
            }
        });

        cardViewForSearchChiniseCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),SearchRestaurant.class);
                intent.putExtra("search","Chinese");
                startActivity(intent);
            }
        });

        cardViewForBakeryCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),SearchRestaurant.class);
                intent.putExtra("search","Bakery");
                startActivity(intent);
            }
        });

        cardViewForSearchBanglaCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),SearchRestaurant.class);
                intent.putExtra("search","Bangla");
                startActivity(intent);
            }
        });

        cardViewForSearchCFastFoodCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),SearchRestaurant.class);
                intent.putExtra("search","Fastfood");
                startActivity(intent);
            }
        });





      // progressBar.setVisibility(View.VISIBLE);

        ShowLoder("Loading .. ..");

        URL offerListUrl = NetworkUtils.buildOfferUrl();
        new RestaurantListOfferTask().execute(offerListUrl);

        URL restaurantUrl = NetworkUtils.buildRecommendedRestaurantUrl();
        new RestaurantListTask().execute(restaurantUrl);


        return v;
    }

    public void ShowLoder(String message){
        dialog = ProgressDialog.show(getContext(), "",
                message, true);
    }

    private void setSliderViews() {

        for (int i = 0; i <= 2; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            switch (i) {
                case 0:
                    String URL = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(0);
                      sliderView.setImageUrl(URL);
                    break;
                case 1:
                    String URL1 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(1);
                    sliderView.setImageUrl(URL1);

                    break;
                case 2:

                    String URL2 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(2);
                    sliderView.setImageUrl(URL2);
                    break;

            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //  sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }


    }

    public class RestaurantListOfferTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String offerResults = null;
            try {
                offerResults = NetworkUtils.getOfferFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return offerResults;
        }

        @Override
        protected void onPostExecute(String RestaurantResults) {

            if (RestaurantResults != null && !RestaurantResults.equals("")) {

                String json = RestaurantResults;

                JSONObject restaurantList = null;

                JSONArray jsonArray=null;

                String imageUrl;

                imgUrl = new ArrayList<String>();

                try {
                    restaurantList = new JSONObject(json);

                    jsonArray = restaurantList.getJSONArray("offer_list");

                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject restaurant = jsonArray.getJSONObject(i);

                        imageUrl = restaurant.getString("img_url");

                        imgUrl.add(imageUrl);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               // dialog.dismiss();

                sliderLayout = v.findViewById(R.id.FoodAppimageSlider);

                sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

                setSliderViews();


            }else{
                Toast.makeText(getContext(), "No restaurant found or network not available", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public class RestaurantListTask extends AsyncTask<URL, Void, String> implements   RecommandedRestaurantListAdapter.ListItemClickListener {

        List<String> allNames = new ArrayList<String>();
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
                RestaurantResults = NetworkUtils.getRecommendedRestaurantFromHttpUrl(searchUrl);
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

                        allNames.add(name);
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

                dialog.dismiss();

                LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext(),LinearLayoutManager.HORIZONTAL,true);
                mNumberOfRestaurant.setLayoutManager(layoutManager);

                mNumberOfRestaurant.setHasFixedSize(true);

                recommandedRestaurantListAdapter = new RecommandedRestaurantListAdapter(allNames,OpeningTimes,ClosingTimes,Cusines,  this);

                mNumberOfRestaurant.setAdapter(recommandedRestaurantListAdapter);


            }else{
                Toast.makeText(getContext(), "No restaurant found or network not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            int clickedRestaurnat = MerchantId.get(clickedItemIndex).intValue();
            String restaurantName = allNames.get(clickedItemIndex);
            String cusine = Cusines.get(clickedItemIndex);
            int vat = VatList.get(clickedItemIndex);
            int deliveryCharge = deliveryChargList.get(clickedItemIndex);
            //Toast.makeText(getContext(),"restaurant id" +clickedRestaurnat ,Toast.LENGTH_SHORT).show();
            Intent foodmenuIntent = new Intent(getContext(),RestaurantMenuPage.class);
            foodmenuIntent.putExtra("mercahnt_Id",String.valueOf(clickedRestaurnat));
            foodmenuIntent.putExtra("restaurant_name",restaurantName);
            foodmenuIntent.putExtra("cuisine",cusine);
            foodmenuIntent.putExtra("vat",String.valueOf(vat));
            foodmenuIntent.putExtra("deliveryCharge",String.valueOf(deliveryCharge));
            startActivity(foodmenuIntent);



        }
    }









    }

