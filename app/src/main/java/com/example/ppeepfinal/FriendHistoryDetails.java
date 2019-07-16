package com.example.ppeepfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FriendHistoryDetails extends AppCompatActivity {

    String phone;
    TextView mName,mPhone;
    ProgressDialog dialog;
    Button mRemove;
    TableLayout orderDeliverTable;
    private static final String TAG = "FriendHistoryDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_history_details);
        mName = (TextView)findViewById(R.id.tv_friend_name);
        mPhone = (TextView)findViewById(R.id.tv_friend_phone);
        mRemove = (Button) findViewById(R.id.bt_remove_friend);
        orderDeliverTable = (TableLayout) findViewById(R.id.table_of_order_item);

        Intent friendHistoryIntent = getIntent();
        phone = friendHistoryIntent.getStringExtra("contact");
        if(phone !=null){
            //ShowLoder("Loding Friend Info...");
            callApi();
            ShowLoder("Loding Order history...");
            callDeliverOrder();

        }



        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowLoder("Deleting Friend Info...");
                callApiForRemoveFriend();
            }
        });


    }

    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }
    public void callApi(){
        URL profileInfoCheckUrl = NetworkUtils.buildProfileDetailInfoUrl();

        new ProfileInfoTask().execute(profileInfoCheckUrl);


    }
    public void callDeliverOrder(){
        URL orderDeliverUrl = NetworkUtils.buildDeliverOrderInfoUrl();

        new OrderDeliverHistoryTask().execute(orderDeliverUrl);
    }
    public void callApiForRemoveFriend(){
        URL removeFriendUrl= NetworkUtils.buildRemoveFriendUrl();

        new RemoveFriendTask().execute(removeFriendUrl);
    }

    public void AlertForAction(String title,String Message,Boolean finish){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(Message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(finish == true)finish();
                    }
                });
        alertDialog.show();
    }
    public class ProfileInfoTask extends AsyncTask<URL, Void, String>  {




        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];

            String userInfoResults = null;

            try {

                userInfoResults = NetworkUtils.getProfileDetailResponseFromHttpUrl(searchUrl,phone);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return userInfoResults;
        }

        @Override
        protected void onPostExecute(String userInfoResults) {

            if (userInfoResults != null && !userInfoResults.equals("")) {

                String json = userInfoResults;

                JSONObject friendList = null;

                JSONArray jsonArray=null;

                String name = null,contact = null,message = null;

                List<String> allNames = new ArrayList<String>();

                try {
                    friendList = new JSONObject(json);
                    jsonArray = friendList.getJSONArray("user");


                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject friend = jsonArray.getJSONObject(i);
                        name = friend.getString("first_name");
                        contact = friend.getString("contact");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mName.setText(name);
                mPhone.setText(contact);






            }else{
                Toast.makeText(getApplicationContext(), "No restaurant", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class OrderDeliverHistoryTask extends AsyncTask<URL, Void, String>  {

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];

            String deliverOrderResults = null;

            try {

                deliverOrderResults = NetworkUtils.getDeliverOrderDetailsOfUserResponseFromHttpUrl(searchUrl,phone);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return deliverOrderResults;
        }

        @Override
        protected void onPostExecute(String deliverOrderResults) {

            if (deliverOrderResults != null && !deliverOrderResults.equals("")) {

                String json = deliverOrderResults;

                JSONObject deliverOrderList ;

                JSONArray deliverOrderListArray;

                String orderDate = null,message = null;

                double totalAmount = 0.0,earn_point = 0.0;

                List<String> allNames = new ArrayList<String>();

                try {
                    deliverOrderList = new JSONObject(json);

                    deliverOrderListArray = deliverOrderList.getJSONArray("order");

                    for (int i=0; i<deliverOrderListArray.length(); i++) {

                        TableRow tbrow = new TableRow(getApplicationContext());

                        JSONObject orderDeliver = deliverOrderListArray.getJSONObject(i);

                        orderDate = orderDeliver.getString("created_at");

                        if(i==0) Toast.makeText(getApplicationContext(), "Ok "+orderDate, Toast.LENGTH_SHORT).show();

                        TextView t1v = new TextView(getApplicationContext());

                        t1v.setText(orderDate);

                        t1v.setGravity(Gravity.CENTER);

                        t1v.setTextColor(Color.BLACK);

                        tbrow.addView(t1v);

                        totalAmount = orderDeliver.getDouble("total_with_discount");

                        TextView t2v = new TextView(getApplicationContext());

                        t2v.setText(String.valueOf(totalAmount));

                        t2v.setGravity(Gravity.CENTER);

                        t2v.setTextColor(Color.BLACK);

                        tbrow.addView(t2v);

                        earn_point = orderDeliver.getDouble("earn_point");

                        TextView t3v = new TextView(getApplicationContext());

                        t3v.setText(String.valueOf(earn_point));

                        t3v.setGravity(Gravity.CENTER);

                        t3v.setTextColor(Color.GREEN);

                        tbrow.addView(t3v);

                        orderDeliverTable.addView(tbrow);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();






            }else{
                Toast.makeText(getApplicationContext(), "No restaurant", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class RemoveFriendTask extends AsyncTask<URL, Void, String>  {




        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];

            String removeFriendResults = null;

            try {

                removeFriendResults = NetworkUtils.getRemoveFriendResponseFromHttpUrl(searchUrl,mPhone.getText().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return removeFriendResults;
        }

        @Override
        protected void onPostExecute(String removeFriendResults) {

            if (removeFriendResults != null && !removeFriendResults.equals("")) {

                String json = removeFriendResults;

                JSONObject friendRemove = null;


                String Success = null,contact = null,message = null;


                try {
                    friendRemove = new JSONObject(json);
                    Success = friendRemove.getString("Success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                if(Success != null){
                    AlertForAction("Success",Success,true);
                }






            }else{
                Toast.makeText(getApplicationContext(), "No restaurant", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
