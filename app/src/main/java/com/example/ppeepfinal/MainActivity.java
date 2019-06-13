package com.example.ppeepfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button LetsGoEnter, SignUpButton;
    TextView TermsConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_screen);


        LetsGoEnter = (Button) findViewById(R.id.enterletGo);

        LetsGoEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent letsGoIntent = new Intent(MainActivity.this,OTPpage.class);
                startActivity(letsGoIntent);
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
}
