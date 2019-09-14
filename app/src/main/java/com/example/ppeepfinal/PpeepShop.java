package com.example.ppeepfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class PpeepShop extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabLayout;
    Toolbar shopToolbar;
    ViewPager viewPagerId;
    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppeep_shop);

        shopToolbar = (Toolbar) findViewById(R.id.shopapptoolbar);
        setSupportActionBar(shopToolbar);
        mDrawerLayout = findViewById(R.id.ShopDrawerId);
        NavigationView navigationView = findViewById(R.id.shopNavigationId);
        navigationView.setNavigationItemSelectedListener(this);




        tabLayout = findViewById(R.id.tabLayoutShop);
        viewPagerId = (ViewPager) findViewById(R.id.viewPagerShopId);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //add fragment here
        adapter.AddFragment(new ShopFragmentHome(),"Home");
        adapter.AddFragment(new ShopFragmentWishList(),"Wishlist");
        adapter.AddFragment(new ShopFragmentHome(),"Cart");

        viewPagerId.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPagerId);
        tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.wishlist_icon_sample);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_shopping_cart_black_24dp);



        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.nav_open,R.string.nav_close);

        mDrawerLayout.addDrawerListener(mToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToggle.syncState();



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
}
