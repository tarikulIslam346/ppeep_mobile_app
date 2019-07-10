package com.example.ppeepfinal;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GroupFriendAdd extends AppCompatActivity {

    EditText mUserInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_friend_add);
        mUserInput = (EditText) findViewById(R.id.userPhoneNoTextOfFriendInputEditText);
    }
}
