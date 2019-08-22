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
import org.w3c.dom.Text;

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
    private TextView mRestaurantName, mDeliveryCharge, mVat,mSubTotal,mTotal,mDiscount;
    int total_price_without_vat_deliveryCharg;
    int vat,deliveryCharge;
    double total_with_vat_delivery_chrage,discount,promoDiscount,promoMaxDiscountAmount;
    TextView addressOnMap,tv_my_point,mPromoPercentageTextView,mPromoCodeDiscount;

    Toolbar foodToolbar;
    String ItemIds="",ItemAmounts="";
    Integer merchantId;
    String phoneNo;
    double lat;
    double lng;
    URL orderCreateUrl;
    Button orderSubmit,preOrderFood,promoApply;
    ProgressDialog dialog;
    String myPhoneNo;
    EditText mPointInput;
    Button mAddPoint;
    String promoCode,promoPercentage,promocodeMaxDiscount,preOrderDate,preOrderTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart_page);
        mdb = UserDatabase.getInstance(getApplicationContext());
        loadUserFromDb();
        mListOfCartItem = (RecyclerView)findViewById(R.id.recycler_cart);


        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);
        mPointInput = (EditText)findViewById(R.id.userPointTextInputEditText) ;
        mAddPoint = (Button)findViewById(R.id.bt_add_point) ;
        addressOnMap = (TextView) findViewById(R.id.tv_user_address_map_view);

        tv_my_point = (TextView) findViewById(R.id.tv_my_point) ;
        mAddPoint.setVisibility(View.INVISIBLE);

        List<OrderMerchantModel> orderMerchant = mdb.orderMercahntDAO().loadOrderMerchant();
        mRestaurantName = (TextView) findViewById(R.id.tv_restaurant_name_food_cart);
        mDeliveryCharge = (TextView) findViewById(R.id.tv_delivery_charge);
        mVat = (TextView) findViewById(R.id.tv_total_vat);
        mSubTotal = (TextView) findViewById(R.id.tv_food_price);
        mTotal = (TextView) findViewById(R.id.tv_order_item_price);
        mDiscount = (TextView) findViewById(R.id.tv_discount_amount);
        mPromoPercentageTextView = (TextView)findViewById(R.id.tv_discount_amount_promo_code) ;
        mPromoCodeDiscount = (TextView)findViewById(R.id.tv_discount_amount_promo_code_cash);

        discount = 0.0;
        promoDiscount = 0.0;
        promoMaxDiscountAmount=0.0;
        //mDiscount.setVisibility(View.INVISIBLE);





        orderSubmit = (Button) findViewById(R.id.placeOrderId);
        orderSubmit.setVisibility(View.INVISIBLE);


        Intent extraInputIntent = getIntent();
        promoCode = extraInputIntent.getStringExtra("promo_code");
        promoPercentage = extraInputIntent.getStringExtra("percentage");
        promocodeMaxDiscount = extraInputIntent.getStringExtra("max_discount");
        preOrderDate = extraInputIntent.getStringExtra("date");
        preOrderTime = extraInputIntent.getStringExtra("time");

        if(promoCode!=null){

        }
        if(promoCode!=null && promoPercentage!=null & promocodeMaxDiscount!=null){
            promoMaxDiscountAmount = Double.valueOf(promocodeMaxDiscount);
            promoDiscount = Double.valueOf(promoPercentage);
            mPromoPercentageTextView.setText("Discount(" +promoPercentage +"%)");
        }




        preOrderFood = (Button) findViewById(R.id.preorderfoodID);
        preOrderFood.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent orderSubmitIntent = new Intent(getApplicationContext(),PreOrderTimeSelect.class);
                finish();
                startActivity(orderSubmitIntent);

            }

        });

        promoApply = (Button) findViewById(R.id.btn_applyPromo);
        promoApply.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent PromoApplyIntent = new Intent(getApplicationContext(),ApplyPromoPage.class);
                finish();
                startActivity(PromoApplyIntent);

            }

        });






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


        //mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
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
            String address  =  user.get(0).getAddress();
            addressOnMap.setText(address);
        }



        orderSubmit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                orderCreateUrl = NetworkUtils.buildOrderUrl();
                new OrderTask().execute(orderCreateUrl);
                URL sendNotificationURL = NetworkUtils.buildUrl(NetworkUtils.SENT_DRIVER_NOTIFICATION);
                new SentNotificationTask().execute(sendNotificationURL);


            }

        });



        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        mListOfCartItem.setLayoutManager(layoutManager);

        //mListOfCartItem.setHasFixedSize(true);

        foodCartPageAdapter = new FoodCartPageAdapter(this,this);

        mListOfCartItem.setAdapter(foodCartPageAdapter);



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


        if(myPhoneNo!=null){
            ShowLoder("Loading Point ...");
            URL userPointUrl = NetworkUtils.buildUserPointUrl();
            new PointTask().execute(userPointUrl);
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        retriveOrder();
        /*Intent foodCart = getIntent();
        String address = foodCart.getStringExtra("address");
        if(address!=null)
        addressOnMap.setText(address);*/


    }
    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }
    public  void loadUserFromDb(){
        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table

        if(user.size()!= 0){//if data exist

            myPhoneNo = user.get(0).getPhone();// set phone to text view

            //myProfileName.setText(user.get(0).getName());// set name to text view
        }
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

            double totalvat = (total_price_without_vat_deliveryCharg * vat)/100;

            double promoDiscountAmount = (total_price_without_vat_deliveryCharg * promoDiscount)/100;

            if(promoDiscount!=0.0){
                if(promoMaxDiscountAmount==0.0){
                    mPromoCodeDiscount.setText(String.valueOf(promoDiscountAmount));
                }else{
                    if(promoMaxDiscountAmount < promoDiscountAmount){
                        promoDiscountAmount = promoMaxDiscountAmount;
                        mPromoCodeDiscount.setText(String.valueOf(promoMaxDiscountAmount));
                    }else{
                        mPromoCodeDiscount.setText(String.valueOf(promoDiscountAmount));
                    }

                }

            }

            total_with_vat_delivery_chrage = (total_price_without_vat_deliveryCharg +  totalvat + deliveryCharge)- (discount+promoDiscountAmount);

            mVat.setText(String.valueOf(totalvat));

            mTotal.setText(String.valueOf(total_with_vat_delivery_chrage));

        }else{
            mSubTotal.setText("0.0");
            mVat.setText("0.0");
            mTotal.setText("0.0");
            mDeliveryCharge.setText("0.0");
            mDiscount.setText("0.0");
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

    public class PointTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String UserPointResults = null;
            try {
                UserPointResults = NetworkUtils.getUserPointFromHttpUrl(searchUrl,myPhoneNo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return UserPointResults;
        }

        @Override
        protected void onPostExecute(String UserPointResults) {

            if (UserPointResults != null && !UserPointResults.equals("")) {


                String json = UserPointResults;

                JSONObject userPointInfo = null;


                JSONArray jsonArray ;

                double total_point = 0.0;

                try {

                    userPointInfo = new JSONObject(json);

                    jsonArray = userPointInfo.getJSONArray("user_earn_point");

                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject driverProfile = jsonArray.getJSONObject(i);

                        total_point = driverProfile.getDouble("total_point");

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

                if(total_point != 0.0){
                    final  double myPoint = total_point;
                    tv_my_point.setText("Your Available  Point : "+String.valueOf(total_point));
                    mAddPoint.setVisibility(View.VISIBLE);

                    mAddPoint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(     mPointInput.getText()!=null
                                    && !mPointInput.getText().equals("")
                                    && Integer.valueOf(mPointInput.getText().toString()) <= 50
                                    && Integer.valueOf(mPointInput.getText().toString()) > 0
                                    && Double.valueOf(mPointInput.getText().toString()) <= myPoint
                            ){

                               // mDiscount.setVisibility(View.VISIBLE);
                                mDiscount.setText(mPointInput.getText());
                                tv_my_point.setText("Your Available  Point : "+(myPoint- 50));
                                discount = Double.valueOf(mPointInput.getText().toString());
                                retriveOrder();

                            }else{
                                if(Double.valueOf(mPointInput.getText().toString()) > myPoint){
                                    //update from server
                                    View parentLayout = findViewById(R.id.sb_food_cart_page);
                                    Snackbar.make(parentLayout,  " Balance not available", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                }
                                            })
                                            .show();
                                }
                                if(Integer.valueOf(mPointInput.getText().toString() ) > 50){
                                    //update from server
                                    View parentLayout = findViewById(R.id.sb_food_cart_page);
                                    Snackbar.make(parentLayout,  " Please Enter less Or equal 50 point", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                }
                                            })
                                            .show();
                                }
                                if(mPointInput.getText()==null
                                        && !mPointInput.getText().equals("")){
                                    View parentLayout = findViewById(R.id.sb_food_cart_page);
                                    Snackbar.make(parentLayout,  " Please Enter point", Snackbar.LENGTH_INDEFINITE)
                                            .show();
                                }

                            }
                        }
                    });
                }
                //  mSearchResultsTextView.setText(allNames.get(8));
            }else{
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "No restaurant menu found or net connection error", Toast.LENGTH_SHORT).show();
            }
        }
    }

   public class OrderTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String DriverResults = null;
            try {
                DriverResults = NetworkUtils.getFoodOrderFromHttpUrl(
                        searchUrl,ItemIds,ItemAmounts,String.valueOf(discount),
                        phoneNo,String.valueOf(merchantId),String.valueOf(lat),String.valueOf(lng),
                        addressOnMap.getText().toString(),
                        preOrderDate,
                        preOrderTime);
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

                String fname = null,lname=null,profilePic = null,contact = null,orderId=null,message=null;


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
                    orderId = driverINfo.getString("order_data");



                    /*mProgressbar.setVisibility(View.INVISIBLE);// set progressbar to invisible
                    ViewGroup.LayoutParams layoutParams = mProgressbar.getLayoutParams();// instantiate  layout parameter progressbar
                    layoutParams.height = 0;// set layout height to zero
                    layoutParams.width = 0;// set layout width to zero
                    mProgressbar.setLayoutParams(layoutParams);// set progressbar layout height & width*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try{
                    driverINfo = new JSONObject(json);
                    orderId = driverINfo.getString("order_data");
                    message = driverINfo.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
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
                    if(orderId!=null)orderSubmitIntent.putExtra("orderId",orderId);
                    startActivity(orderSubmitIntent);
                }
                if(fname == null && orderId!=null){
                    List<OrderModel> orders = foodCartPageAdapter.getmOrders();
                    if(orders.size()!=0){
                        for(int i =0;i<orders.size();i++) mdb.orderDAO().deleteOrder(orders.get(i));
                    }
                    List<OrderMerchantModel> om = mdb.orderMercahntDAO().loadOrderMerchant();
                    mdb.orderMercahntDAO().deleteOrderMerchant(om.get(0));
                    Intent orderSubmitIntent = new Intent(getApplicationContext(),OrderSubmitComplete.class);
                    orderSubmitIntent.putExtra("orderId",orderId);
                    if(message!=null)orderSubmitIntent.putExtra("message",message);
                    startActivity(orderSubmitIntent);
                }
                //  mSearchResultsTextView.setText(allNames.get(8));
            }else{
                Toast.makeText(getApplicationContext(), "No restaurant menu found or net connection error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class SentNotificationTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String DriverResults = null;
            try {
                DriverResults = NetworkUtils.sentDriverNotificationFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return DriverResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String DriverResults) {

            if (DriverResults != null && !DriverResults.equals("")) {

                //  mSearchResultsTextView.setText(allNames.get(8));
            }else{
                Toast.makeText(getApplicationContext(), " Net connection error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
