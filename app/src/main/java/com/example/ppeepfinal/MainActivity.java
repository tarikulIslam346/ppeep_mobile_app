package com.example.ppeepfinal;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

public class MainActivity extends AppCompatActivity {

    Button LetsGoEnter, SignUpButton;
    TextView TermsConditions;
    Handler mHandler;
    public static int APP_REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_screen);


        LetsGoEnter = (Button) findViewById(R.id.enterletGo);

        LetsGoEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            phoneLogin(v);


              /* Intent letsGoIntent = new Intent(MainActivity.this,HomePage.class);
                startActivity(letsGoIntent);*/
            }





        });

/*

        TermsConditions = (TextView) findViewById(R.id.TermsConditionId);

        TermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent termsConditionsIntent = new Intent(MainActivity.this,TermsConditions.class);
                startActivity(termsConditionsIntent);
            }
        });
*/

    }
    public void  phoneLogin(final View view) {
        final Intent intent = new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
       AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
           @Override
           public void onSuccess(Account account) {
              /* Intent letsGoIntent = new Intent(getApplicationContext(),HomePage.class);
               startActivity(letsGoIntent);*/
           }

           @Override
           public void onError(AccountKitError accountKitError) {

           }
       });


    }


   /* public void goToMyLoggedInActivity()
    {

    }*/
}
