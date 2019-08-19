package com.example.ppeepfinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;

import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
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

    ProgressDialog dialog;

    private UserDatabase mdb;// declear databse

    private String myPhone,myWorkAddress,myHomeAddress,updateString,updateTask;

    List<UserModel> user;

    View v;

    List<String> imgUrl;


    TextView mHomeAddress,mWorkAddress;


    CircleButton foodExpressButton;

    MaterialCardView ppeepfoodCardView,ppeepStoreCardView,facebookCardView,youtubeCardView,ppeepRideCardView;


    public FragmentHome() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.home_fragment, container, false);

        ppeepfoodCardView=v.findViewById(R.id.ppeepfoodCard);

        mHomeAddress = v.findViewById(R.id.tv_home_address);

        mWorkAddress = v.findViewById(R.id.tv_work_address);


        mdb = UserDatabase.getInstance(getApplicationContext());//call database here

        loadUserFromDb();


        URL offerListUrl = NetworkUtils.buildOfferUrl();
        new RestaurantListOfferTask().execute(offerListUrl);

        loadUserAddress();


        Intent intent = getActivity().getIntent();
        myWorkAddress = intent.getStringExtra("work_address");
        myHomeAddress = intent.getStringExtra("home_address");

        if(myHomeAddress !=null){
            ShowLoder("Updating home address..");
            updateString ="address";
            mHomeAddress.setText(myHomeAddress);
            updateTask = myHomeAddress;
            URL profileUpdateUrl = NetworkUtils.buildProfileUpdateUrl();
            new ProfileAdressUpdateTask().execute(profileUpdateUrl);
        }

        if(myWorkAddress !=null){
            ShowLoder("Updating work address...");
            updateString ="work_address";
            mWorkAddress.setText(myWorkAddress);
            updateTask = myWorkAddress;
            URL profileUpdateUrl = NetworkUtils.buildProfileUpdateUrl();
            new ProfileAdressUpdateTask().execute(profileUpdateUrl);
        }

        mHomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryMap = new Intent(getContext(),UserAutoCompleteAdress.class);
                deliveryMap.putExtra("activity","home_address");
                startActivity(deliveryMap);

            }
        });

        mWorkAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryMap = new Intent(getContext(),UserAutoCompleteAdress.class);
                deliveryMap.putExtra("activity","home_work_address");
                startActivity(deliveryMap);
            }
        });


        ppeepfoodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFoodExpress = new Intent(getApplicationContext(),FoodApp.class);
                startActivity(intentFoodExpress);



            }
        });

        ppeepStoreCardView=v.findViewById(R.id.ppeepStoreCardView);

        ppeepStoreCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), WebViewClass.class);
                i.putExtra(WebViewClass.WEBSITE_ADDRESS, "https://www.ppeepbd.com/store/");
                startActivity(i);



            }
        });

        ppeepRideCardView=v.findViewById(R.id.ppeepRideCardView);

        ppeepRideCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), PPeepRideWebview.class);
                i.putExtra(PPeepRideWebview.WEBSITE_ADDRESS, "https://www.ppeepbd.com/ride/");
                startActivity(i);



            }
        });

        sliderLayout = v.findViewById(R.id.imageSlider);

        //sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :

        //setSliderViews();

        return v;
    }

    public  void loadUserFromDb(){

        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table

        if(user.size()!=0) {
            myPhone = user.get(0).getPhone();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserFromDb();
        loadUserAddress();
    }




       // sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :



    public void loadUserAddress(){
        if(myPhone !=null){
           // ShowLoder("Loading address...");
            URL profileUrl = NetworkUtils.buildProfileDetailInfoUrl();
            new ProfileDetailTask().execute(profileUrl);
        }
    }


    public void ShowLoder(String message){
         dialog = ProgressDialog.show(getContext(), "",
                message, true);
    }


    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());


            switch (i) {
                case 0:
                    String URL = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(2);
                    sliderView.setImageUrl(URL);
                    break;
                case 1:
                    String URL1 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(3);
                    sliderView.setImageUrl(URL1);

                    break;
                case 2:

                    String URL2 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(4);
                    sliderView.setImageUrl(URL2);
                    break;
                case 3:

                    String URL3 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(0);
                    sliderView.setImageUrl(URL3);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                //    Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

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

    public class ProfileDetailTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String profileDetailResults = null;
            try {
                profileDetailResults = NetworkUtils.getProfileDetailResponseFromHttpUrl(searchUrl,myPhone);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return profileDetailResults;
        }

        @Override
        protected void onPostExecute(String profileDetailResults) {
            if (profileDetailResults != null && !profileDetailResults.equals("")) {



                String json = profileDetailResults;
                JSONObject userInfo = null;
                JSONArray jsonArray=null;

                String userName = null,email = null,gender =null,dob=null,address = null,work_address=null;
               // dialog.dismiss();



                try {
                    userInfo = new JSONObject(json);
                    jsonArray = userInfo.getJSONArray("user");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject userProfile = jsonArray.getJSONObject(i);
                        address = userProfile.getString("address");
                        work_address = userProfile.getString("work_address");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //dialog.dismiss();


                if(address != null ){
                    mHomeAddress.setText(address);
                }
                if(work_address != null ){
                    mWorkAddress.setText(work_address);
                }


            }else{
                //dialog.dismiss();
                View parentLayout = v.findViewById(R.id.sb_home_fragment);
                Snackbar.make(parentLayout, "Net connection error", Snackbar.LENGTH_INDEFINITE)
                        .show();
            }
        }


    }

    public class ProfileAdressUpdateTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String profileUpdateResults = null;
            try {
                profileUpdateResults = NetworkUtils.getProfileUpdateResponseFromHttpUrl(searchUrl, myPhone, updateString, updateTask);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return profileUpdateResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String profileUpdateResults) {
            if (profileUpdateResults != null && !profileUpdateResults.equals("")) {


                String json = profileUpdateResults;
                JSONObject userInfo = null;

                String error = null, success = null;
                try {
                    userInfo = new JSONObject(json);
                    success = userInfo.getString("Success");
                    error = userInfo.getString("error");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();


                if (success != null) {

                    View parentLayout = v.findViewById(R.id.sb_home_fragment);
                    Snackbar.make(parentLayout, "" + success, Snackbar.LENGTH_LONG)
                            .show();
                }
                if (error != null) {
                    View parentLayout = v.findViewById(R.id.sb_home_fragment);
                    Snackbar.make(parentLayout, "" + error, Snackbar.LENGTH_LONG)
                            .show();
                }

            } else {
                //Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                View parentLayout = v.findViewById(R.id.sb_home_fragment);
                Snackbar.make(parentLayout, "Network not available", Snackbar.LENGTH_LONG)
                        .show();

            }
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

                sliderLayout = v.findViewById(R.id.imageSlider);

                sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

                setSliderViews();


            }else{
                Toast.makeText(getContext(), " network not available", Toast.LENGTH_SHORT).show();
            }
        }


    }

}
