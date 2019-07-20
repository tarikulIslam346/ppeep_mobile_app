package com.example.ppeepfinal;

import android.os.Bundle;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;


public class FragmentMyProfile extends Fragment {

    View v;
    SliderLayout sliderLayout;
    TextView myProfileName,myPhoneNo;
    CircleImageView circleImageView;

    private UserDatabase mdb;

    public FragmentMyProfile() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      v=inflater.inflate(R.layout.test_home_layout,container,false);



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
}
