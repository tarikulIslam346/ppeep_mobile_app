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
import android.widget.TextView;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;

import java.util.List;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mToggle;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private ViewPagerAdapter adapter;

    private UserDatabase mdb;// declear databse

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);

        viewPager = (ViewPager) findViewById(R.id.viewPagerId);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mDrawerLayout = findViewById(R.id.drawerId);

        mdb = UserDatabase.getInstance(getApplicationContext());//call database here

        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.nav_open,R.string.nav_close);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.NavigationId);

        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = (TextView) headerView.findViewById(R.id.profile_name);

        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table

        if(user.size()!=0) {navUsername.setText(user.get(0).getName());}

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
        tabLayout.getTabAt(2).setIcon(R.drawable.notificationicon);
        tabLayout.getTabAt(3).setIcon(R.drawable.myprofileicon);

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
