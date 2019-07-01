package com.example.ppeepfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

public class TabFragmentRecommended extends Fragment {
    View v, nextV;
    SliderLayout sliderLayout;
    TextView mViewAll;
    ViewPager viewPager;
    CardView cardViewForSearchChiniseCusine,cardViewForSearchCFastFoodCusine,cardViewForSearchBanglaCusine,cardViewForBakeryCusine;


    public TabFragmentRecommended(){


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.activity_tab_fragment_recommended,container,false);
        mViewAll = (TextView) v.findViewById(R.id.tv_view_all);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerId) ;
        cardViewForSearchChiniseCusine = (CardView) v.findViewById(R.id.cv_chinise);
        cardViewForBakeryCusine = (CardView) v.findViewById(R.id.cv_bakery);
        cardViewForSearchBanglaCusine = (CardView) v.findViewById(R.id.cv_bangla);
        cardViewForSearchCFastFoodCusine = (CardView) v.findViewById(R.id.cv_fastfood);




        mViewAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
               viewPager.setCurrentItem(1);
            }
        });
        cardViewForSearchChiniseCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),SearchRestaurant.class);
                intent.putExtra("search","Chinese");
                startActivity(intent);
            }
        });
        cardViewForBakeryCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),SearchRestaurant.class);
                intent.putExtra("search","Bakery");
                startActivity(intent);
            }
        });
        cardViewForSearchBanglaCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),SearchRestaurant.class);
                intent.putExtra("search","Bangla");
                startActivity(intent);
            }
        });
        cardViewForSearchCFastFoodCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),SearchRestaurant.class);
                intent.putExtra("search","Fastfood");
                startActivity(intent);
            }
        });









        sliderLayout = v.findViewById(R.id.FoodAppimageSlider);
        //    sliderLayout.setIndicatorAnimation(IndicatorAnimations.NONE); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

        setSliderViews();
        return v;
    }









    private void setSliderViews() {

        for (int i = 0; i <= 2; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.offerimageslider1);
                    //  sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.offerimageslider2);
                    // sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");

                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.offerimageslider4);
                    //  sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;

            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //  sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }


    }





    }

