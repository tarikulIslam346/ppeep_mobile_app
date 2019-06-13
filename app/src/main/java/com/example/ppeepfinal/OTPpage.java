package com.example.ppeepfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OTPpage extends AppCompatActivity {
Button SubmitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);

      SubmitButton = (Button) findViewById(R.id.submit_button);


        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitIntent = new Intent(OTPpage.this,VerificationComplete.class);
                startActivity(submitIntent);
            }
        });


    }
}
