package com.example.ppeepfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfileEdit extends AppCompatActivity {

    private UserDatabase mdb;
    TextView myProfileName,myPhoneNo,myProfileDetailName,myProfileDetailGender,myProfileDetaildob,myProfileDetailemail;
    ProgressBar progressBar;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    FloatingActionButton floatingActionButtonImage;
    ImageView accountProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database

        myProfileName = (TextView) findViewById(R.id.tv_edit_page_profile_name);

        myPhoneNo = (TextView) findViewById(R.id.tv_edit_page_phone);
        progressBar = (ProgressBar) findViewById(R.id.pv_edit_page_profile_detail) ;
        myProfileDetailName = (TextView) findViewById(R.id.tv_profile_detail_name) ;
        myProfileDetailGender = (TextView) findViewById(R.id.tv_profile_detail_gender);
        myProfileDetailemail = (TextView) findViewById(R.id.tv_profile_detail_email);
        myProfileDetaildob = (TextView) findViewById(R.id.tv_profile_detail_dob);
        floatingActionButtonImage = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        accountProfile = (ImageView) findViewById(R.id.imageview_account_profile);

        floatingActionButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        loadUserFromDb();
        loadUserFromServer();



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

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            accountProfile.setImageBitmap(imageBitmap);
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        loadUserFromDb();
        loadUserFromServer();
    }

    public  void loadUserFromDb(){
        List<UserModel> user =  mdb.userDAO().loadPhone();//select all data form room database user table

        if(user.size()!= 0){//if data exist

            myPhoneNo.setText(user.get(0).getPhone());// set phone to text view

            myProfileName.setText(user.get(0).getName());// set name to text view
        }
    }

    public  void loadUserFromServer(){
        URL profileDetailUrl = NetworkUtils.buildProfileDetailInfoUrl();
        new ProfileDetailTask().execute(profileDetailUrl);
    }

    public class ProfileDetailTask extends AsyncTask<URL, Void, String> {



        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String profileDetailResults = null;
            try {
                profileDetailResults = NetworkUtils.getProfileDetailResponseFromHttpUrl(searchUrl,myPhoneNo.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return profileDetailResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String profileDetailResults) {
            if (profileDetailResults != null && !profileDetailResults.equals("")) {



                String json = profileDetailResults;
                JSONObject userInfo = null;
                JSONArray jsonArray=null;

                String userName = null,email = null,gender =null,dob=null;



                try {
                    userInfo = new JSONObject(json);
                    jsonArray = userInfo.getJSONArray("user");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject restaurant = jsonArray.getJSONObject(i);
                        userName = restaurant.getString("first_name");
                        email = restaurant.getString("email");
                        gender = restaurant.getString("gender");
                        dob = restaurant.getString("dob");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();

                layoutParams.height = 0;
                layoutParams.width = 0;
                progressBar.setLayoutParams(layoutParams);

                if(userName != null ){
                    myProfileDetailName.setText(userName);
                }
                if(email != null ){
                    myProfileDetailemail.setText(email);
                }
                if(gender != null ){
                    myProfileDetailGender.setText(gender);
                }
                if(dob != null ){
                    myProfileDetaildob.setText(dob);
                }








            }else{
                Toast.makeText(getApplicationContext(), "No restaurant found or network not available", Toast.LENGTH_LONG).show();
            }
        }


    }
}