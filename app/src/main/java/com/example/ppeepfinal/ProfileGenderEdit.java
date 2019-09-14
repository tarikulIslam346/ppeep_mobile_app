package com.example.ppeepfinal;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ProfileGenderEdit extends AppCompatActivity {
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button updateGender,cancelButton;
    ProgressDialog dialog;
    private UserDatabase mdb;
    private String myPhoneNo,myGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gender_edit);
        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
        List<UserModel> user = mdb.userDAO().loadPhone();//select all data form room database user table
        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        updateGender = (Button) findViewById(R.id.updateGender);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwithToProfile();
            }
        });
        if(user.size()!= 0){//if data exist
            myPhoneNo = user.get(0).getPhone();// set phone to text view

        }

        updateGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radioSexGroup.getCheckedRadioButtonId();
                radioSexButton=(RadioButton)findViewById(selectedId);
                if(radioSexButton.getText().toString() !=null){
                    myGender = radioSexButton.getText().toString();
                    ShowLoder("Updating...");
                    // userBloodGroupUpdate.setVisibility(View.INVISIBLE);
                    // cancelButton.setVisibility(View.INVISIBLE);
                    //myDob = userDobDayTextInput.getText().toString()+"/"+userDobMonthTextInput.getText().toString()+"/"+userDobYearTextInput.getText().toString();
                    URL profileUpdateUrl = NetworkUtils.buildProfileUpdateUrl();
                    new ProfileGenderUpdateTask().execute(profileUpdateUrl);

                }
                //Toast.makeText(ProfileGenderEdit.this,radioSexButton.getText(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }
    public class ProfileGenderUpdateTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String profileUpdateResults = null;
            try {
                profileUpdateResults = NetworkUtils.getProfileUpdateResponseFromHttpUrl(searchUrl, myPhoneNo, "gender", myGender);
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

                dialog.dismiss();
                if (success != null) {

                    View parentLayout = findViewById(R.id.sb_gender);
                    Snackbar snackbar = Snackbar.make(parentLayout, ""+success, Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            /* Fix it
                             * Change Action text color
                             * setActionTextColor(Color.RED)
                             * */
                            .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));

                    View sbView = snackbar.getView();

                    /* Fix it
                     * Change  text coler
                     * */
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(android.R.color.black ));

                    /* Fix it
                     * Change background  color
                     * */
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    snackbar.show();
                    SwithToProfile();
                }
                if (error != null) {
                    View parentLayout = findViewById(R.id.sb_gender);
                    Snackbar snackbar = Snackbar.make(parentLayout, ""+error, Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            /* Fix it
                             * Change Action text color
                             * setActionTextColor(Color.RED)
                             * */
                            .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));

                    View sbView = snackbar.getView();

                    /* Fix it
                     * Change  text coler
                     * */
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(android.R.color.black ));

                    /* Fix it
                     * Change background  color
                     * */
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    snackbar.show();
                }

            } else {
                dialog.dismiss();
                View parentLayout = findViewById(R.id.sb_gender);
                Snackbar snackbar = Snackbar.make(parentLayout, "Network Not Available", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        /* Fix it
                         * Change Action text color
                         * setActionTextColor(Color.RED)
                         * */
                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));

                View sbView = snackbar.getView();

                /* Fix it
                 * Change  text coler
                 * */
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(android.R.color.black ));

                /* Fix it
                 * Change background  color
                 * */
                sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                snackbar.show();
            }
        }


    }
    public void SwithToProfile() {

        Intent updateintent = new Intent(getApplicationContext(), ProfileEdit.class);
        updateintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(updateintent);
        finish();

    }
}
