package com.example.ppeepfinal;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpPage extends AppCompatActivity {
TextView LogInHere;
Button SignUpReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

//LogInHere = (TextView) findViewById(R.id.logInHere);



        LogInHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginHereIntent = new Intent(SignUpPage.this,LogInPage.class);
                startActivity(loginHereIntent);
            }
        });


        SignUpReg = (Button) findViewById(R.id.sign_up_reg);

        SignUpReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(SignUpPage.this,OTPpage.class);
                startActivity(signUpIntent);
            }
        });

    }
}
