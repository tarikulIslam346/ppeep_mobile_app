package com.example.ppeepfinal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.card.MaterialCardView;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class FragmentHome extends Fragment {

    SliderLayout sliderLayout;
    public static String FACEBOOK_URL = "https://www.facebook.com/YourPageName";
    public static String FACEBOOK_PAGE_ID = "YourPageName";
    View v;
    List<String> imgUrl;
    CircleButton foodExpressButton;
    MaterialCardView ppeepfoodCardView,ppeepStoreCardView,facebookCardView,youtubeCardView,ppeepRideCardView;


    public FragmentHome() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.home_fragment, container, false);
        ppeepfoodCardView=v.findViewById(R.id.ppeepfoodCard);

      // foodExpressButton= v.findViewById(R.id.foodexpress_id);

        URL offerListUrl = NetworkUtils.buildOfferUrl();
        new RestaurantListOfferTask().execute(offerListUrl);

        ppeepfoodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFoodExpress = new Intent(getApplicationContext(),FoodApp.class);
                startActivity(intentFoodExpress);



            }
        });

        ppeepStoreCardView=v.findViewById(R.id.ppeepStoreCardView);

        // foodExpressButton= v.findViewById(R.id.foodexpress_id);


        ppeepStoreCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), WebViewClass.class);
                i.putExtra(WebViewClass.WEBSITE_ADDRESS, "https://www.ppeepbd.com/store/");
                startActivity(i);



            }
        });

        ppeepRideCardView=v.findViewById(R.id.ppeepRideCardView);

        // foodExpressButton= v.findViewById(R.id.foodexpress_id);


        ppeepRideCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), PPeepRideWebview.class);
                i.putExtra(PPeepRideWebview.WEBSITE_ADDRESS, "https://www.ppeepbd.com/ride/");
                startActivity(i);



            }
        });

     //   facebookCardView=v.findViewById(R.id.facebookCardView);




      /*  facebookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               goToFacebook();
*//*
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/P-PEEP-503500450179099/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.facebook.katana");
                startActivity(intent);*//*
            }
        });*/

   /*    youtubeCardView=v.findViewById(R.id.youtubeCardView);




        youtubeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC2sv1y9olthPN5ewdr7TQ5g/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });
*/



        sliderLayout = v.findViewById(R.id.imageSlider);

        sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :



        return v;
    }


    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

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
                case 3:

                    String URL3 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(4);
                    sliderView.setImageUrl(URL3);
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

    private void goToFacebook() {
        try {
            String facebookUrl = getFacebookPageURL();
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFacebookPageURL() {
        String FACEBOOK_URL = "https://www.facebook.com/P-PEEP-503500450179099/";
        String facebookurl = null;

        try {
            PackageManager packageManager = getActivity().getPackageManager();

            if (packageManager != null) {
                Intent activated = packageManager.getLaunchIntentForPackage("com.facebook.katana");

                if (activated != null) {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

                    if (versionCode >= 3002850) {
                        facebookurl = "https://www.facebook.com/P-PEEP-503500450179099/";
                    }
                } else {
                    facebookurl = FACEBOOK_URL;
                }
            } else {
                facebookurl = FACEBOOK_URL;
            }
        } catch (Exception e) {
            facebookurl = FACEBOOK_URL;
        }
        return facebookurl;
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

                sliderLayout = v.findViewById(R.id.imageSlider);

                sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

                setSliderViews();


            }else{
                Toast.makeText(getContext(), " network not available", Toast.LENGTH_SHORT).show();
            }
        }


    }

}
