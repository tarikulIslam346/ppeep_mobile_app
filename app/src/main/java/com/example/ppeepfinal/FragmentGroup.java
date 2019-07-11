package com.example.ppeepfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
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

    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;

    View v;

    private RecyclerView mListOfFriend;

    private CircleMenu circleMenu,circleMenu2;

    private UserDatabase mdb;

    private List<UserModel> user;

    private String phoneNo;

    private FragmentGroupAdapter mfragmentGroupAdapter;

    private ProgressBar mProgressbar;

    private TextView mProfileNameTv,mPhoneNoTv;

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


    public void callApi(){
        URL FriendListCheckUrl = NetworkUtils.buildFriendListUrl();

        new  FriendListTask().execute(FriendListCheckUrl);
    }



    public class FriendListTask extends AsyncTask<URL, Void, String> implements   FragmentGroupAdapter.ListItemClickListener {




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

                JSONArray jsonArray=null;

                String name,message = null;

                List<String> allNames = new ArrayList<String>();



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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());

                    mListOfFriend.setLayoutManager(layoutManager);

                    mListOfFriend.setHasFixedSize(true);

                    mfragmentGroupAdapter = new FragmentGroupAdapter(allNames, this);

                    mListOfFriend.setAdapter(mfragmentGroupAdapter);
                }



            }else{
                Toast.makeText(getContext(), "No restaurant", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            /*int clickedRestaurnat = MerchantId.get(clickedItemIndex).intValue();
            String restaurantName = allNames.get(clickedItemIndex);
            String cusine = Cusines.get(clickedItemIndex);
            //Toast.makeText(getContext(),"restaurant id" +clickedRestaurnat ,Toast.LENGTH_SHORT).show();
            Intent foodmenuIntent = new Intent(getContext(),RestaurantMenuPage.class);
            foodmenuIntent.putExtra("mercahnt_Id",String.valueOf(clickedRestaurnat));
            foodmenuIntent.putExtra("restaurant_name",restaurantName);
            foodmenuIntent.putExtra("cuisine",cusine);
            startActivity(foodmenuIntent);*/



        }
    }


}
