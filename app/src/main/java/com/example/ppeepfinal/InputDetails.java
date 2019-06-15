package com.example.ppeepfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InputDetails extends AppCompatActivity {
Button InputDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_details);
        InputDetails =(Button) findViewById(R.id.input_details);

        InputDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent InputDetailsIntent = new Intent(InputDetails.this,HomePage.class);
                startActivity(InputDetailsIntent);
            }

        });
    }
}
