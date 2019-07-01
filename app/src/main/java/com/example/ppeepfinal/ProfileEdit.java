package com.example.ppeepfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ProfileEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);


        ImageView nameEdit = findViewById(R.id.nameEditID);
        nameEdit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent NameEditIntent = new Intent(getApplicationContext(),ProfileNameEdit.class);
                startActivity(NameEditIntent);

            }

        });



        ImageView GenderEdit = findViewById(R.id.GenderEditID);
        GenderEdit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent GenderEditIntent = new Intent(getApplicationContext(),ProfileGenderEdit.class);
                startActivity(GenderEditIntent);

            }

        });




        ImageView EmailEdit = findViewById(R.id.EmailEditID);
        EmailEdit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent EmailEditIntent = new Intent(getApplicationContext(),ProfileEmailEdit.class);
                startActivity(EmailEditIntent);

            }

        });


        ImageView AddressEdit = findViewById(R.id.AddressEditID);
        AddressEdit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent AddressEditIntent = new Intent(getApplicationContext(),ProfileAddressEdit.class);
                startActivity(AddressEditIntent);

            }

        });


        ImageView DOBEdit = findViewById(R.id.dobEditID);
        DOBEdit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent DOBEditIntent = new Intent(getApplicationContext(),ProfileDobEdit.class);
                startActivity(DOBEditIntent);

            }

        });


        ImageView BloodEdit = findViewById(R.id.BloodGroupEditID);
        BloodEdit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent BloodEditIntent = new Intent(getApplicationContext(),ProfileBloodGroupEdit.class);
                startActivity(BloodEditIntent);

            }

        });

    }
}
