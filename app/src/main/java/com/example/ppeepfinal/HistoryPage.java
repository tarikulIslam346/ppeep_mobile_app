package com.example.ppeepfinal;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HistoryPage extends AppCompatActivity {

    ProgressDialog dialog;
    UserDatabase mdb;
    String phoneNo;
    private HistoryPageAdapter historyPageAdapter;
    private RecyclerView mNumberOfHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history_page);

        mNumberOfHistory = (RecyclerView)findViewById(R.id.historyrecycle);

        mdb = UserDatabase.getInstance(getApplicationContext());

        List<UserModel> user =  mdb.userDAO().loadPhone();

        if(user.size()!=0){

            phoneNo = user.get(0).getPhone();

            ShowLoder("Loading .. ..");

            URL currentOrderInfoUrl = NetworkUtils.buildAllOrderInfoUrl();

            new CurrentOrderListTask().execute(currentOrderInfoUrl);
        }

    }
    public void ShowLoder(String message){

        dialog = ProgressDialog.show(this, "", message, true);
    }

    public class  CurrentOrderListTask extends AsyncTask<URL, Void, String> implements   HistoryPageAdapter.ListItemClickListener {

        List<String> allNames = new ArrayList<String>();
        List<Integer> OrderId = new ArrayList<Integer>();
        List<String> OrderDate = new ArrayList<String>();
        List<Integer> OrderStatus = new ArrayList<Integer>();
        List<String> Logo = new ArrayList<String>();



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
        @Override
        protected void onPostExecute(String currentOrderResults) {

            if (currentOrderResults != null && !currentOrderResults.equals("")) {

                String json = currentOrderResults;

                JSONObject orderList = null;

                JSONArray jsonArray=null;

                int orderID,order_status;

                String orderCreated=null,restaurant_name=null,logo=null;



                try {
                    orderList = new JSONObject(json);
                    jsonArray = orderList.getJSONArray("order");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject restaurant = jsonArray.getJSONObject(i);


                        orderID = restaurant.getInt("order_id");
                        restaurant_name = restaurant.getString("restaurant_name");
                        orderCreated = restaurant.getString("created_at");
                        order_status = restaurant.getInt("order_status");
                        logo = restaurant.getString("logo");


                        OrderId.add(orderID);
                        allNames.add(restaurant_name);
                        OrderDate.add(orderCreated);
                        OrderStatus.add(order_status);
                        Logo.add(logo);



                        //Toast.makeText(HistoryPage.this, "Click", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(getApplicationContext(),CurrentOrderHistory.class);
                        intent.putExtra("order_id",String.valueOf(order));
                        startActivity(intent);*/


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();


               /*if(message != null ){
                    Snackbar.make(v.findViewById(R.id.layout_nearby), " "+message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }*/

                if(jsonArray!= null){
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    mNumberOfHistory.setLayoutManager(layoutManager);

                    mNumberOfHistory.setHasFixedSize(true);

                    historyPageAdapter = new HistoryPageAdapter(OrderId,allNames,OrderDate,OrderStatus,Logo,  this);

                    mNumberOfHistory.setAdapter(historyPageAdapter);
                }





            }else{
                Toast.makeText(getApplicationContext(), "No network not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            int clickedOrderId = OrderId.get(clickedItemIndex).intValue();
            Intent intent = new Intent(getApplicationContext(),CurrentOrderHistory.class);
            intent.putExtra("order_id",String.valueOf(clickedOrderId));
            startActivity(intent);

        }


    }
}
