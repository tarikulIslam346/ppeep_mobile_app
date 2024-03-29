package com.example.ppeepfinal;

import android.app.TimePickerDialog;
import android.content.Intent;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class PreOrderTimeSelect extends AppCompatActivity {
    TimePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order_time_select);




        Calendar calendar= Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView datePicker =(TextView) findViewById(R.id.currentdatepick);
        datePicker.setText(currentDate);


        //set time

       // tvw=(TextView)findViewById(R.id.textView1);
        final TextView timePicker=(TextView) findViewById(R.id.currenttimepick);
      // timePicker.setIs24HourView(true);
        timePicker.setInputType(InputType.TYPE_NULL);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(PreOrderTimeSelect.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String am_pm;
                                if(sHour > 12) {
                                    am_pm = "PM";
                                    sHour = sHour - 12;
                                }
                                else
                                {
                                    am_pm="AM";
                                }

                                timePicker.setText(sHour + ":" + sMinute +" "+am_pm);
                            }
                        }, hour, minutes, false);
                picker.show();
            }
        });



        Button orderSubmit = (Button) findViewById(R.id.placeOrderId);
        orderSubmit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                if( !datePicker.getText().toString().equals("") && !timePicker.getText().toString().equals("")){
                    Intent orderSubmitIntent = new Intent(getApplicationContext(),FoodCartPage.class);
                    orderSubmitIntent.putExtra("date",datePicker.getText().toString());
                    orderSubmitIntent.putExtra("time",timePicker.getText().toString());
                    finish();
                    startActivity(orderSubmitIntent);
                }else{

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.pre_order_info_layout), " Enter Correct Time", Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            /* Fix it
                             * Change Action text color
                             * setActionTextColor(Color.RED)
                             * */
                            .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));

                    View sbView = snackbar.getView();

                    /* Fix it
                     * Change  text coler
                     * */
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(android.R.color.black ));

                    /* Fix it
                     * Change background  color
                     * */
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    snackbar.show();

                }



            }

        });

    }
}
