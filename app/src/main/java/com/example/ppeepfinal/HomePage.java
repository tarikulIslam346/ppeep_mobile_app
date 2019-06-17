package com.example.ppeepfinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderLayout;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    //Context context;
    private Button mPeep;
   // SliderLayout sliderLayout;
   // TextView profileName ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        viewPager = (ViewPager) findViewById(R.id.viewPagerId);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        mDrawerLayout = findViewById(R.id.drawerId);



        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.nav_open,R.string.nav_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get profile text_view
        //View headerView = HomePage.f(R.layout.hea);
       // profileName = (TextView) findViewById(R.id.profile_name);
       //profileName.setText(" OK");






        //add fragment here

        adapter.AddFragment(new FragmentHome(),"Home");
        adapter.AddFragment(new FragmentGroup(),"My Group");
        //  adapter.AddFragment(new FragmentNotification() ,"Promotion");
        adapter.AddFragment(new FragmentNotification(),"Notification");
        adapter.AddFragment(new FragmentMyProfile(),"My Profile");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_homelogo);
        tabLayout.getTabAt(1).setIcon(R.drawable.groupicon);




/*
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.customtab);
        }
*/
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
       /* Intent intent;
        if (menuItem.getItemId()==R.id.profile)
        {
            intent=new Intent(this,ProfileEdit.class);
            startActivity(intent);

        }

        else if (menuItem.getItemId()==R.id.history)
        {
            intent=new Intent(this,HistoryPage.class);
            startActivity(intent);

        }

        else if (menuItem.getItemId()==R.id.settings)
        {
            intent=new Intent(this,SettingsPage.class);
            startActivity(intent);

        }

        else if (menuItem.getItemId()==R.id.help)
        {
            intent=new Intent(this,HelpPage.class);
            startActivity(intent);

        }

        else if (menuItem.getItemId()==R.id.language)
        {
            intent=new Intent(this,LanguagePage.class);
            startActivity(intent);

        }

        else if (menuItem.getItemId()==R.id.policies)
        {
            intent=new Intent(this,PoliciesPage.class);
            startActivity(intent);

        }
        else if (menuItem.getItemId()==R.id.logout)
        {
            intent=new Intent(this,HomePage.class);
            startActivity(intent);

        }*/

        return false;
    }
}
