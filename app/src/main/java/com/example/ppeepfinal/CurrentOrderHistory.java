package com.example.ppeepfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppeepfinal.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CurrentOrderHistory extends AppCompatActivity {

    String OrderId;
    TextView mOredrRestaurantName,mOrderrestaurnatAddress,mCustomerName,mCustomerPhoneNo,mDeliveryAddress,mSubTotal,mDeliveryCharge,mVat,mTotal,mDiscount;
    Button mDriverConfirm,mOrderDeliver;
    TableLayout stk;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_history);


        mOredrRestaurantName = (TextView) findViewById(R.id.tv_order_restaurant_name);

        mOrderrestaurnatAddress = (TextView) findViewById(R.id.tv_order_restaurant_address);

        mCustomerName = (TextView) findViewById(R.id.tv_customer_name);

        mCustomerPhoneNo = (TextView) findViewById(R.id.tv_customer_phone);

        mDeliveryAddress = (TextView) findViewById(R.id.tv_delivery_address);

        // mOrderItem = (TextView)findViewById()


        mSubTotal = (TextView) findViewById(R.id.tv_total_food_price) ;

        mDeliveryCharge = (TextView) findViewById(R.id.tv_delivery_charge);

        mVat = (TextView) findViewById(R.id.tv_total_vat);

        mTotal = (TextView) findViewById(R.id.tv_total_price);

        mDiscount = (TextView)findViewById(R.id.tv_discount_amount);



        mOredrRestaurantName.setVisibility(View.INVISIBLE);

        mOrderrestaurnatAddress.setVisibility(View.INVISIBLE);

        mCustomerName.setVisibility(View.INVISIBLE);

        mCustomerPhoneNo.setVisibility(View.INVISIBLE);

        mDeliveryAddress.setVisibility(View.INVISIBLE);

        // mOrderItem.setVisibility(View.INVISIBLE);

        mSubTotal.setVisibility(View.INVISIBLE);

        mDeliveryCharge.setVisibility(View.INVISIBLE);

        mDiscount.setVisibility(View.INVISIBLE);

        Intent orderItemFromCustomerIntent = getIntent();

        OrderId = orderItemFromCustomerIntent.getStringExtra("order_id");

        if(OrderId != null){
            ShowLoder("Loding .. .. ..");
            URL orderInfoUrl = NetworkUtils.buildOrderInfoUrl();
            new OrderDetailsTask().execute(orderInfoUrl);
        }




        stk = (TableLayout) findViewById(R.id.table_of_item);

    }
    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }
    public class OrderDetailsTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];

            String orderInfoResults = null;

            try {

                orderInfoResults = NetworkUtils.getOrderDetailsResponseFromHttpUrl(searchUrl,OrderId);

            } catch (IOException e) {

                e.printStackTrace();
            }
            return orderInfoResults ;
        }

        @Override
        protected void onPostExecute(String orderInfoResults) {

            if (orderInfoResults != null && !orderInfoResults.equals("")) {

                String json = orderInfoResults;

                JSONObject orderInfo = null;

                JSONArray jsonArray ,orderDetailsJsonArray;

                String restaurant_name = null,
                        street=null,city = null,
                        state = null,
                        delivery_address = null,
                        first_name = null,
                        contact=null,
                        item_name = null;
                int quantity=0,discount_amount = 0;
                double delivery_charge=0,total_per_item = 0,total_without_vat=0,total_vat=0,unit_price=0,total_with_discount=0;
                List<String> item = new ArrayList<String>();





                try {

                    orderInfo = new JSONObject(json);



                    jsonArray = orderInfo.getJSONArray("order");

                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject driverProfile = jsonArray.getJSONObject(i);

                        /*********** customer info**********/
                        first_name = driverProfile.getString("first_name");

                        contact = driverProfile.getString("contact");

                        delivery_address = driverProfile.getString("delivery_address");

                        /*********** customer info**********/


                        /*********** restaurant info**********/
                        restaurant_name = driverProfile.getString("restaurant_name");

                        street = driverProfile.getString("street");

                        city = driverProfile.getString("city");

                        state = driverProfile.getString("state");

                        delivery_charge = driverProfile.getDouble("delivery_charge");

                        total_without_vat = driverProfile.getDouble("total_without_vat");

                        total_vat = driverProfile.getDouble("total_vat");

                        discount_amount = driverProfile.getInt("discount_amount");

                        total_with_discount = driverProfile.getDouble("total_with_discount");
                        /*********** restaurant info**********/
                    }
                    orderDetailsJsonArray = orderInfo.getJSONArray("order_details");



                    for (int i=0; i<orderDetailsJsonArray.length(); i++) {

                        JSONObject driverProfile = orderDetailsJsonArray.getJSONObject(i);
                        TableRow tbrow = new TableRow(getApplicationContext());

                        /*********** food item info**********/
                        item_name = driverProfile.getString("item_name");
                        TextView t1v = new TextView(getApplicationContext());
                        t1v.setText(item_name);
                        tbrow.addView(t1v);

                        quantity = driverProfile.getInt("amount");

                        TextView t2v = new TextView(getApplicationContext());
                        t2v.setText(String.valueOf(quantity));
                        t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);

                        unit_price = driverProfile.getDouble("unit_price");
                        TextView t3v = new TextView(getApplicationContext());
                        t3v.setText(String.valueOf(unit_price));
                        t3v.setGravity(Gravity.CENTER);
                        tbrow.addView(t3v);

                        total_per_item = driverProfile.getDouble("total_per_item");
                        TextView t4v = new TextView(getApplicationContext());
                        t4v.setText(String.valueOf(total_per_item));
                        t4v.setGravity(Gravity.CENTER);
                        tbrow.addView(t4v);
                        stk.addView(tbrow);

                        /*********** food item info**********/


                    }

                    if(total_without_vat != 0){
                        mSubTotal.setText(String.valueOf(total_without_vat));
                        mSubTotal.setVisibility(View.VISIBLE);
                    }
                    if(delivery_charge !=0){
                        mDeliveryCharge.setText(String.valueOf(delivery_charge));
                        mDeliveryCharge.setVisibility(View.VISIBLE);
                    }
                    if(total_vat!=0){
                        mVat.setText(String.valueOf(total_vat));
                        mVat.setVisibility(View.VISIBLE);
                    }

                    if(total_with_discount!=0){
                        mTotal.setText(String.valueOf(total_with_discount));
                        mTotal.setVisibility(View.VISIBLE);
                    }

                    mDiscount.setText(String.valueOf(discount_amount));
                    mDiscount.setVisibility(View.VISIBLE);



                    if(first_name != null){
                        mCustomerName.setText(first_name);
                        mCustomerName.setVisibility(View.VISIBLE);
                    }
                    if(contact != null){
                        mCustomerPhoneNo.setText(contact);
                        mCustomerPhoneNo.setVisibility(View.VISIBLE);
                    }
                    if(delivery_address != null){
                        mDeliveryAddress.setText(delivery_address);
                        mDeliveryAddress.setVisibility(View.VISIBLE);
                    }
                    if(restaurant_name != null){
                        mOredrRestaurantName.setText(restaurant_name);
                        mOredrRestaurantName.setVisibility(View.VISIBLE);
                    }
                    if(street != null && state != null && city!=null){
                        mOrderrestaurnatAddress.setText(street+" "+ city +" "+ state);
                        mOrderrestaurnatAddress.setVisibility(View.VISIBLE);
                    }
                    dialog.dismiss();





                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }else {

                dialog.dismiss();

                Toast.makeText(getApplicationContext()," Net connection error ",Toast.LENGTH_LONG).show();
            }
        }
    }
}
