package com.example.ppeepfinal;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
/*import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    TextView myBloodGroup,myProfileName,myPhoneNo,myProfileDetailName,myProfileDetailGender,myProfileDetaildob,myProfileDetailemail,MyProfileAddress;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    FloatingActionButton floatingActionButtonImage;
    ImageView accountProfile;
    ProgressDialog dialog;
    Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database

        myProfileName = (TextView) findViewById(R.id.tv_edit_page_profile_name);

        myPhoneNo = (TextView) findViewById(R.id.tv_edit_page_phone);
        myProfileDetailName = (TextView) findViewById(R.id.tv_profile_detail_name) ;
        myProfileDetailGender = (TextView) findViewById(R.id.tv_profile_detail_gender);
        myProfileDetailemail = (TextView) findViewById(R.id.tv_profile_detail_email);
        myProfileDetaildob = (TextView) findViewById(R.id.tv_profile_detail_dob);
        MyProfileAddress=(TextView) findViewById(R.id.addresTextView);
        myBloodGroup = (TextView)findViewById(R.id.myBloodGroup);
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



    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
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
            imageBitmap = (Bitmap) extras.get("data");
            //ShowLoder("Loading...");
            URL profileImageUrl = NetworkUtils.buildUserImageUploadUrl();
            new ProfileImageUploadTask().execute(profileImageUrl);
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
        ShowLoder("Loading Profile...");
        URL profileDetailUrl = NetworkUtils.buildProfileDetailInfoUrl();
        new ProfileDetailTask().execute(profileDetailUrl);
    }
    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
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

                String userName = null,email = null,gender =null,dob=null,address = null,blood_group=null;



                try {
                    userInfo = new JSONObject(json);
                    jsonArray = userInfo.getJSONArray("user");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject userProfile = jsonArray.getJSONObject(i);
                        userName = userProfile.getString("first_name");
                        email = userProfile.getString("email");
                        gender = userProfile.getString("gender");
                        dob = userProfile.getString("dob");
                        address = userProfile.getString("address");
                        blood_group = userProfile.getString("blood_group");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();

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
                if(address != null ){
                    MyProfileAddress.setText(address);
                }
                if(blood_group != null ){
                    myBloodGroup.setText(blood_group);
                }

                ImageView nameEdit = findViewById(R.id.nameEditID);
                nameEdit.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        Intent NameEditIntent = new Intent(getApplicationContext(),ProfileNameEdit.class);

                        startActivity(NameEditIntent);
                        finish();

                    }

                });



                ImageView GenderEdit = findViewById(R.id.GenderEditID);
                GenderEdit.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        Intent GenderEditIntent = new Intent(getApplicationContext(),ProfileGenderEdit.class);
                        startActivity(GenderEditIntent);
                        finish();

                    }

                });




                ImageView EmailEdit = findViewById(R.id.EmailEditID);
                EmailEdit.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        Intent EmailEditIntent = new Intent(getApplicationContext(),ProfileEmailEdit.class);
                        EmailEditIntent.putExtra("email",myProfileDetailemail.getText().toString());
                        startActivity(EmailEditIntent);
                        finish();

                    }

                });


                ImageView AddressEdit = findViewById(R.id.AddressEditID);
                AddressEdit.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        Intent AddressEditIntent = new Intent(getApplicationContext(),ProfileAddressEdit.class);
                        AddressEditIntent.putExtra("address",MyProfileAddress.getText().toString());
                        startActivity(AddressEditIntent);
                        finish();
                    }

                });


                ImageView DOBEdit = findViewById(R.id.dobEditID);

                DOBEdit.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        Intent DOBEditIntent = new Intent(getApplicationContext(),ProfileDobEdit.class);
                        String str = myProfileDetaildob.getText().toString();
                        String arr[] = str.split("/");
                        Log.d("DOB : Day ", arr[0]);
                        Log.d("DOB : Month ", arr[1]);
                        Log.d("DOB : Year ", arr[2]);
                        DOBEditIntent.putExtra("day",arr[0]);
                        DOBEditIntent.putExtra("month",arr[1]);
                        DOBEditIntent.putExtra("year",arr[2]);
                        startActivity(DOBEditIntent);
                        finish();
                    }

                });


                ImageView BloodEdit = findViewById(R.id.BloodGroupEditID);
                BloodEdit.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        Intent BloodEditIntent = new Intent(getApplicationContext(),ProfileBloodGroupEdit.class);

                        startActivity(BloodEditIntent);
                        finish();
                    }

                });










            }else{
                dialog.dismiss();
                View parentLayout = findViewById(R.id.sb_profile_info);
                Snackbar.make(parentLayout, "Net connection error", Snackbar.LENGTH_INDEFINITE)
                        .show();
            }
        }


    }
    public class ProfileImageUploadTask extends AsyncTask<URL, Void, String> {



        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String profileImageUploadResults = null;
            try {
                profileImageUploadResults = NetworkUtils.getImageUploadResponseFromHttpUrl(searchUrl,myPhoneNo.getText().toString(),imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return profileImageUploadResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String profileImageUploadResults) {
            if (profileImageUploadResults != null && !profileImageUploadResults.equals("")) {



                String json = profileImageUploadResults;
                JSONObject userImage = null;

                String message = null,Success = null;

                try {
                    userImage = new JSONObject(json);
                    Success = userImage.getString("Success");


                } catch (JSONException e) {
                    e.printStackTrace();
                }



                try {
                    userImage = new JSONObject(json);
                    message = userImage.getString("Message");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

               // dialog.dismiss();
                View parentLayout = findViewById(R.id.sb_profile_info);
                if(message!=null) Snackbar.make(parentLayout, ""+message, Snackbar.LENGTH_INDEFINITE).show();
                if(Success!=null) Snackbar.make(parentLayout, ""+Success, Snackbar.LENGTH_INDEFINITE).show();








            }else{
                //dialog.dismiss();
                View parentLayout = findViewById(R.id.sb_profile_info);
                Snackbar.make(parentLayout, "Net connection error", Snackbar.LENGTH_INDEFINITE)
                        .show();
            }
        }


    }
}
