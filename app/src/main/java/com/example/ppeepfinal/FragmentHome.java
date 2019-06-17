package com.example.ppeepfinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class FragmentHome extends Fragment {
    SliderLayout sliderLayout;
    View v;
    SparkButton pickabooButton,darazButton,robishopButton,bagdoomButton,pickabooButton2,darazButton2,robishopButton2,bagdoomButton2;

    public FragmentHome() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_fragment, container, false);

      //  View v = getLayoutInflater().inflate(R.layout.customtab, null);
      //  v.findViewById(R.id.icon).setBackgroundResource(R.drawable.icon_homelogo);



pickabooButton= v.findViewById(R.id.facebook_button1);
        darazButton= v.findViewById(R.id.facebook_button2);
        robishopButton= v.findViewById(R.id.facebook_button3);
        bagdoomButton= v.findViewById(R.id.facebook_button4);


//pickaboo listener
       pickabooButton.setEventListener(new SparkEventListener() {
                                     @Override
                                     public void onEvent(ImageView button, boolean buttonState) {
                                         Intent intent = new Intent(getContext(), WebView.class);
                                         intent.putExtra("uri", "https://foodexpress.com.bd/");
                                         startActivity(intent);

                                     }

           @Override
           public void onEventAnimationEnd(ImageView button, boolean buttonState) {

           }

           @Override
           public void onEventAnimationStart(ImageView button, boolean buttonState) {

           }
       });




        //daraz listener
        darazButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.daraz.com.bd/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.daraz.android");
                startActivity(intent);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

//robishop listener
        robishopButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://robishop.com.bd/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("net.omobio.robisc");
                startActivity(intent);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });




        //bagdoom listener
        bagdoomButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bagdoom.com/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.mcc.bagdoom");
                startActivity(intent);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });


        //below buttons




        robishopButton2= v.findViewById(R.id.facebook_button31);
        pickabooButton2= v.findViewById(R.id.facebook_button32);
        bagdoomButton2= v.findViewById(R.id.facebook_button33);
        darazButton2= v.findViewById(R.id.facebook_button34);

//pickaboo listener
        pickabooButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pickaboo.com/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.pickaboo.app");
                startActivity(intent);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });




        //daraz listener
        darazButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.daraz.com.bd/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.daraz.android");
                startActivity(intent);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

//robishop listener
        robishopButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://robishop.com.bd/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("net.omobio.robisc");
                startActivity(intent);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });




        //bagdoom listener
        bagdoomButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bagdoom.com/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.mcc.bagdoom");
                startActivity(intent);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });









        sliderLayout = v.findViewById(R.id.imageSlider);
    //    sliderLayout.setIndicatorAnimation(IndicatorAnimations.NONE); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
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

}
