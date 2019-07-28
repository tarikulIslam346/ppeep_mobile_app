package com.example.ppeepfinal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import at.markushi.ui.CircleButton;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class FragmentHome extends Fragment {

    SliderLayout sliderLayout;
    public static String FACEBOOK_URL = "https://www.facebook.com/YourPageName";
    public static String FACEBOOK_PAGE_ID = "YourPageName";
    View v;

    CircleButton foodExpressButton;
    MaterialCardView ppeepfoodCardView,ppeepStoreCardView,facebookCardView,youtubeCardView,ppeepRideCardView;


    public FragmentHome() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.home_fragment, container, false);
        ppeepfoodCardView=v.findViewById(R.id.ppeepfoodCard);

      // foodExpressButton= v.findViewById(R.id.foodexpress_id);


        ppeepfoodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFoodExpress = new Intent(getApplicationContext(),FoodApp.class);
                startActivity(intentFoodExpress);



            }
        });

        ppeepStoreCardView=v.findViewById(R.id.ppeepStoreCardView);

        // foodExpressButton= v.findViewById(R.id.foodexpress_id);


        ppeepStoreCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), WebViewClass.class);
                i.putExtra(WebViewClass.WEBSITE_ADDRESS, "https://www.ppeepbd.com/store/");
                startActivity(i);



            }
        });

        ppeepRideCardView=v.findViewById(R.id.ppeepRideCardView);

        // foodExpressButton= v.findViewById(R.id.foodexpress_id);


        ppeepRideCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), PPeepRideWebview.class);
                i.putExtra(PPeepRideWebview.WEBSITE_ADDRESS, "https://www.ppeepbd.com/ride/");
                startActivity(i);



            }
        });

     //   facebookCardView=v.findViewById(R.id.facebookCardView);




      /*  facebookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               goToFacebook();
*//*
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/P-PEEP-503500450179099/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.facebook.katana");
                startActivity(intent);*//*
            }
        });*/

   /*    youtubeCardView=v.findViewById(R.id.youtubeCardView);




        youtubeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC2sv1y9olthPN5ewdr7TQ5g/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });
*/



        sliderLayout = v.findViewById(R.id.imageSlider);

        sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :

        setSliderViews();

        return v;
    }


    private void setSliderViews() {

        for (int i = 0; i <= 4; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            switch (i) {
                case 0:
                  sliderView.setImageDrawable(R.drawable.dashkey);
                //  sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.alhamdulillah);
                 // sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");

                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.dashassetkey);
                  //  sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.alhamdulillah2);
                  // sliderView.setImageUrl("https://photos.google.com/u/1/search/_tra_/photo/AF1QipM82iVT4b8z7IkgjIIHwGWL7Y1cVKUIakZjrbMg");
                    break;
                case 4:
                    sliderView.setImageDrawable(R.drawable.dashkey);
                    //sliderView.setImageUrl("https://www.google.com/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwjB4o7S7JXiAhVB4HMBHRKTABMQjRx6BAgBEAU&url=https%3A%2F%2Fsoftexpo.com.bd%2Fexhibitor%2Fp-peep%2F&psig=AOvVaw1wreEDMiJSLocTV4AtbRH9&ust=1557745577087943");
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

    private void goToFacebook() {
        try {
            String facebookUrl = getFacebookPageURL();
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFacebookPageURL() {
        String FACEBOOK_URL = "https://www.facebook.com/P-PEEP-503500450179099/";
        String facebookurl = null;

        try {
            PackageManager packageManager = getActivity().getPackageManager();

            if (packageManager != null) {
                Intent activated = packageManager.getLaunchIntentForPackage("com.facebook.katana");

                if (activated != null) {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

                    if (versionCode >= 3002850) {
                        facebookurl = "https://www.facebook.com/P-PEEP-503500450179099/";
                    }
                } else {
                    facebookurl = FACEBOOK_URL;
                }
            } else {
                facebookurl = FACEBOOK_URL;
            }
        } catch (Exception e) {
            facebookurl = FACEBOOK_URL;
        }
        return facebookurl;
    }




}
