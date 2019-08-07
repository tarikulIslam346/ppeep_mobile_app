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

        sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :

        setSliderViews();

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

        for (int i = 0; i <= 4; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            switch (i) {
                case 0:
                  sliderView.setImageDrawable(R.drawable.dashkey);
                //  sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.alhamdulillah);
                 // sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");

                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.dashassetkey);
                  //  sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.alhamdulillah2);
                  // sliderView.setImageUrl("https://photos.google.com/u/1/search/_tra_/photo/AF1QipM82iVT4b8z7IkgjIIHwGWL7Y1cVKUIakZjrbMg");
                    break;
                case 4:
                    sliderView.setImageDrawable(R.drawable.dashkey);
                    //sliderView.setImageUrl("https://www.google.com/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwjB4o7S7JXiAhVB4HMBHRKTABMQjRx6BAgBEAU&url=https%3A%2F%2Fsoftexpo.com.bd%2Fexhibitor%2Fp-peep%2F&psig=AOvVaw1wreEDMiJSLocTV4AtbRH9&ust=1557745577087943");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                      Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
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




}
