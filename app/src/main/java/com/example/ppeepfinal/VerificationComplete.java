



package com.example.ppeepfinal;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VerificationComplete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_complete);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(VerificationComplete.this, HomePage.class);
                VerificationComplete.this.startActivity(mainIntent);
                VerificationComplete.this.finish();
            }
        }, 3700);
    }
}
