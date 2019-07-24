package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.MyLocation;
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


public class MainActivity extends AppCompatActivity {

    Button LetsGoEnter, SignUpButton;
    TextView TermsConditions;
    Handler mHandler;
    public static int APP_REQUEST_CODE = 99;
    String accountKitId, phoneNumberString;
    ProgressBar pageSwitchProgress;
    private UserDatabase mdb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_screen);

       // service = APIClient.createService(ApiService.class);
        MyLocation myLocation = new MyLocation(MainActivity.this);
        mdb = UserDatabase.getInstance(getApplicationContext());


        LetsGoEnter = (Button) findViewById(R.id.enterletGo);
        pageSwitchProgress = (ProgressBar) findViewById(R.id.page_switch_progressbar) ;

        LetsGoEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            phoneLogin(v);
            }

        });


    }

    public void phoneLogin(View view) {
        final Intent intent = new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
                        // or .ResponseType.CODE
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


       AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (phoneNumber != null) {
                    phoneNumberString = phoneNumber.toString();
                    //NetworkUtils nt = new NetworkUtils();
                   // nt.setPhoneNo(phoneNumberString);
                    URL userExistCheckUrl = NetworkUtils.buildPhoneCheckUrl();
                    LetsGoEnter.setVisibility(View.INVISIBLE);
                    pageSwitchProgress.setVisibility(View.VISIBLE);
                    new PhoneCheckTask().execute(userExistCheckUrl);
                }
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });


    }


    //  Create a class called PhoneCheckTask that extends AsyncTask<URL, Void, String>
    public class PhoneCheckTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String phonecheckResults = null;
            try {
                phonecheckResults = NetworkUtils.getResponseFromHttpUrl(searchUrl,phoneNumberString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return phonecheckResults ;
        }

        @Override
        protected void onPostExecute(String phonecheckResults) {
            Intent HomepageStart = new Intent(MainActivity.this, HomePage.class);
             Intent InputPageStart = new Intent(MainActivity.this,InputDetails.class);
            if (phonecheckResults != null && !phonecheckResults.equals("")) {

                    pageSwitchProgress.setVisibility(View.INVISIBLE);
                    UserModel userModel = new UserModel(phoneNumberString.toString(), phonecheckResults,null,0.0,0.0);
                    mdb.userDAO().insertPhone(userModel);
                    finish();
                    startActivity(HomepageStart);


            }else {
                pageSwitchProgress.setVisibility(View.INVISIBLE);
                InputPageStart.putExtra("phone",phoneNumberString.toString());
                startActivity(InputPageStart);
            }
        }
    }




}
