package com.example.ppeepfinal;

import android.content.Intent;
/*import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class FoodApp extends AppCompatActivity {
Toolbar foodToolbar;
Menu foodCart;
    private TabLayout tabLayoutId;
    private ViewPager viewPagerId;
    private ViewPagerAdapter adapter;
    SearchView restaurantSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_app);
        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        restaurantSearchView = (SearchView)  findViewById(R.id.sv_for_restaurant);

        restaurantSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(getApplicationContext(),SearchRestaurant.class);
                intent.putExtra("search",query);
                startActivity(intent);

                return false;
            }

        });


/*
 @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.foodappcart, menu);
            foodCart=menu.findItem(R.id.action_drawer_cart);
            return super.onCreateOptionsMenu(menu);
        }
*/

        tabLayoutId = (TabLayout) findViewById(R.id.tabLayoutId);
        viewPagerId = (ViewPager) findViewById(R.id.viewPagerId);




        adapter.AddFragment(new TabFragmentRecommended(),"Home");
        adapter.AddFragment(new TabFragmentNearby(),"Nearby");
        //  adapter.AddFragment(new FragmentNotification() ,"Promotion");
        adapter.AddFragment(new TabFragmentPopular(),"Popular");
        adapter.AddFragment(new TabFragmentOffer(),"Free Delivery");


        viewPagerId.setAdapter(adapter);
        tabLayoutId.setupWithViewPager(viewPagerId);

        tabLayoutId.getTabAt(0).setIcon(R.drawable.homeicon);
        tabLayoutId.getTabAt(1).setIcon(R.drawable.nearbyicon);
        tabLayoutId.getTabAt(2).setIcon(R.drawable.recommendedicon2);
        tabLayoutId.getTabAt(3).setIcon(R.drawable.freedeliveryicon);




    }


}
