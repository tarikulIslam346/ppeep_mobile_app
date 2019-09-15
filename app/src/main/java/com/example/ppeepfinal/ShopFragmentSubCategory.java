package com.example.ppeepfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.ppeepfinal.adapter.ExpandableSubListAdapter;
import com.example.ppeepfinal.model.ExpandedSubMenuModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopFragmentSubCategory extends AppCompatActivity  {
    DrawerLayout mDrawerLayout;
    Toolbar myToolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle mToggle;
    ExpandableSubListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    MaterialCardView itemDetailsID;
    List<ExpandedSubMenuModel> listDataHeader;
    HashMap<ExpandedSubMenuModel, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_fragment_sub_category);
        Toolbar myToolbar= (Toolbar) findViewById(R.id.ppeeptoolbar);
        // TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);

        itemDetailsID=(MaterialCardView) findViewById(R.id.itemDetailsID);
        itemDetailsID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemdetailsintent=new Intent(getApplicationContext(),ShopItemDetails.class);
                startActivity(itemdetailsintent);
            }
        });

        mDrawerLayout = findViewById(R.id.subCategoryDrawer);
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDrawerLayout != null) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        }, 800);

        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.nav_open,R.string.nav_close);
        mDrawerLayout.addDrawerListener(mToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToggle.syncState();

        expandableList = (ExpandableListView) findViewById(R.id.shopsubnavigationmenu);
       navigationView = (NavigationView) findViewById(R.id.shopDrawerNavigation);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        prepareListData();
        mMenuAdapter = new ExpandableSubListAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //Log.d("DEBUG", "submenu item clicked");
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedSubMenuModel>();
        listDataChild = new HashMap<ExpandedSubMenuModel, List<String>>();

        ExpandedSubMenuModel item1 = new ExpandedSubMenuModel();
        item1.setIconName("Clothing");
        item1.setIconImg(android.R.drawable.ic_delete);
        // Adding data header
        listDataHeader.add(item1);

        ExpandedSubMenuModel item2 = new ExpandedSubMenuModel();
        item2.setIconName("Men's Footwear");
        item2.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item2);

        ExpandedSubMenuModel item3 = new ExpandedSubMenuModel();
        item3.setIconName("Men's Accessories");
        item3.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item3);

        // Adding child data
        List<String> heading1 = new ArrayList<String>();
        heading1.add("Shirt");
        heading1.add("Pant");
        heading1.add("Panjabi");
        heading1.add("Jersey");
        heading1.add("Vest");

        List<String> heading2 = new ArrayList<String>();
        heading2.add("Casual Shoes");
        heading2.add("Boots");
        heading2.add("Loafers");
        heading2.add("Formal Shoes");
        heading2.add("Sandals & Flip-Flop");
        heading2.add("Shoe Care");


        List<String> heading3 = new ArrayList<String>();
        heading3.add("Belts");
        heading3.add("Wallets");
        heading3.add("Backpack");
        heading3.add("Bags");
        heading3.add("Jwelry");



        listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
        listDataChild.put(listDataHeader.get(1), heading2);
        listDataChild.put(listDataHeader.get(2), heading3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

}