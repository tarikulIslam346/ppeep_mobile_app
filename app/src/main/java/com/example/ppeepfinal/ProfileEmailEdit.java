package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ProfileEmailEdit extends AppCompatActivity {
    private UserDatabase mdb;
    String myPhoneNo, myName;
    EditText userEmailTextInput;
    Button userEmailUpdate;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_email_edit);
        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
        List<UserModel> user = mdb.userDAO().loadPhone();//select all data form room database user table
        userEmailTextInput = (EditText) findViewById(R.id.userEmailTextInputEditText);
        userEmailUpdate = (Button) findViewById(R.id.emailUpdateButton);
        progressBar = (ProgressBar) findViewById(R.id.pv_edit_profile_name);

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);



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

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        userEmailTextInput.setText(email);

        userEmailUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEmailTextInput.getText() != null && !userEmailTextInput.getText().equals("")) {

                    if (myPhoneNo != null) {
                        //update from server
                        progressBar.setVisibility(View.VISIBLE);
                        userEmailUpdate.setVisibility(View.INVISIBLE);
                        cancelButton.setVisibility(View.INVISIBLE);
                        URL profileUpdateUrl = NetworkUtils.buildProfileUpdateUrl();
                        new ProfileNameUpdateTask().execute(profileUpdateUrl);
                    }
                } else {
                    View parentLayout = findViewById(R.id.sb_user_name_error);
                    Snackbar.make(parentLayout, " Please Enter User name", Snackbar.LENGTH_INDEFINITE)
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
                profileUpdateResults = NetworkUtils.getProfileUpdateResponseFromHttpUrl(searchUrl, myPhoneNo, "email", userEmailTextInput.getText().toString());
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

                String error = null, success = null;
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
                userEmailUpdate.setVisibility(View.VISIBLE);
                if (success != null) {

                    View parentLayout = findViewById(R.id.sb_user_name_error);
                    Snackbar.make(parentLayout, "" + success, Snackbar.LENGTH_LONG)
                            .show();
                    SwithToProfile();
                }
                if (error != null) {
                    View parentLayout = findViewById(R.id.sb_user_name_error);
                    Snackbar.make(parentLayout, "" + error, Snackbar.LENGTH_LONG)
                            .show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                userEmailUpdate.setVisibility(View.VISIBLE);
            }
        }


    }
    public void SwithToProfile() {

        Intent updateintent = new Intent(getApplicationContext(), ProfileEdit.class);
        startActivity(updateintent);

    }
}
