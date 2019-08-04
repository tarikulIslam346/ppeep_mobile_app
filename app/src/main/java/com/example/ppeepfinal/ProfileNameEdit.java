package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
/*import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ProfileNameEdit extends AppCompatActivity {

    private UserDatabase mdb;
    String myPhoneNo,myName;
    EditText userNameTextInput;
    Button userNameUpdate;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name_edit);
        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table
        userNameTextInput = (EditText) findViewById(R.id.userNameTextInputEditText);
        userNameUpdate = (Button) findViewById(R.id.userNameUpdateButton);
      final Button  cancelButton = (Button) findViewById(R.id.cancelButton);
        progressBar = (ProgressBar) findViewById(R.id.pv_edit_profile_name);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwithToProfile();
            }
        });

        if(user.size()!= 0){//if data exist

            myPhoneNo = user.get(0).getPhone();// set phone to text view
            myName = user.get(0).getName();
        }
        userNameTextInput.setText(myName);

        userNameUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNameTextInput.getText()!=null && !userNameTextInput.getText().equals("")){

                    if(myPhoneNo  != null){
                        //update from server
                        progressBar.setVisibility(View.VISIBLE);
                        userNameUpdate.setVisibility(View.INVISIBLE);
                        cancelButton.setVisibility(View.INVISIBLE);
                        URL profileUpdateUrl = NetworkUtils.buildProfileUpdateUrl();
                        new ProfileNameUpdateTask().execute(profileUpdateUrl);
                    }
                }else{
                    View parentLayout = findViewById(R.id.sb_user_name_error);
                    Snackbar.make(parentLayout,  " Please Enter User name", Snackbar.LENGTH_INDEFINITE)
                            .show();
                }
            }
        });



    }

    public class ProfileNameUpdateTask extends AsyncTask<URL, Void, String> {



        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String profileUpdateResults = null;
            try {
                profileUpdateResults = NetworkUtils.getProfileUpdateResponseFromHttpUrl(searchUrl,myPhoneNo,"first_name",userNameTextInput.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return profileUpdateResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String profileUpdateResults) {
            if (profileUpdateResults != null && !profileUpdateResults.equals("")) {



                String json = profileUpdateResults;
                JSONObject userInfo = null;

                String error=null,success =null;
                try {
                       userInfo = new JSONObject(json);
                       success = userInfo.getString("Success");
                       error = userInfo.getString("error");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();

                layoutParams.height = 0;
                layoutParams.width = 0;
                progressBar.setLayoutParams(layoutParams);



                if(success != null){
                    List<UserModel> user =  mdb.userDAO().loadPhone();
                    if(user.size()!= 0){
                        int Id = user.get(0).getId();
                        UserModel updateUser = mdb.userDAO().loadUserById(Id);
                        updateUser.setName(userNameTextInput.getText().toString());
                        //Toast.makeText(getApplicationContext(), ""+updateUser.getName(), Toast.LENGTH_LONG).show();
                        mdb.userDAO().updateUser(updateUser);
                    }
                    View parentLayout = findViewById(R.id.sb_user_name_error);
                    Snackbar.make(parentLayout,  ""+success, Snackbar.LENGTH_LONG)
                            .show();
                    SwithToProfile();

                }
                if(error  != null){
                    View parentLayout = findViewById(R.id.sb_user_name_error);
                    Snackbar.make(parentLayout,  ""+error, Snackbar.LENGTH_LONG)
                            .show();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                userNameUpdate.setVisibility(View.VISIBLE);
            }
        }


    }
    public void SwithToProfile(){

        Intent updateintent=new Intent(getApplicationContext(),ProfileEdit.class);
        updateintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(updateintent);
        finish();


    }
}
