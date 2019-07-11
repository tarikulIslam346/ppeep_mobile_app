package com.example.ppeepfinal;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ppeepfinal.data.OrderMerchantModel;
import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GroupFriendAdd extends AppCompatActivity {

    EditText mUserInput;
    Button mAdd,mCancell;
    private UserDatabase mdb;
    private  String phoneNoOffriend,phoneNo;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_friend_add);
        mdb = UserDatabase.getInstance(getApplicationContext());
        mUserInput = (EditText) findViewById(R.id.userPhoneNoTextOfFriendInputEditText);
        List<UserModel> user = mdb.userDAO().loadPhone();
        if(user.size()!= 0 ){
            phoneNo = user.get(0).getPhone();
        }
        mAdd  = (Button) findViewById(R.id.bt_add_friend);
        mCancell = (Button) findViewById(R.id.bt_add_friend_cancelButton);
        mCancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mUserInput.getText().toString() != null){
                   ShowLoder("Loading ...");
                    phoneNoOffriend = mUserInput.getText().toString();
                    URL addFriendUrl = NetworkUtils.buildAddFriendUrl();
                    new FriendAddTask().execute(addFriendUrl);
                }else{
                    Toast.makeText(getApplicationContext(), "Add phone no of your friend", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
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
    public class FriendAddTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String addFriendResults = null;
            try {
                addFriendResults = NetworkUtils.getAddFriendResponseFromHttpUrl(searchUrl,phoneNo,phoneNoOffriend);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addFriendResults;
        }

        @Override
        protected void onPostExecute(String addFriendResults) {

            if (addFriendResults != null && !addFriendResults.equals("")) {

                String json = addFriendResults;
                JSONObject addFriend = null;
                String Success = null,Friend=null,Message=null;

                try {
                    addFriend = new JSONObject(json);
                    Success = addFriend.getString("Success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    addFriend = new JSONObject(json);
                    Friend = addFriend.getString("Friend");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    addFriend = new JSONObject(json);
                    Message = addFriend.getString("Message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }




                if(Friend != null){
                    AlertForAction("Friend",Friend,true);
                    dialog.dismiss();

                }
                 if(Success != null){
                     AlertForAction("Success",Success,true);
                     dialog.dismiss();
                    // finish();
                 }
                if(Message != null){
                    AlertForAction("Message",Message,false);
                    dialog.dismiss();
                }


            }else{
                Toast.makeText(getApplicationContext(), "No internet connection availabel", Toast.LENGTH_LONG).show();
            }
        }
    }
}
