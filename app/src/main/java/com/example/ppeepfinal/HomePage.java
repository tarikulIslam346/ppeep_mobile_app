package com.example.ppeepfinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
/*import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;*/
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.ppeepfinal.data.OrderMerchantModel;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.Api;
import com.example.ppeepfinal.utilities.MyLocation;
import com.example.ppeepfinal.utilities.VolleyRequest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.github.florent37.bubbletab.BubbleTab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.List;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mToggle;

    private BubbleTab tabLayoutId;
    private ViewPager viewPagerId;

    private ViewPagerAdapter adapter;
    TextView navUsername;
    View headerView;
    private double lat,lng;

    private UserDatabase mdb;// declear databse

    List<UserModel> user;
    ProgressDialog dialog;
    private static final String TAG = "HomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        mdb = UserDatabase.getInstance(getApplicationContext());//call database here

        //Load user info
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

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                       // String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, "Token "+ token);
                       // Toast.makeText(HomePage.this,"Token : "+token, Toast.LENGTH_LONG).show();
                    }
                });
        List<OrderMerchantModel> orderMerchantModels =  mdb.orderMercahntDAO().loadOrderMerchant();
        if(orderMerchantModels.size() != 0){
            Toast.makeText(HomePage.this,""+orderMerchantModels.get(orderMerchantModels.size()-1).getMerchantName(),Toast.LENGTH_LONG).show();
        }




        NavigationView navigationView = findViewById(R.id.NavigationId2);
        // navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        tabLayoutId = (BubbleTab) findViewById(R.id.tabLayoutId);


        viewPagerId = (ViewPager) findViewById(R.id.viewPagerId);
        tabLayoutId.setupWithViewPager(viewPagerId);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mDrawerLayout = findViewById(R.id.drawerId);



        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.nav_open,R.string.nav_close);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        headerView = navigationView.getHeaderView(0);

        navUsername = (TextView) headerView.findViewById(R.id.profile_name);

         loadUserFromDb();

        //add fragment here
        adapter.AddFragment(new FragmentHome(),"Home");

        adapter.AddFragment(new FragmentGroup(),"My Group");

        //  adapter.AddFragment(new FragmentNotification() ,"Promotion");

        adapter.AddFragment(new FragmentNotification(),"Notification");

        adapter.AddFragment(new FragmentMyProfile(),"My Profile");

        tabLayoutId = (BubbleTab) findViewById(R.id.tabLayoutId);


        viewPagerId = (ViewPager) findViewById(R.id.viewPagerId);
        tabLayoutId.setupWithViewPager(viewPagerId);

    /*    tabLayout.getTabAt(0).setIcon(R.drawable.icon_homelogo);

        tabLayout.getTabAt(1).setIcon(R.drawable.groupicon);
        tabLayout.getTabAt(2).setIcon(R.drawable.notificationicon);
        tabLayout.getTabAt(3).setIcon(R.drawable.myprofileicon);*/

        viewPagerId.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadUserFromDb();
    }

    public  void loadUserFromDb(){


        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table

        if(user.size()!=0) {navUsername.setText(user.get(0).getName());}
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
            intent=new Intent(this,HelpPage.class);
            startActivity(intent);

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
                ShowLoder("Loading..");
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
                        Toast.makeText(HomePage.this, error, Toast.LENGTH_SHORT).show();
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



   /* @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
     Intent intent;
        if (menuItem.getItemId()==R.id.profile)
        {
           *//* intent=new Intent(this,ProfileEdit.class);
            startActivity(intent);*//*
            Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
        }

        else if (menuItem.getItemId()==R.id.history)
        {
            intent=new Intent(this,HistoryPage.class);
            startActivity(intent);

        }

        else if (menuItem.getItemId()==R.id.settings)
        {
            intent=new Intent(this,PromotionPage.class);
            startActivity(intent);

        }

        else if (menuItem.getItemId()==R.id.help)
        {
            intent=new Intent(this,HelpPage.class);
            startActivity(intent);

        }

        Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();

        return false;
    }*/
}
