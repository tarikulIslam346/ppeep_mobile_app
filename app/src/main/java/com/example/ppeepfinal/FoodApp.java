package com.example.ppeepfinal;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

public class FoodApp extends AppCompatActivity {
Toolbar foodToolbar;
Menu foodCart;
    private TabLayout tabLayoutId;
    private ViewPager viewPagerId;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_app);
        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


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




        adapter.AddFragment(new TabFragmentRecommended(),"Recommended");
        adapter.AddFragment(new TabFragmentNearby(),"Nearby");
        //  adapter.AddFragment(new FragmentNotification() ,"Promotion");
        adapter.AddFragment(new TabFragmentPopular(),"Popular");
        adapter.AddFragment(new TabFragmentOffer(),"Offer");


        viewPagerId.setAdapter(adapter);
        tabLayoutId.setupWithViewPager(viewPagerId);

        tabLayoutId.getTabAt(0).setIcon(R.drawable.recommendedicon);
        tabLayoutId.getTabAt(1).setIcon(R.drawable.nearbyicon);
        tabLayoutId.getTabAt(2).setIcon(R.drawable.recommendedicon2);
        tabLayoutId.getTabAt(3).setIcon(R.drawable.offericon);




    }


}
