package com.example.ppeepfinal;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.example.ppeepfinal.data.OrderMerchantModel;
import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserDatabase;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class FoodCartPage extends AppCompatActivity  implements   FoodCartPageAdapter.ListItemClickListener{

    private RecyclerView mListOfCartItem;
    private FoodCartPageAdapter foodCartPageAdapter;
    private UserDatabase mdb;
    private TextView mRestaurantName, mDeliveryCharge, mVat,mSubTotal,mTotal;
    int total_price_without_vat_deliveryCharg;
    int vat,deliveryCharge;
    float total_with_vat_delivery_chrage;

    Toolbar foodToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart_page);
        mListOfCartItem = (RecyclerView)findViewById(R.id.recycler_cart);


        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);

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


       // mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
       /* List<OrderModel> order =  mdb.orderDAO().loadOrder();//select all data form room database user table
        OrderItem = new ArrayList<String>();
        OrderItemPrice = new ArrayList<Integer>();
        if(order.size()!= 0){//if data exist
            for(int i=0;i<order.size();i++) {
                OrderItem.add(order.get(i).getItemName());// set order item name
                itemId = itemId + (order.get(i).getItemId() + ",");

                OrderItemPrice.add(order.get(i).getItemPrice());// set order item price

            }
        }
        List<OrderMerchantModel> mercahnt = mdb.orderMercahntDAO().loadOrderMerchant();
        if(mercahnt.size() != 0){
            mercahntId = mercahnt.get(0).getMerchantId();
        }
        List<UserModel> user = mdb.userDAO().loadPhone();
        if(user.size() != 0){
            phoneNo = user.get(0).getPhone();
        }*/
        //Toast.makeText(getApplicationContext(), "Mercahnt Id : " + mercahntId + " ,Client phone No : "+ phoneNo + ",Order Item : " + itemId, Toast.LENGTH_LONG).show();



        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        mListOfCartItem.setLayoutManager(layoutManager);

        //mListOfCartItem.setHasFixedSize(true);

        foodCartPageAdapter = new FoodCartPageAdapter(this,this);

        mListOfCartItem.setAdapter(foodCartPageAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mListOfCartItem.addItemDecoration(decoration);

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
                Snackbar.make(parentLayout, orders.get(position).getItemName()+ " has been deleted", Snackbar.LENGTH_LONG)
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
    }

    private void  retriveOrder(){
        final List<OrderModel> order = mdb.orderDAO().loadOrder();
        foodCartPageAdapter.setmOrders(order);
        total_price_without_vat_deliveryCharg=0;
        if(order.size() != 0){
            total_with_vat_delivery_chrage = 0;
            for(int i =0;i<order.size();i++){
                total_price_without_vat_deliveryCharg = total_price_without_vat_deliveryCharg + order.get(i).getItemPrice();
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

   /* public class RestaurantMenuListTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String RestaurantMenuResults = null;
            try {
                RestaurantMenuResults = NetworkUtils.getRestaurantMenuFromHttpUrl(searchUrl,merchantId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return RestaurantMenuResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String RestaurantMenuResults) {

            if (RestaurantMenuResults != null && !RestaurantMenuResults.equals("")) {




                String json = RestaurantMenuResults;

                JSONObject restaurantMenuList = null;

                JSONObject restaurantFoodMenuList = null;

                JSONArray jsonArray ,jsonFoodMenuArray;

                String menu,foodMenuItem;


                try {

                    restaurantMenuList = new JSONObject(json);

                    jsonArray = restaurantMenuList.getJSONArray("menu");

                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject restaurant = jsonArray.getJSONObject(i);

                        menu = restaurant.getString("category_name");

                        Menus.add(menu);
                    }








                    /*mProgressbar.setVisibility(View.INVISIBLE);// set progressbar to invisible
                    ViewGroup.LayoutParams layoutParams = mProgressbar.getLayoutParams();// instantiate  layout parameter progressbar
                    layoutParams.height = 0;// set layout height to zero
                    layoutParams.width = 0;// set layout width to zero
                    mProgressbar.setLayoutParams(layoutParams);// set progressbar layout height & width*/


               /* } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  mSearchResultsTextView.setText(allNames.get(8));
            }else{
                Toast.makeText(getApplicationContext(), "No restaurant menu found or net connection error", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}
