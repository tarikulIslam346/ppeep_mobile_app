package com.example.ppeepfinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
/*import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;*/
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.ppeepfinal.data.OrderMerchantModel;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.Api;
import com.example.ppeepfinal.utilities.MyLocation;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.example.ppeepfinal.utilities.VolleyRequest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.github.florent37.bubbletab.BubbleTab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener   {

    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mToggle;

    private BubbleTab tabLayoutId;
    private ViewPager viewPagerId;

    private ViewPagerAdapter adapter;
    TextView navUsername;
    View headerView;
    private double lat,lng;
    Toolbar myToolbar;

    private UserDatabase mdb;// declear databse

    List<UserModel> user;
    ProgressDialog dialog;
    String phoneNo,fcm=null;
    private static final String TAG = "HomePage";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        mdb = UserDatabase.getInstance(getApplicationContext());//call database here

        user = mdb.userDAO().loadPhone();

        setUserLocation();




        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        fcm = token;
                        URL fcmUrl = NetworkUtils.buildUpdateUserFCMInfoUrl();
                        new UpdateFcmTask().execute(fcmUrl);
                        Log.d(TAG, "Token "+ token);
                       // Toast.makeText(HomePage.this,"Token : "+token, Toast.LENGTH_LONG).show();
                    }
                });

        NavigationView navigationView = findViewById(R.id.NavigationId2);
        navigationView.setNavigationItemSelectedListener(this);

        tabLayoutId = (BubbleTab) findViewById(R.id.tabLayoutId);


        viewPagerId = (ViewPager) findViewById(R.id.viewPagerId);
        tabLayoutId.setupWithViewPager(viewPagerId);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mDrawerLayout = findViewById(R.id.drawerId);

      Toolbar myToolbar= (Toolbar) findViewById(R.id.ppeeptoolbar);
        // TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
   setSupportActionBar(myToolbar);



        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.nav_open,R.string.nav_close);

        mDrawerLayout.addDrawerListener(mToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToggle.syncState();






        headerView = navigationView.getHeaderView(0);

        navUsername = (TextView) headerView.findViewById(R.id.profile_name);

         loadUserFromDb();

        //add fragment here
        adapter.AddFragment(new FragmentHome(),"Home");
        adapter.AddFragment(new FragmentGroup(),"My Group");
        adapter.AddFragment(new FragmentNotification(),"Notification");
        adapter.AddFragment(new FragmentMyProfile(),"My Profile");
        tabLayoutId = (BubbleTab) findViewById(R.id.tabLayoutId);
        viewPagerId = (ViewPager) findViewById(R.id.viewPagerId);
        tabLayoutId.setupWithViewPager(viewPagerId);
        viewPagerId.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadUserFromDb();
    }

    public  void loadUserFromDb(){


        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table

        if(user.size()!=0) {
            navUsername.setText(user.get(0).getName());
            phoneNo = user.get(0).getPhone();
        }
    }


 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
        {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent intent;
        if (menuItem.getItemId()==R.id.profile)
        {
         intent=new Intent(getApplicationContext(),ProfileEdit.class);
            startActivity(intent);

        }

        else if (menuItem.getItemId()==R.id.history)
        {
            intent=new Intent(this,HistoryPage.class);
            startActivity(intent);

        }
        

        else if (menuItem.getItemId()==R.id.help)
        {
            Intent i = new Intent(getApplicationContext(), SupportPageWebView.class);
            i.putExtra(WebViewClass.WEBSITE_ADDRESS, "http://support.ppeepbd.com/");
            startActivity(i);


        }



        return false;
    }
    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }
    public void setUserLocation(){
        MyLocation myLocation = new MyLocation(HomePage.this);
        myLocation.setListener(new MyLocation.MyLocationListener() {
            @Override
            public void onLocationFound(Location location) {

                lat = location.getLatitude();
                lng = location.getLongitude();
                ShowLoder("Update Location .....");
                URL userCurrentLocationUrl = NetworkUtils.buildUpdateUserLocationInfoUrl();
                new UpdateLocationTask().execute(userCurrentLocationUrl);


                VolleyRequest volleyRequest = new VolleyRequest(HomePage.this);
                volleyRequest.VolleyGet(Api.reverseGeo + "demo?lat=" + lat + "&lng=" + lng + "&address_level=UPTO_THANA");
                volleyRequest.setListener(new VolleyRequest.MyServerListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String address = response.getJSONObject("result").getString("address");
                           dialog.dismiss();
                            if(user.size()!=0){
                                int Id = user.get(0).getId();
                                UserModel updateUser = mdb.userDAO().loadUserById(Id);
                                updateUser.setLat(lat);
                                updateUser.setLng(lng);
                                updateUser.setAddress(address);
                                mdb.userDAO().updateUser(updateUser);
                            }



                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onError(String error) {
                        dialog.dismiss();
                        Toast.makeText(HomePage.this, error, Toast.LENGTH_SHORT).show();
                   /*     Snackbar.make(findViewById(R.id.layout_home_page), ""+error, Snackbar.LENGTH_INDEFINITE)
                                .setAction("Close", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .show();*/


                        Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_home_page), " "+error, Snackbar.LENGTH_INDEFINITE)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                /* Fix it
                                 * Change Action text color
                                 * setActionTextColor(Color.RED)
                                 * */
                                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));

                        View sbView = snackbar.getView();

                        /* Fix it
                         * Change  text coler
                         * */
                        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(getResources().getColor(android.R.color.black ));

                        /* Fix it
                         * Change background  color
                         * */
                        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        snackbar.show();
                    }

                    @Override
                    public void responseCode(int resposeCode) {

                    }
                });

            }

            @Override
            public void onFailed() {

            }
        });
    }


    public class UpdateLocationTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String updateLocationResults = null;
            try {
                updateLocationResults = NetworkUtils.updateUserLocationFromHttpUrl(searchUrl,phoneNo,String.valueOf(lat),String.valueOf(lng));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return updateLocationResults ;
        }

        @Override
        protected void onPostExecute(String updateLocationResults) {
            if (updateLocationResults != null && !updateLocationResults.equals("")) {

                String json = updateLocationResults;
                JSONObject userLocationUpdate = null;
                String  message=null,error=null;
                //dialog.dismiss();
                try {
                    userLocationUpdate = new JSONObject(json);
                    message = userLocationUpdate.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    userLocationUpdate = new JSONObject(json);
                    error = userLocationUpdate.getString("error");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                if(message!=null)Log.d(TAG,message);
                if(error!=null)Log.d(TAG,error);


            }else{
                dialog.dismiss();
               // Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.layout_home_page), " Check Your Connection", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        }
    }


    public class UpdateFcmTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String updateFcmResults = null;
            try {
                updateFcmResults = NetworkUtils.updateUserFcmFromHttpUrl(searchUrl,phoneNo,fcm);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return updateFcmResults ;
        }

        @Override
        protected void onPostExecute(String updateFcmResults) {
            if (updateFcmResults != null && !updateFcmResults.equals("")) {

                String json = updateFcmResults;
                JSONObject userLocationUpdate = null;
                String  message=null,error=null;
                try {
                    userLocationUpdate = new JSONObject(json);
                    message = userLocationUpdate.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    userLocationUpdate = new JSONObject(json);
                    error = userLocationUpdate.getString("error");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                if(message!=null)Log.d(TAG,message);
                if(error!=null)Log.d(TAG,error);


            }else{
               // dialog.dismiss();
               // Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.layout_home_page), " Check Your Connection", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        }
    }




 @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    HomePage.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
