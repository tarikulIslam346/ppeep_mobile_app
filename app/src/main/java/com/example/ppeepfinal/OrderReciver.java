package com.example.ppeepfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OrderReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String state = extras.getString("extra");
        updateView(state,context);// update your textView in the main layout
    }
    public  void  updateView(String data,Context context){
        //Toast.makeText(context, ""+data, Toast.LENGTH_SHORT).show();
    }
}
