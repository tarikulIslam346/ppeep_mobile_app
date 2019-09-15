package com.example.ppeepfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ppeepfinal.adapter.ImageSliderItemDetailsAdapter;
import com.google.android.material.button.MaterialButton;

public class ShopItemDetails extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    Toolbar SHOPToolbar;
    MaterialButton shopcartAdd;
    private int dotscount;
    private ImageView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);

        viewPager = (ViewPager) findViewById(R
                .id.itemdetailsViewpager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        SHOPToolbar = (Toolbar) findViewById(R.id.shopapptoolbar);
        setSupportActionBar(SHOPToolbar);
        shopcartAdd=(MaterialButton) findViewById(R.id.shopcartAdd);
        shopcartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartOpen= new Intent(getApplicationContext(),ShopCartPage.class);
                startActivity(cartOpen);
            }
        });

        ImageSliderItemDetailsAdapter viewPagerAdapter =new ImageSliderItemDetailsAdapter(this);


        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
