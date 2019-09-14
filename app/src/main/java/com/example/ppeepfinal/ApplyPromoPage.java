package com.example.ppeepfinal;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ppeepfinal.data.UserDatabase;
import com.example.ppeepfinal.data.UserModel;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApplyPromoPage extends AppCompatActivity {

    ProgressDialog dialog;
    UserDatabase mdb;
    String phoneNo,code;
    MaterialCardView mPromoCodeCard;
    TextView mPromocode,mPromoCodePercentage,mPromocodeOrderAmount,mPromoCodeMaxDiscount,mPromoCodeValidity;
    Button mCancel,mApply,mAcceptPromo;
    EditText mPromoinput;
    TextView promoCodeDetails;
    TextView percentagePromocode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_apply_promo_page);

        mdb = UserDatabase.getInstance(getApplicationContext());

        mPromoCodeCard = (MaterialCardView)findViewById(R.id.card_layout_promo_code);

      promoCodeDetails = (TextView) findViewById(R.id.promotiondetailsId);

        percentagePromocode = (TextView) findViewById(R.id.percentage_promocode);

       /* mPromocode = (TextView) findViewById(R.id.tv_promo_code);

        mPromoCodePercentage = (TextView) findViewById(R.id.tv_promocode_percentage);

        mPromocodeOrderAmount = (TextView) findViewById(R.id.tv_promocode_order_amount);

      //  mPromoCodeMaxDiscount = (TextView) findViewById(R.id.tv_promocode_max_amount);

        mPromoCodeValidity = (TextView) findViewById(R.id.tv_promocode_validity);
*/
        mApply = (Button) findViewById(R.id.bt_apply_promo);

        mCancel = (Button) findViewById(R.id.bt_cancel);

     mAcceptPromo = (Button) findViewById(R.id.bt_accept_promo);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartPageintent = new Intent(ApplyPromoPage.this,FoodCartPage.class);

                startActivity(cartPageintent);
                finish();
            }
        });
        mPromoinput = (EditText) findViewById(R.id.promoInputEditText);

        mPromoCodeCard.setVisibility(View.INVISIBLE);

        List<UserModel> user =  mdb.userDAO().loadPhone();

        if(user.size()!=0){

            phoneNo = user.get(0).getPhone();


        }

        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPromoinput.getText() != null && !mPromoinput.getText().equals("")) {

                    if (phoneNo != null) {

                        code = mPromoinput.getText().toString();

                        ShowLoder("Loading Promocode ....","Promo Code Apply");

                        URL promocodeUrl = NetworkUtils.buildUserGetPromoUrl();

                        new PromocodeTask().execute(promocodeUrl);
                    }
                } else {
                    View parentLayout = findViewById(R.id.sb_promo_page);
                    Snackbar.make(parentLayout, " Please Enter Promo Code", Snackbar.LENGTH_INDEFINITE)
                            .show();
                }
            }
        });



    }

    public void ShowLoder(String message,String title){

       dialog = ProgressDialog.show(ApplyPromoPage.this, title, message, true);
    }

    public class  PromocodeTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String promocodeResults = null;
            try {
                promocodeResults = NetworkUtils.getPromoCodeResponseFromHttpUrl(searchUrl,phoneNo,code);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return promocodeResults;
        }
        @Override
        protected void onPostExecute(String promocodeResults) {

            if (promocodeResults != null && !promocodeResults.equals("")) {

                String json = promocodeResults;

                JSONObject promocode= null;

                JSONArray jsonArray=null;

                int order_amount=0,discount_amount=0,percentage=0;


                String promo_code=null,validity=null,error=null;



                try {
                    promocode = new JSONObject(json);
                    jsonArray = promocode.getJSONArray("promo");

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject promo = jsonArray.getJSONObject(i);

                        promo_code = promo.getString("code");
                        validity = promo.getString("validity");
                        order_amount = promo.getInt("order_applicable_amount");
                        discount_amount = promo.getInt("max_discount_amount");
                        percentage = promo.getInt("percentage");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try{
                    promocode = new JSONObject(json);
                    error= promocode.getString("error");

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();


               if(error != null ){
                    Snackbar.make(findViewById(R.id.sb_promo_page), " "+error, Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }

                if(jsonArray!= null){
                    final int max_amount = discount_amount;

                    promoCodeDetails.setText("Enjoy "+String.valueOf(percentage)+"% Off up to BDT "+String.valueOf(discount_amount)+" on your next "+String.valueOf(order_amount)+"  orders! Offer valid on orders above BDT 200 till "+validity+".");

                    percentagePromocode.setText("Enjoy "+promo_code+"% Off!!");
               //     mPromoCodeMaxDiscount.setText("Enjoy "+String.valueOf(percentage)+" Off up to BDT "+String.valueOf(discount_amount)+" on your next "+String.valueOf(order_amount)+"  orders! Offer valid on orders above BDT 200 till "+validity+".");
                //    mPromocodeOrderAmount.setText(String.valueOf(order_amount)+"  Order");
                  //  mPromoCodePercentage.setText(String.valueOf(percentage));
                   // mPromoCodeValidity.setText(validity);
                    mPromoCodeCard.setVisibility(View.VISIBLE);

                    mAcceptPromo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent cartPageintent = new Intent(ApplyPromoPage.this,FoodCartPage.class);
                            cartPageintent.putExtra("promo_code",mPromocode.getText().toString());
                            cartPageintent.putExtra("percentage",mPromoCodePercentage.getText().toString());
                            cartPageintent.putExtra("max_discount",String.valueOf(max_amount));
                            finish();
                            startActivity(cartPageintent);

                        }
                    });


                }





            }else{
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_SHORT).show();
            }
        }




    }
}
