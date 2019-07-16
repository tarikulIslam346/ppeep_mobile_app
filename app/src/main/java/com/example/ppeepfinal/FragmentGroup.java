package com.example.ppeepfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hitomi.cmlibrary.CircleMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FragmentGroup extends Fragment {

    View v;

    private RecyclerView mListOfFriend;

    private CircleMenu circleMenu,circleMenu2;

    private UserDatabase mdb;

    private List<UserModel> user;

    private String phoneNo;

    private FragmentGroupAdapter mfragmentGroupAdapter;

    private ProgressBar mProgressbar;

    private TextView mProfileNameTv,mPhoneNoTv,mGropuMemberNo,mTotalPointOfUser;

    public FragmentGroup() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.group_fragment,container,false);

        FloatingActionButton friendAddButton= (FloatingActionButton) v.findViewById(R.id.fabGroupAdd);

        mListOfFriend = (RecyclerView)v.findViewById(R.id.rv_list_of_friend_group);

        mProgressbar = (ProgressBar) v.findViewById(R.id.pv_friend_list);

        mProfileNameTv = (TextView) v.findViewById(R.id.tv_profile_name_of_friend_group);

        mPhoneNoTv = (TextView) v.findViewById(R.id.tv_phone_no_of_friend_group);

        mGropuMemberNo = (TextView) v.findViewById(R.id.tv_group_member_no);

        mTotalPointOfUser = (TextView) v.findViewById(R.id.tv_total_point_of_user);

        mTotalPointOfUser.setText("0");

        mGropuMemberNo.setText("0");


        friendAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent friendAddIntent= new Intent(getActivity(),GroupFriendAdd.class);
             startActivity(friendAddIntent);
            }
        });

        mdb = UserDatabase.getInstance(getContext());

        user =  mdb.userDAO().loadPhone();

        if(user.size() != 0){

            phoneNo = user.get(0).getPhone();

            mProfileNameTv.setText(user.get(0).getName());

            mPhoneNoTv.setText(phoneNo);
        }

        mProgressbar.setVisibility(View.VISIBLE);

        callApi();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();

    }

    @Override
    public void onPause() {
        super.onPause();
        callApi();
    }


    public void callApi(){
        URL FriendListCheckUrl = NetworkUtils.buildFriendListUrl();

        new  FriendListTask().execute(FriendListCheckUrl);
    }



    public class FriendListTask extends AsyncTask<URL, Void, String> implements   FragmentGroupAdapter.ListItemClickListener {


        List<String> allNames = new ArrayList<String>();
        List<String> allPhoneNo = new ArrayList<String>();
        List<Double>allTotalPoint = new ArrayList<Double>();

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];

            String friendListResults = null;

            try {

                friendListResults = NetworkUtils.getFriendListResponseFromHttpUrl(searchUrl,phoneNo);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return friendListResults;
        }

        @Override
        protected void onPostExecute(String friendListResults) {

            if (friendListResults != null && !friendListResults.equals("")) {

                String json = friendListResults;

                JSONObject friendList = null;

                JSONArray jsonArray=null,jsonArrayOfTotalPoint=null;

                String name,message = null,contact = null;
                double total_point,total_point_of_user=0;

               // List<String> allNames = new ArrayList<String>();



                try {
                    friendList = new JSONObject(json);
                    jsonArray = friendList.getJSONArray("friend_list");


                    if(jsonArray.length() == 0 ){
                        message = friendList.getString("message");
                    }

                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject friend = jsonArray.getJSONObject(i);

                        name = friend.getString("first_name");

                        allNames.add(name);

                        contact = friend.getString("contact");

                        allPhoneNo.add(contact);

                        total_point = friend.getDouble("total_point");

                        allTotalPoint.add(total_point);
                    }

                    jsonArrayOfTotalPoint = friendList.getJSONArray("total_earn_of_user");
                    for (int i=0; i<jsonArrayOfTotalPoint.length(); i++) {

                        JSONObject friend = jsonArrayOfTotalPoint.getJSONObject(i);

                        total_point_of_user = friend.getDouble("total_point");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mProgressbar.setVisibility(View.INVISIBLE);

                ViewGroup.LayoutParams layoutParams = mProgressbar.getLayoutParams();

                layoutParams.height = 0;

                layoutParams.width = 0;

                mProgressbar.setLayoutParams(layoutParams);

                if(jsonArray.length() == 0 ){
                   // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                }else{

                    mGropuMemberNo.setText(String.valueOf(jsonArray.length()));

                    if(total_point_of_user!=0)mTotalPointOfUser.setText(String.valueOf(total_point_of_user));

                    LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());

                    mListOfFriend.setLayoutManager(layoutManager);

                    mListOfFriend.setHasFixedSize(true);

                    mfragmentGroupAdapter = new FragmentGroupAdapter(allNames,allTotalPoint, this);

                    mListOfFriend.setAdapter(mfragmentGroupAdapter);
                }



            }else{
                Toast.makeText(getContext(), "No restaurant", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            String phoneNo = allPhoneNo.get(clickedItemIndex);
            Intent freindPointIntent = new Intent(getContext(),FriendHistoryDetails.class);
            freindPointIntent.putExtra("contact",phoneNo);
            startActivity(freindPointIntent);



        }
    }


}
