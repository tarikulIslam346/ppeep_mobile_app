package com.example.ppeepfinal;

import android.content.Intent;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class InputDetails extends AppCompatActivity {
    Button InputDetails;
    EditText mInputName;
    String phoneNo;
    private UserDatabase mdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_details);
        InputDetails =(Button) findViewById(R.id.input_details);
        mInputName = (EditText)findViewById(R.id.name_input);

        mdb = UserDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        phoneNo = intent.getStringExtra("phone");



        InputDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                URL postUserNameUrl = NetworkUtils.buildRegisterUrl();
                new PostNameTask().execute(postUserNameUrl);
            }

        });
    }

    public class PostNameTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String postNameResults = null;
            try {
                postNameResults = NetworkUtils.postUserNameResponseUrl(searchUrl,phoneNo,mInputName.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return postNameResults ;
        }

        @Override
        protected void onPostExecute(String postNameResults) {
            Intent HomepageStart = new Intent(InputDetails.this, HomePage.class);
            if (postNameResults != null && !postNameResults.equals("")) {
                UserModel userModel = new UserModel(phoneNo,mInputName.getText().toString(),null,0.0,0.0);
                mdb.userDAO().insertPhone(userModel);
                finish();
                startActivity(HomepageStart);

            }else{
                Toast.makeText(InputDetails.this, "Error occurd", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
