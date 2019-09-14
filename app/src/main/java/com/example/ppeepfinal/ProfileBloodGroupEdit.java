package com.example.ppeepfinal;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class ProfileBloodGroupEdit extends AppCompatActivity {
    private UserDatabase mdb;
    String myPhoneNo, myBlood;
    Button userBloodGroupUpdate,cancelButton;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_blood_group_edit);
        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
        List<UserModel> user = mdb.userDAO().loadPhone();//select all data form room database user table
        userBloodGroupUpdate = (Button) findViewById(R.id.updateBloodGroup);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwithToProfile();
            }
        });
        if(user.size()!= 0){//if data exist

            myPhoneNo = user.get(0).getPhone();// set phone to text view
           // myName = user.get(0).getName();
        }

        Spinner staticSpinner = (Spinner) findViewById(R.id.blood_group_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.bloodgroup_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),""+parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                myBlood =(String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        userBloodGroupUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBlood != null){
                    ShowLoder("Updating...");
                    // userBloodGroupUpdate.setVisibility(View.INVISIBLE);
                    // cancelButton.setVisibility(View.INVISIBLE);
                    //myDob = userDobDayTextInput.getText().toString()+"/"+userDobMonthTextInput.getText().toString()+"/"+userDobYearTextInput.getText().toString();
                    URL profileUpdateUrl = NetworkUtils.buildProfileUpdateUrl();
                    new BloodGroupUpdateTask().execute(profileUpdateUrl);
                }
            }
        });




    }
    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }
    public class BloodGroupUpdateTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String profileUpdateResults = null;
            try {
                profileUpdateResults = NetworkUtils.getProfileUpdateResponseFromHttpUrl(searchUrl, myPhoneNo, "blood_group", myBlood);
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

                //userBloodGroupUpdate.setVisibility(View.VISIBLE);
                if (success != null) {

                    View parentLayout = findViewById(R.id.sb_user_blood_group);

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
                    View parentLayout = findViewById(R.id.sb_user_blood_group);
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
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                //userBloodGroupUpdate.setVisibility(View.VISIBLE);
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
