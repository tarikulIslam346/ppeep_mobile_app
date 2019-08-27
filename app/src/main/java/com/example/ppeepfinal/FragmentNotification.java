package com.example.ppeepfinal;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.adapter.FragmentNotificationAdapter;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.smarteist.autoimageslider.SliderLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentNotification extends Fragment {

    SliderLayout sliderLayout;
    View v;
    private  RecyclerView mNumberOfNotification;
    UserDatabase mdb;
    private  String phoneNo;
    private ProgressDialog dialog;
    private FragmentNotificationAdapter fragmentNotificationAdapter;
    private TextView mNoNotificationText;
    private  ImageView mNoNotificationImage;

    public FragmentNotification() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.notification_fragment, container, false);

        mdb = UserDatabase.getInstance(getContext());

        mNumberOfNotification = (RecyclerView)v.findViewById(R.id.rv_notification);

        mNoNotificationText = (TextView) v.findViewById(R.id.no_notification_txt) ;

        mNoNotificationImage = (ImageView) v.findViewById(R.id.no_notification_img);



        List<UserModel> user =  mdb.userDAO().loadPhone();

        if(user.size()!=0){

            phoneNo = user.get(0).getPhone();

            ShowLoder("Loading Notification....","Notification");

            URL userNotificationUrl = NetworkUtils.buildUserNotificationUrl();

            new NotificationListTask().execute(userNotificationUrl);
        }


        return v;
    }
    public void ShowLoder(String message,String title){

        dialog = ProgressDialog.show(getContext(), title, message, true);
    }

    public class  NotificationListTask extends AsyncTask<URL, Void, String> implements   FragmentNotificationAdapter.ListItemClickListener {

        List<String> allNotificationText = new ArrayList<String>();
        List<String> allNotificationImg = new ArrayList<String>();
        List<String> allNotifcationDate = new ArrayList<String>();



        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String userNotificationResults = null;
            try {
                userNotificationResults = NetworkUtils.getUserNotificationResponseFromHttpUrl(searchUrl,phoneNo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return userNotificationResults;
        }
        @Override
        protected void onPostExecute(String userNotificationResults) {

            if (userNotificationResults != null && !userNotificationResults.equals("")) {

                String json = userNotificationResults;

                JSONObject notificationList = null;

                JSONArray jsonArray=null;


                String notification_image=null,notification_text=null,notification_date=null;



                try {
                    notificationList = new JSONObject(json);
                    jsonArray = notificationList.getJSONArray("notification");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject restaurant = jsonArray.getJSONObject(i);

                        notification_text = restaurant.getString("notification_text");
                        notification_image = restaurant.getString("notification_image");
                        notification_date = restaurant.getString("created_at");


                        allNotificationText.add(notification_text);
                        allNotificationImg.add(notification_image);
                        allNotifcationDate.add(notification_date);



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

                    mNoNotificationText.setVisibility(View.INVISIBLE);

                    ViewGroup.LayoutParams layoutParamsOfText = mNoNotificationText.getLayoutParams();

                    layoutParamsOfText.height = 0;

                    layoutParamsOfText.width = 0;

                    mNoNotificationText.setLayoutParams(layoutParamsOfText);

                    mNoNotificationImage.setVisibility(View.INVISIBLE);

                    ViewGroup.LayoutParams layoutParamsofImage = mNoNotificationText.getLayoutParams();

                    layoutParamsofImage.height = 0;

                    layoutParamsofImage.width = 0;
                    mNoNotificationImage.setLayoutParams(layoutParamsofImage);



                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

                    mNumberOfNotification.setLayoutManager(layoutManager);

                    mNumberOfNotification.setHasFixedSize(true);

                    fragmentNotificationAdapter = new FragmentNotificationAdapter(allNotificationText,allNotificationImg,allNotifcationDate,  this);

                    mNumberOfNotification.setAdapter(fragmentNotificationAdapter);
                }





            }else{
                dialog.dismiss();
                Toast.makeText(getContext(), "No network not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            /*int clickedOrderId = OrderId.get(clickedItemIndex).intValue();
            Intent intent = new Intent(getContext(),CurrentOrderHistory.class);
            intent.putExtra("order_id",String.valueOf(clickedOrderId));
            startActivity(intent);*/

        }


    }

}



