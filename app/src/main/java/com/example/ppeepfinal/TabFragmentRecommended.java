package com.example.ppeepfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
    ProgressBar progressBar;



    public TabFragmentRecommended(){


    }

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
        progressBar = (ProgressBar) v.findViewById(R.id.pv_offerr);




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





       progressBar.setVisibility(View.VISIBLE);

        URL offerListUrl = NetworkUtils.buildOfferUrl();
        new RestaurantListOfferTask().execute(offerListUrl);


        return v;
    }









    private void setSliderViews() {

        for (int i = 0; i <= 2; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            switch (i) {
                case 0:
                    //sliderView.setImageDrawable(R.drawable.offerimageslider1);
                    String URL = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(0);
                      sliderView.setImageUrl(URL);
                    break;
                case 1:
                    //sliderView.setImageDrawable(R.drawable.offerimageslider2);
                    String URL1 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(1);
                    sliderView.setImageUrl(URL1);
                    // sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");

                    break;
                case 2:
                    //sliderView.setImageDrawable(R.drawable.offerimageslider4);

                    String URL2 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(2);
                    sliderView.setImageUrl(URL2);
                    //  sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
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

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
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
                        if(i==1)Toast.makeText(getContext(),""+imageUrl,Toast.LENGTH_LONG).show();

                        imgUrl.add(imageUrl);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();

                layoutParams.height = 0;
                layoutParams.width = 0;
                progressBar.setLayoutParams(layoutParams);


                sliderLayout = v.findViewById(R.id.FoodAppimageSlider);
                //    sliderLayout.setIndicatorAnimation(IndicatorAnimations.NONE); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

                setSliderViews();



            }else{
                Toast.makeText(getContext(), "No restaurant found or network not available", Toast.LENGTH_SHORT).show();
            }
        }


    }









    }

