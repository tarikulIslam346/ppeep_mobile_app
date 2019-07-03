package com.example.ppeepfinal.data;

/*import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;*/

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "orderMerchant")
public class OrderMerchantModel {

    @PrimaryKey(autoGenerate = true)
    private  int id;
    private int merchantId;
    private String merchantName;
    private  int vat;
    private int deliveryCharge;

    @Ignore
    public OrderMerchantModel( int merchantId,String merchantName,int vat,int deliveryCharge){
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.vat = vat;
        this.deliveryCharge = deliveryCharge;
    }

    public OrderMerchantModel(int id, int merchantId,String merchantName,int vat,int deliveryCharge){
        this.id = id;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.vat = vat;
        this.deliveryCharge = deliveryCharge;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(int deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }
}
