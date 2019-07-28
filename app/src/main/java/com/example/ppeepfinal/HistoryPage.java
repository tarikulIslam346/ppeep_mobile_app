package com.example.ppeepfinal;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HistoryPage extends AppCompatActivity {
    //List<Integer> OrderId;
    ProgressDialog dialog;
    UserDatabase mdb;
    String phoneNo;
    TableLayout stk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        mdb = UserDatabase.getInstance(getApplicationContext());
        List<UserModel> user =  mdb.userDAO().loadPhone();
        stk = (TableLayout) findViewById(R.id.table_of_item);
        if(user.size()!=0){
            phoneNo = user.get(0).getPhone();
            ShowLoder("Loading .. ..");
            URL currentOrderInfoUrl = NetworkUtils.buildAllOrderInfoUrl();
            new CurrentOrderListTask().execute(currentOrderInfoUrl);
        }
        //stk = (TableLayout) findViewById(R.id.table_of_item);
    }
    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }

    public class  CurrentOrderListTask extends AsyncTask<URL, Void, String> {


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
                double total_with_discount=0.0;
                String orderCreated=null;
                //OrderId = new ArrayList<Integer>();


                try {
                    orderList = new JSONObject(json);
                    jsonArray = orderList.getJSONArray("order");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject restaurant = jsonArray.getJSONObject(i);
                        TableRow tbrow = new TableRow(getApplicationContext());

                        TextView t1v = new TextView(getApplicationContext());
                        orderID = restaurant.getInt("order_id");
                        t1v.setText("# "+orderID);
                        t1v.setTextSize(20);
                        tbrow.addView(t1v);


                        TextView t2v = new TextView(getApplicationContext());
                        total_with_discount = restaurant.getDouble("total_with_discount");
                        t2v.setText(total_with_discount+" BDT ");
                        t2v.setTextSize(20);
                        tbrow.addView(t2v);

                        TextView t3v = new TextView(getApplicationContext());
                        orderCreated = restaurant.getString("created_at");
                        t3v.setText(orderCreated);
                        t3v.setTextSize(20);
                        tbrow.addView(t3v);

                        final int order= orderID;

                        tbrow.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(HistoryPage.this, "Click", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),CurrentOrderHistory.class);
                                intent.putExtra("order_id",String.valueOf(order));
                                startActivity(intent);
                            }
                        });



                        stk.addView(tbrow);
                       // OrderId.add(orderID);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();





            }else{
                Toast.makeText(getApplicationContext(), "No network not available", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
