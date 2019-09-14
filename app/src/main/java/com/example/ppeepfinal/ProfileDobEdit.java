package com.example.ppeepfinal;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class ProfileDobEdit extends AppCompatActivity {

    EditText userDobDayTextInput,userDobMonthTextInput,userDobYearTextInput;
    private UserDatabase mdb;
    String myPhoneNo,myDob;
    Button userDobUpdate,cancelButton;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dob_edit);
        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table
        userDobDayTextInput = (EditText) findViewById(R.id.userDobDayTextInputField);
        userDobMonthTextInput = (EditText) findViewById(R.id.userDobMonthTextInputField);
        userDobYearTextInput = (EditText) findViewById(R.id.userDobYearTextInputField);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        userDobUpdate = (Button) findViewById(R.id.userDobUpdate);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwithToProfile();
            }
        });

        if(user.size()!= 0){//if data exist

            myPhoneNo = user.get(0).getPhone();// set phone to text view
        }
        //userNameTextInput.setText(myName);
        Intent intent = getIntent();
        String day = intent.getStringExtra("day");
        String month = intent.getStringExtra("month");
        String year = intent.getStringExtra("year");
        if(day!=null)userDobDayTextInput.setText(day);
        if(month!=null)userDobMonthTextInput.setText(month);
        if(year!=null)userDobYearTextInput.setText(year);

        userDobUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDobDayTextInput.getText() != null &&
                        !userDobDayTextInput.getText().equals("") &&
                        userDobMonthTextInput.getText() != null &&
                        !userDobMonthTextInput.getText().equals("") &&
                        userDobYearTextInput.getText() != null &&
                        !userDobYearTextInput.getText().equals("")


                ) {

                    if (myPhoneNo != null
                            && Integer.valueOf(userDobMonthTextInput.getText().toString()) < 13
                            && Integer.valueOf(userDobMonthTextInput.getText().toString()) > 0
                            && userDobYearTextInput.getText().toString().length() == 4
                            &&  Integer.valueOf(userDobYearTextInput.getText().toString()) >1900
                            && Integer.valueOf(userDobDayTextInput.getText().toString()) < 32
                            && Integer.valueOf(userDobDayTextInput.getText().toString()) > 0) {
                        //update from server


                            ShowLoder("Updating...");
                            userDobUpdate.setVisibility(View.INVISIBLE);
                            cancelButton.setVisibility(View.INVISIBLE);
                            myDob = userDobDayTextInput.getText().toString()+"/"+userDobMonthTextInput.getText().toString()+"/"+userDobYearTextInput.getText().toString();
                            URL profileUpdateUrl = NetworkUtils.buildProfileUpdateUrl();
                            new DobUpdateTask().execute(profileUpdateUrl);


                    } else {
                    if(Integer.valueOf(userDobMonthTextInput.getText().toString()) >= 13 || Integer.valueOf(userDobMonthTextInput.getText().toString()) <0 ){
                        View parentLayout = findViewById(R.id.sb_for_dob);

                        Snackbar snackbar = Snackbar.make(parentLayout, "Enter Valid Month", Snackbar.LENGTH_LONG)
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
                    if(userDobYearTextInput.getText().length() != 4 ||  Integer.valueOf(userDobYearTextInput.getText().toString()) <1900) {
                        View parentLayout = findViewById(R.id.sb_for_dob);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Enter Valid Year", Snackbar.LENGTH_LONG)
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
                    if(Integer.valueOf(userDobDayTextInput.getText().toString()) >= 32 || Integer.valueOf(userDobDayTextInput.getText().toString()) <0){
                            View parentLayout = findViewById(R.id.sb_for_dob);
                        Snackbar snackbar = Snackbar.make(parentLayout, "Enter Valid Day", Snackbar.LENGTH_LONG)
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
                       /* Toast.makeText(ProfileDobEdit.this, "Please Enter Valid Date of birth", Toast.LENGTH_SHORT).show();
                    View parentLayout = findViewById(R.id.sb_for_dob);
                    Snackbar.make(parentLayout,  " Please Enter Valid Date of birth", Snackbar.LENGTH_INDEFINITE)
                            .show();*/
                    }
                }
            }
        });
    }

    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }

    public class DobUpdateTask extends AsyncTask<URL, Void, String> {



        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String profileUpdateResults = null;
            try {
                profileUpdateResults = NetworkUtils.getProfileUpdateResponseFromHttpUrl(searchUrl,myPhoneNo,"dob",myDob);
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

                dialog.dismiss();



                if(success != null){

                    View parentLayout = findViewById(R.id.sb_for_dob);
                    Snackbar.make(parentLayout,  ""+success, Snackbar.LENGTH_LONG)
                            .show();
                    SwithToProfile();

                }
                if(error  != null){
                    View parentLayout = findViewById(R.id.sb_for_dob);
                    Snackbar.make(parentLayout,  ""+error, Snackbar.LENGTH_LONG)
                            .show();
                }

            }else{
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                userDobUpdate.setVisibility(View.VISIBLE);
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
