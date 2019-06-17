package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppeepfinal.utilities.NetworkUtils;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;



public class MainActivity extends AppCompatActivity {

    Button LetsGoEnter, SignUpButton;
    TextView TermsConditions;
    Handler mHandler;
    public static int APP_REQUEST_CODE = 99;
    String accountKitId, phoneNumberString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_screen);

       // service = APIClient.createService(ApiService.class);


        LetsGoEnter = (Button) findViewById(R.id.enterletGo);

        LetsGoEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                            phoneLogin(v);


              /* Intent letsGoIntent = new Intent(MainActivity.this,HomePage.class);
                startActivity(letsGoIntent);*/
            }

        });


    }

    public void phoneLogin(View view) {
        final Intent intent = new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_REQUEST_CODE) {


            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
               // showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }
                goToProfileInActivity();
            }

            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


    public  void goToProfileInActivity(){
        //phoneNumberString = "01796248710";

        //userCheckRetrofit2Api(phoneNumberString);

       AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (phoneNumber != null) {
                    phoneNumberString = phoneNumber.toString();
                    URL githubSearchUrl = NetworkUtils.buildUrl();
                    //Toast.makeText(MainActivity.this,phoneNumberString,Toast.LENGTH_SHORT).show();

                   // userCheckRetrofit2Api(phoneNumberString);
                    new GithubQueryTask().execute(githubSearchUrl);
                }

                // Get email
                //String email = account.getEmail();
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });
        //Intent inputDetailsIntent = new Intent(MainActivity.this,InputDetails.class);
        //inputDetailsIntent.putExtra("phone",)

        //startActivity(inputDetailsIntent);*/
    }

    public void userCheckRetrofit2Api(String Phone) {

    }

    // COMPLETED (1) Create a class called GithubQueryTask that extends AsyncTask<URL, Void, String>
    public class GithubQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String githubSearchResults) {
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                //mSearchResultsTextView.setText(githubSearchResults);
                //String toastMessage = "Login Cancelled";
                // Toast.makeText(MainActivity.this,githubSearchResults.toString(),Toast.LENGTH_SHORT).show();
                // if(githubSearchResults.toString() == {"message":"Number exist"})
                Intent HomepageStart = new Intent(MainActivity.this, HomePage.class);
                //inputDetailsIntent.putExtra("phone",)

                startActivity(HomepageStart);


            }else{
                Intent InputPageStart = new Intent(MainActivity.this, InputDetails.class);
                //inputDetailsIntent.putExtra("phone",)

                startActivity(InputPageStart);

            }
        }
    }




}
