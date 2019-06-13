package com.example.ppeepfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LogInPage extends AppCompatActivity {
Button LoginConfirm;
TextView CreateOneButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        LoginConfirm = (Button) findViewById(R.id.log_in_confirm);

        LoginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LogInPage.this,OTPpage.class);
                startActivity(loginIntent);
            }
        });



     CreateOneButton=(TextView) findViewById(R.id.create_one_button);


        CreateOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createoneIntent = new Intent(LogInPage.this,SignUpPage.class);
                startActivity(createoneIntent);
            }
        });


    }
}
