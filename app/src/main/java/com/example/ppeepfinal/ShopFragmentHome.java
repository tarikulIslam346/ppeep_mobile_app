package com.example.ppeepfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

public class ShopFragmentHome extends Fragment {

    SliderLayout sliderLayout;
    private TabLayout tabLayout;
    Toolbar shopToolbar;
    ViewPager viewPagerId;
    Button brandShopButton,shopcategoryMain;
    View v;
    private ViewPagerAdapter adapter;
    MaterialCardView menCategory;
    public ShopFragmentHome() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_shop_fragment_home, container, false);
        sliderLayout = v.findViewById(R.id.ShopAppimageSlider);

        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

       // menCategory=(MaterialCardView) v.findViewById(R.id.menShopCategory);

        brandShopButton = (Button) v.findViewById(R.id.brandShopid);
        shopcategoryMain =(Button) v.findViewById(R.id.maincategory);

        brandShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                /*
                 * IMPORTANT: We use the "root frame" defined in
                 * "root_fragment.xml" as the reference to replace fragment
                 */

                ShopFragmentBrandShop myFragment = new ShopFragmentBrandShop();
                trans.replace(R.id.shop_root_home, myFragment);

                /*
                 * IMPORTANT: The following lines allow us to add the fragment
                 * to the stack and return to it later, by pressing back
                 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });
        shopcategoryMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                /*
                 * IMPORTANT: We use the "root frame" defined in
                 * "root_fragment.xml" as the reference to replace fragment
                 */

                ShopCategoryFragment myFragment = new ShopCategoryFragment();
                trans.replace(R.id.shop_root_home, myFragment);

                /*
                 * IMPORTANT: The following lines allow us to add the fragment
                 * to the stack and return to it later, by pressing back
                 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });

        /*menCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mencategory = new Intent(getContext(),ShopFragmentSubCategory.class);
                startActivity(mencategory);
            }
        });*/

      //  setSliderViews();


        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        /*
         * When this container fragment is created, we fill it with our first
         * "real" fragment
         */
        transaction.replace(R.id.shop_root_home, new ShopCategoryFragment());

        transaction.commit();

        return v;
    }

   /* private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());


            switch (i) {
                case 0:
                    String URL = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(2);
                    sliderView.setImageUrl(URL);
                    break;
                case 1:
                    String URL1 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(3);
                    sliderView.setImageUrl(URL1);

                    break;
                case 2:

                    String URL2 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(4);
                    sliderView.setImageUrl(URL2);
                    break;
                case 3:

                    String URL3 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(0);
                    sliderView.setImageUrl(URL3);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    //    Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            sliderLayout.addSliderView(sliderView);
        }


    }*/
}