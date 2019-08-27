package com.example.ppeepfinal;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;*/
import android.os.Build;
import android.os.Bundle;
/*import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;*/
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.FragmentTransitionSupport;
import androidx.viewpager.widget.ViewPager;

import com.example.ppeepfinal.data.UserCurrentOrder;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TabFragmentFoodHome extends Fragment {

    View v, nextV;

    SliderLayout sliderLayout;

    TextView mViewAll;

    ViewPager viewPager;

    CardView cardViewForSearchChiniseCusine,cardViewForSearchCFastFoodCusine,cardViewForSearchBanglaCusine,cardViewForBakeryCusine;

    List<String>imgUrl;
    String lat,lng;

   // ProgressBar progressBar;

    ProgressDialog dialog;

    private RecyclerView mNumberOfRestaurant;

    private RecommandedRestaurantListAdapter recommandedRestaurantListAdapter;
    MaterialButton currentOrder;
    UserDatabase mdb;
    String phoneNo;
    List<Integer>OrderId;
     FragmentManager fragmentManager;



    public TabFragmentFoodHome(){ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.activity_tab_fragment_recommended,container,false);
        mdb = UserDatabase.getInstance(getContext());

       /*TabFragmentRecommended fragmentHome = new TabFragmentRecommended();
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.add(R.id.tab_offer_container, fragmentHome);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/



        mViewAll = (TextView) v.findViewById(R.id.tv_view_all);

        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerId) ;

        cardViewForSearchChiniseCusine = (CardView) v.findViewById(R.id.cv_chinise);

        cardViewForBakeryCusine = (CardView) v.findViewById(R.id.cv_bakery);

        cardViewForSearchBanglaCusine = (CardView) v.findViewById(R.id.cv_bangla);

        cardViewForSearchCFastFoodCusine = (CardView) v.findViewById(R.id.cv_fastfood);

       // progressBar = (ProgressBar) v.findViewById(R.id.pv_offerr);

        mNumberOfRestaurant = (RecyclerView)v.findViewById(R.id.rv_numbers);

       List<UserModel> user =  mdb.userDAO().loadPhone();
       if(user.size()!=0){
          phoneNo = user.get(0).getPhone();
           //ShowLoder("Loading .. ..");
           //URL currentOrderInfoUrl = NetworkUtils.buildCurrentOrderInfoUrl();
           //new CurrentOrderListTask().execute(currentOrderInfoUrl);
       }


        currentOrder = (MaterialButton) v.findViewById(R.id.bt_show_current_order);

        currentOrder.setVisibility(View.INVISIBLE);







        mViewAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
               viewPager.setCurrentItem(1);
            }
        });

        cardViewForSearchChiniseCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {



                FragmentTransaction trans = getFragmentManager()
                    .beginTransaction();
                /*
                 * IMPORTANT: We use the "root frame" defined in
                 * "root_fragment.xml" as the reference to replace fragment
                 */
                Bundle arguments = new Bundle();
                arguments.putString("search", "Chinese");
                //arguments.putInt("VALUE2", 100);

                TabFragmentFoodCuisineSearch myFragment = new TabFragmentFoodCuisineSearch();
                myFragment.setArguments(arguments);
                trans.replace(R.id.root_frame, myFragment);

                /*
                 * IMPORTANT: The following lines allow us to add the fragment
                 * to the stack and return to it later, by pressing back
                 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();

            }
        });

        cardViewForBakeryCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                /*
                 * IMPORTANT: We use the "root frame" defined in
                 * "root_fragment.xml" as the reference to replace fragment
                 */
                Bundle arguments = new Bundle();
                arguments.putString("search", "Bakery");
                //arguments.putInt("VALUE2", 100);

                TabFragmentFoodCuisineSearch myFragment = new TabFragmentFoodCuisineSearch();
                myFragment.setArguments(arguments);
                trans.replace(R.id.root_frame, myFragment);

                /*
                 * IMPORTANT: The following lines allow us to add the fragment
                 * to the stack and return to it later, by pressing back
                 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });

        cardViewForSearchBanglaCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                /*
                 * IMPORTANT: We use the "root frame" defined in
                 * "root_fragment.xml" as the reference to replace fragment
                 */
                Bundle arguments = new Bundle();
                arguments.putString("search", "Bangla");
                //arguments.putInt("VALUE2", 100);

                TabFragmentFoodCuisineSearch myFragment = new TabFragmentFoodCuisineSearch();
                myFragment.setArguments(arguments);
                trans.replace(R.id.root_frame, myFragment);

                /*
                 * IMPORTANT: The following lines allow us to add the fragment
                 * to the stack and return to it later, by pressing back
                 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });

        cardViewForSearchCFastFoodCusine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                /*
                 * IMPORTANT: We use the "root frame" defined in
                 * "root_fragment.xml" as the reference to replace fragment
                 */
                Bundle arguments = new Bundle();
                arguments.putString("search", "FastFood");
                //arguments.putInt("VALUE2", 100);

                TabFragmentFoodCuisineSearch myFragment = new TabFragmentFoodCuisineSearch();
                myFragment.setArguments(arguments);
                trans.replace(R.id.root_frame, myFragment);

                /*
                 * IMPORTANT: The following lines allow us to add the fragment
                 * to the stack and return to it later, by pressing back
                 */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });





      // progressBar.setVisibility(View.VISIBLE);

       ShowLoder("Loading .. ..");

        URL offerListUrl = NetworkUtils.buildOfferUrl();
        new RestaurantListOfferTask().execute(offerListUrl);

        URL restaurantUrl = NetworkUtils.buildRecommendedRestaurantUrl();

        if(user.size() !=0){
            // Toast.makeText(getContext()," " + String.valueOf(user.get(0).getLat()), Toast.LENGTH_LONG).show();
            lat  = String.valueOf(user.get(0).getLat());
            lng = String.valueOf(user.get(0).getLng());
            new RestaurantListTask().execute(restaurantUrl);

        }


        //new RestaurantListTask().execute(restaurantUrl);







        return v;
    }

    public void ShowLoder(String message){
        dialog = ProgressDialog.show(getContext(), "",
                message, true);
    }



    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(getContext());


            switch (i) {
                case 0:
                    String URL = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(1);
                      sliderView.setImageUrl(URL);
                    break;
                case 1:
                    String URL1 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(5);
                    sliderView.setImageUrl(URL1);

                    break;
                case 2:

                    String URL2 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(6);
                    sliderView.setImageUrl(URL2);
                    break;
                case 3:

                    String URL3 = "https://foodexpress.com.bd/ppeep/public/images/offers/"+imgUrl.get(7);
                    sliderView.setImageUrl(URL3);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //  sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                  //  Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }


    }




    public class RestaurantListOfferTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String offerResults = null;
            try {
                offerResults = NetworkUtils.getOfferFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return offerResults;
        }

        @Override
        protected void onPostExecute(String RestaurantResults) {

            if (RestaurantResults != null && !RestaurantResults.equals("")) {

                String json = RestaurantResults;

                JSONObject restaurantList = null;

                JSONArray jsonArray=null;

                String imageUrl;

                imgUrl = new ArrayList<String>();

                try {
                    restaurantList = new JSONObject(json);

                    jsonArray = restaurantList.getJSONArray("offer_list");

                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject restaurant = jsonArray.getJSONObject(i);

                        imageUrl = restaurant.getString("img_url");

                        imgUrl.add(imageUrl);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               // dialog.dismiss();

                sliderLayout = v.findViewById(R.id.FoodAppimageSlider);

                sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

                setSliderViews();


            }else{
                Toast.makeText(getContext(), " network not available", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public class RestaurantListTask extends AsyncTask<URL, Void, String> implements   RecommandedRestaurantListAdapter.ListItemClickListener {

        List<String> allNames = new ArrayList<String>();
        List<String> logos = new ArrayList<String>();
        List<String> banners = new ArrayList<String>();
        List<String> OpeningTimes = new ArrayList<String>();
        List<String> ClosingTimes = new ArrayList<String>();
        List<String> Cusines = new ArrayList<String>();
        List<Integer> MerchantId = new ArrayList<Integer>();
        List<Integer> VatList = new ArrayList<Integer>();
        List<Integer> deliveryChargList = new ArrayList<Integer>();

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String RestaurantResults = null;
            try {
                RestaurantResults = NetworkUtils.getNearByRestaurantFromHttpUrl(searchUrl,lat,lng);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return RestaurantResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String RestaurantResults) {
            if (RestaurantResults != null && !RestaurantResults.equals("")) {



                String json = RestaurantResults;
                JSONObject restaurantList = null;
                JSONArray jsonArray=null;

                String name,closingTime,openingTime,cusine,message=null,logo,banner;
                int vatOfRestaurant,deliverChargeOfRestaurant;
                int merchantId;


                try {
                    restaurantList = new JSONObject(json);
                    jsonArray = restaurantList.getJSONArray("restaurnat_list");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject restaurant = jsonArray.getJSONObject(i);
                        name = restaurant.getString("restaurant_name");
                        openingTime = restaurant.getString("opening");
                        closingTime = restaurant.getString("closing");
                        cusine = restaurant.getString("cuisine");
                        merchantId = restaurant.getInt("merchant_id");
                        vatOfRestaurant = restaurant.getInt("vat");
                        deliverChargeOfRestaurant = restaurant.getInt("delivery_charges");
                        logo = restaurant.getString("logo");
                        banner = restaurant.getString("cover_image");

                        allNames.add(name);
                        logos.add(logo);
                        banners.add(banner);
                        OpeningTimes.add(openingTime);
                        ClosingTimes.add(closingTime);
                        Cusines.add(cusine);
                        MerchantId.add(merchantId);
                        VatList.add(vatOfRestaurant);
                        deliveryChargList.add(deliverChargeOfRestaurant);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    restaurantList = new JSONObject(json);
                    message = restaurantList.getString("message");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

      dialog.dismiss();
                if(message != null ){
                    Snackbar.make(v.findViewById(R.id.tab_offer_container), " "+message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }

                if(jsonArray!= null) {

                    LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, true);
                    mNumberOfRestaurant.setLayoutManager(layoutManager);

                    mNumberOfRestaurant.setHasFixedSize(true);

                    recommandedRestaurantListAdapter = new RecommandedRestaurantListAdapter(allNames, OpeningTimes, ClosingTimes, Cusines,logos,banners, this);

                    mNumberOfRestaurant.setAdapter(recommandedRestaurantListAdapter);
                }


            }else{
                Toast.makeText(getContext(), "No restaurant found or network not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            int clickedRestaurnat = MerchantId.get(clickedItemIndex).intValue();
            String restaurantName = allNames.get(clickedItemIndex);
            String cusine = Cusines.get(clickedItemIndex);
            String logo = logos.get(clickedItemIndex);
            String banner = banners.get(clickedItemIndex);
            int vat = VatList.get(clickedItemIndex);
            int deliveryCharge = deliveryChargList.get(clickedItemIndex);
            //Toast.makeText(getContext(),"restaurant id" +clickedRestaurnat ,Toast.LENGTH_SHORT).show();
            Intent foodmenuIntent = new Intent(getContext(),RestaurantMenuPage.class);
            foodmenuIntent.putExtra("mercahnt_Id",String.valueOf(clickedRestaurnat));
            foodmenuIntent.putExtra("restaurant_name",restaurantName);
            foodmenuIntent.putExtra("cuisine",cusine);
            foodmenuIntent.putExtra("vat",String.valueOf(vat));
            foodmenuIntent.putExtra("deliveryCharge",String.valueOf(deliveryCharge));
            foodmenuIntent.putExtra("logo",logo);
            foodmenuIntent.putExtra("cover_image",banner);
            startActivity(foodmenuIntent);



        }
    }

    /*public class  CurrentOrderListTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String currentOrderResults = null;
            try {
                currentOrderResults = NetworkUtils.getCurrentOrderResponseFromHttpUrl(searchUrl,phoneNo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return currentOrderResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String currentOrderResults) {
            if (currentOrderResults != null && !currentOrderResults.equals("")) {

                String json = currentOrderResults;

                JSONObject orderList = null;

                JSONArray jsonArray=null;

                int orderID;
                OrderId = new ArrayList<Integer>();


                try {
                    orderList = new JSONObject(json);
                    jsonArray = orderList.getJSONArray("order");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject restaurant = jsonArray.getJSONObject(i);
                        orderID = restaurant.getInt("order_id");
                        OrderId.add(orderID);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                dialog.dismiss();
                if(OrderId.size()!=0){
                    currentOrder.setVisibility(View.VISIBLE);
                    currentOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence[] options = new CharSequence[OrderId.size()];
                            for(int i=0;i<OrderId.size();i++){
                                options[i] = "Order Id : # "+ String.valueOf(OrderId.get(i));
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("See order");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {

                                    Intent intent = new Intent(getContext(),CurrentOrderHistory.class);
                                    intent.putExtra("order_id",String.valueOf(OrderId.get(item)));
                                    startActivity(intent);

                                    dialog.dismiss();

                                }
                            });
                            // Create the AlertDialog object and return it
                            builder.show();


                        }
                    });
                }



            }else{
                Toast.makeText(getContext(), "No network not available", Toast.LENGTH_SHORT).show();
            }
        }


    }*/




    //public void replaceFragment() {
       /* TabFragmentRecommended fragmentHome = new TabFragmentRecommended();
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.tab_offer_container, fragmentHome);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

       /* CuisineSearchFragment fragment = new CuisineSearchFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tab_offer_container, fragment);


        fragmentTransaction.commit();*/

/*
   getFragmentManager().beginTransaction().replace(R.id.food_app_layout, new CuisineSearchFragment()).addToBackStack(null).commit();
*/
       /* }*/
    }






