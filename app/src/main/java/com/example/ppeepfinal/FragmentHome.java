package com.example.ppeepfinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class FragmentHome extends Fragment {

    SliderLayout sliderLayout;

    public static String FACEBOOK_URL = "https://www.facebook.com/YourPageName";

    public static String FACEBOOK_PAGE_ID = "YourPageName";

    ProgressDialog dialog;
    private LinearLayout mLinearLayout;
    private UserDatabase mdb;// declear databse

    private String myPhone,myWorkAddress,myHomeAddress,updateString,updateTask;

    List<UserModel> user;

    View v;

    List<String> imgUrl;


    TextView mHomeAddress,mWorkAddress;


    CircleButton foodExpressButton;

    MaterialCardView ppeepfoodCardView,ppeepStoreCardView,facebookCardView,youtubeCardView,ppeepRideCardView;
    private PopupWindow mPopupWindow;
    PopupWindow popupWindow;
    public FragmentHome() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.home_fragment, container, false);

        ppeepfoodCardView=v.findViewById(R.id.ppeepfoodCard);
        mLinearLayout =(LinearLayout) v.findViewById(R.id.sb_home_fragment);

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

       /*   Intent i = new Intent(getContext(), WebViewClass.class);
                i.putExtra(WebViewClass.WEBSITE_ADDRESS, "https://www.ppeepbd.com/store/");
                startActivity(i);*/
         /*  LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.shop_coming_soon,null);

                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                //instantiate popup window
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                //display the popup window
                popupWindow.showAtLocation(mLinearLayout, Gravity.CENTER, 0, 0);

                //close the popup window on button click
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });*/


         Intent shopIntent=new Intent(getContext(),PpeepShop.class);
         startActivity(shopIntent);

         //       showAlertDialogButtonClicked(v);

            }

        });





        ppeepRideCardView=v.findViewById(R.id.ppeepRideCardView);

        ppeepRideCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   Intent i = new Intent(getContext(), PPeepRideWebview.class);
                i.putExtra(PPeepRideWebview.WEBSITE_ADDRESS, "https://www.ppeepbd.com/ride/");
                startActivity(i);*/

                showAlertDialogRide(v);


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

    public void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        TextView title = new TextView(getContext());
// You Can Customise your Title here
        title.setText("P-PEEP Shop");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.yellow));
        title.setTextSize(22);

        builder.setCustomTitle(title);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.shop_coming_soon, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertDialogRide(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        TextView title = new TextView(getContext());
// You Can Customise your Title here
        title.setText("P-PEEP Ride");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.yellow));
        title.setTextSize(22);

        builder.setCustomTitle(title);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.ride_coming_soon, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
               /* View parentLayout = v.findViewById(R.id.sb_home_fragment);
                Snackbar.make(parentLayout, "Net connection error", Snackbar.LENGTH_INDEFINITE)
                        .show();*/

                View parentLayout = v.findViewById(R.id.sb_home_fragment);
                Snackbar snackbar = Snackbar.make(parentLayout, " Net Connection Error", Snackbar.LENGTH_INDEFINITE)
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
                    Snackbar snackbar = Snackbar.make(parentLayout, "" + error, Snackbar.LENGTH_INDEFINITE)
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
                     * Change  text color
                     * */
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(android.R.color.black ));

                    /* Fix it
                     * Change background  color
                     * */
                    sbView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    snackbar.show();
                }

            } else {
                //Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                View parentLayout = v.findViewById(R.id.sb_home_fragment);

                Snackbar snackbar = Snackbar.make(parentLayout, " Network not available", Snackbar.LENGTH_INDEFINITE)
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
