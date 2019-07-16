package com.example.ppeepfinal;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dingi.dingisdk.camera.CameraUpdate;
import com.dingi.dingisdk.camera.CameraUpdateFactory;
import com.dingi.dingisdk.geometry.LatLng;
import com.dingi.dingisdk.maps.DingiMap;
import com.example.ppeepfinal.data.OrderMerchantModel;
import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.Api;
import com.example.ppeepfinal.utilities.MyLocation;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.example.ppeepfinal.utilities.VolleyRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.DecompositionType.VERTICAL;

//import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class FoodCartPage extends AppCompatActivity implements   FoodCartPageAdapter.ListItemClickListener{

    private RecyclerView mListOfCartItem;
    private FoodCartPageAdapter foodCartPageAdapter;
    private UserDatabase mdb;
    private TextView mRestaurantName, mDeliveryCharge, mVat,mSubTotal,mTotal;
    int total_price_without_vat_deliveryCharg;
    int vat,deliveryCharge;
    float total_with_vat_delivery_chrage;
    TextView addressOnMap;

    Toolbar foodToolbar;
    String ItemIds="",ItemAmounts="";
    Integer merchantId;
    String phoneNo;
    double lat;
    double lng;
    URL orderCreateUrl;
    Button orderSubmit,preOrderFood,promoApply;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart_page);
        mListOfCartItem = (RecyclerView)findViewById(R.id.recycler_cart);


        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);




        addressOnMap = (TextView) findViewById(R.id.tv_user_address_map_view);
        addressOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapView = new Intent(getApplicationContext(),UserMapActivity.class);
                finish();
                startActivity(mapView);
            }
        });

        orderSubmit = (Button) findViewById(R.id.placeOrderId);
        orderSubmit.setVisibility(View.INVISIBLE);


        preOrderFood = (Button) findViewById(R.id.preorderfoodID);
        preOrderFood.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent orderSubmitIntent = new Intent(getApplicationContext(),PreOrderTimeSelect.class);
                startActivity(orderSubmitIntent);

            }

        });

        promoApply = (Button) findViewById(R.id.btn_applyPromo);
        promoApply.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent PromoApplyIntent = new Intent(getApplicationContext(),ApplyPromoPage.class);
                startActivity(PromoApplyIntent);

            }

        });


        mdb = UserDatabase.getInstance(getApplicationContext());
        List<OrderMerchantModel> orderMerchant = mdb.orderMercahntDAO().loadOrderMerchant();
        mRestaurantName = (TextView) findViewById(R.id.tv_restaurant_name_food_cart);
        mDeliveryCharge = (TextView) findViewById(R.id.tv_delivery_charge);
        mVat = (TextView) findViewById(R.id.tv_total_vat);
        mSubTotal = (TextView) findViewById(R.id.tv_food_price);
        mTotal = (TextView) findViewById(R.id.tv_order_item_price);

        if(orderMerchant.size() !=0){
            String restaurantName = orderMerchant.get(0).getMerchantName();
            vat = orderMerchant.get(0).getVat();
            deliveryCharge = orderMerchant.get(0).getDeliveryCharge();
            mRestaurantName.setText(restaurantName);
            mDeliveryCharge.setText(String.valueOf(deliveryCharge));

        }else{
            String restaurantName = "No order found";
            mRestaurantName.setText(restaurantName);
        }


        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
       List<OrderModel> order =  mdb.orderDAO().loadOrder();//select all data form room database user table

        if(order.size()!= 0){//if data exist
            orderSubmit.setVisibility(View.VISIBLE);//if has order then visible the place order button
            orderSubmit.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {


                    Intent orderSubmitIntent = new Intent(getApplicationContext(),OrderSubmitComplete.class);
                    startActivity(orderSubmitIntent);

                }

            });
            for(int i=0;i<order.size();i++) {
                ItemIds = ItemIds + (order.get(i).getItemId() + ",");
                ItemAmounts = ItemAmounts + (order.get(i).getItemAmount() + ",");

            }
        }
       // Toast.makeText(getApplicationContext()," Item  : " + ItemIds + " amounts : " + ItemAmounts,Toast.LENGTH_LONG).show();
        List<OrderMerchantModel> mercahnt = mdb.orderMercahntDAO().loadOrderMerchant();
        if(mercahnt.size() != 0){
            merchantId = mercahnt.get(0).getMerchantId();
        }
        List<UserModel> user = mdb.userDAO().loadPhone();
        if(user.size() != 0){
            phoneNo = user.get(0).getPhone();
        }

        Intent foodCart = getIntent();
        String address = foodCart.getStringExtra("address");
        if(address!=null) addressOnMap.setText(address);
        else{
            MyLocation myLocation = new MyLocation(FoodCartPage.this);
            myLocation.setListener(new MyLocation.MyLocationListener() {
                @Override
                public void onLocationFound(Location location) {

                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    ShowLoder("Loading..");
                    VolleyRequest volleyRequest = new VolleyRequest(FoodCartPage.this);
                    volleyRequest.VolleyGet(Api.reverseGeo + "demo?lat=" + lat + "&lng=" + lng + "&address_level=UPTO_THANA");
                    volleyRequest.setListener(new VolleyRequest.MyServerListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                //((EditText) findViewById(R.id.address)).setText(response.getJSONObject("result").getString("address"));
                                String address = response.getJSONObject("result").getString("address");
                                addressOnMap.setText(address);
                                dialog.dismiss();


                            } catch (Exception e) {

                            }


                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(FoodCartPage.this, error, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void responseCode(int resposeCode) {

                        }
                    });



                }

                @Override
                public void onFailed() {

                }
            });
        }


        orderSubmit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                orderCreateUrl = NetworkUtils.buildOrderUrl();
                new OrderTask().execute(orderCreateUrl);

               // Intent orderSubmitIntent = new Intent(getApplicationContext(),OrderSubmitComplete.class);
              //  startActivity(orderSubmitIntent);

            }

        });
        //Toast.makeText(getApplicationContext(), "Mercahnt Id : " + mercahntId + " ,Client phone No : "+ phoneNo + ",Order Item : " + itemId, Toast.LENGTH_LONG).show();



        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        mListOfCartItem.setLayoutManager(layoutManager);

        //mListOfCartItem.setHasFixedSize(true);

        foodCartPageAdapter = new FoodCartPageAdapter(this,this);

        mListOfCartItem.setAdapter(foodCartPageAdapter);

        //DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
       // mListOfCartItem.addItemDecoration(decoration);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }




            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete


                int position = viewHolder.getAdapterPosition();
                List<OrderModel> orders = foodCartPageAdapter.getmOrders();
                mdb.orderDAO().deleteOrder(orders.get(position));
                View parentLayout = findViewById(R.id.snackbar_show);
                Snackbar.make(parentLayout, orders.get(position).getItemName()+ " has been deleted", Snackbar.LENGTH_SHORT)
                        .show();
                retriveOrder();

            }
        }).attachToRecyclerView(mListOfCartItem);
        mdb = UserDatabase.getInstance(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        retriveOrder();
        Intent foodCart = getIntent();
        String address = foodCart.getStringExtra("address");
        if(address!=null)
        addressOnMap.setText(address);


    }
    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }



    private void  retriveOrder(){
        final List<OrderModel> order = mdb.orderDAO().loadOrder();
        foodCartPageAdapter.setmOrders(order);
        total_price_without_vat_deliveryCharg=0;
        if(order.size() != 0){
            total_with_vat_delivery_chrage = 0;
            for(int i =0;i<order.size();i++){
                total_price_without_vat_deliveryCharg = total_price_without_vat_deliveryCharg + order.get(i).getItemPrice() * order.get(i).getItemAmount();
            }
            mSubTotal.setText(String.valueOf(total_price_without_vat_deliveryCharg));

            float totalvat = (total_price_without_vat_deliveryCharg * vat)/100;
            total_with_vat_delivery_chrage = total_price_without_vat_deliveryCharg +  totalvat +deliveryCharge;
            mVat.setText(String.valueOf(totalvat));
            mTotal.setText(String.valueOf(total_with_vat_delivery_chrage));
        }else{
            mSubTotal.setText("0.0");
            mVat.setText("0.0");
            mTotal.setText("0.0");
            mDeliveryCharge.setText("0.0");
            List<OrderMerchantModel> orderMerchantModel = mdb.orderMercahntDAO().loadOrderMerchant();
            if(orderMerchantModel.size() !=0 ){
                mdb.orderMercahntDAO().deleteOrderMerchant(orderMerchantModel.get(0));
                mRestaurantName.setText("No order found");

            }

        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {


       // List<OrderModel> order = mdb.orderDAO().loadOrder();

       // Toast.makeText(getApplicationContext(),order.get(clickedItemIndex).getItemName() +"  has been deleted from cart"  ,Toast.LENGTH_SHORT).show();

    }

   public class OrderTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String DriverResults = null;
            try {
                DriverResults = NetworkUtils.getFoodOrderFromHttpUrl(searchUrl,ItemIds,ItemAmounts,phoneNo,String.valueOf(merchantId),String.valueOf(lat),String.valueOf(lng), addressOnMap.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return DriverResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String DriverResults) {

            if (DriverResults != null && !DriverResults.equals("")) {




                String json = DriverResults;

                JSONObject driverINfo = null;


                JSONArray jsonArray ;

                String fname = null,lname=null,profilePic = null,contact = null;


                try {

                    driverINfo = new JSONObject(json);

                    jsonArray = driverINfo.getJSONArray("driver");

                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject driverProfile = jsonArray.getJSONObject(i);

                        fname = driverProfile.getString("first_name");
                        lname = driverProfile.getString("last_name");
                        profilePic = driverProfile.getString("profile_pic");
                        contact = driverProfile.getString("contact");

                        //Menus.add(menu);
                    }

                    if(fname != null){
                        List<OrderModel> orders = foodCartPageAdapter.getmOrders();
                        if(orders.size()!=0){
                            for(int i =0;i<orders.size();i++) mdb.orderDAO().deleteOrder(orders.get(i));
                        }
                        List<OrderMerchantModel> om = mdb.orderMercahntDAO().loadOrderMerchant();
                        mdb.orderMercahntDAO().deleteOrderMerchant(om.get(0));

                        Intent orderSubmitIntent = new Intent(getApplicationContext(),OrderSubmitComplete.class);
                        orderSubmitIntent.putExtra("driver_name",fname+" "+lname);
                        orderSubmitIntent.putExtra("profile_pic",profilePic);
                        orderSubmitIntent.putExtra("contact",contact);
                        startActivity(orderSubmitIntent);
                    }







                    /*mProgressbar.setVisibility(View.INVISIBLE);// set progressbar to invisible
                    ViewGroup.LayoutParams layoutParams = mProgressbar.getLayoutParams();// instantiate  layout parameter progressbar
                    layoutParams.height = 0;// set layout height to zero
                    layoutParams.width = 0;// set layout width to zero
                    mProgressbar.setLayoutParams(layoutParams);// set progressbar layout height & width*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  mSearchResultsTextView.setText(allNames.get(8));
            }else{
                Toast.makeText(getApplicationContext(), "No restaurant menu found or net connection error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
