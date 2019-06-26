package com.example.ppeepfinal.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "orderMerchant")
public class OrderMerchantModel {

    @PrimaryKey(autoGenerate = true)
    private  int id;
    private int merchantId;

    @Ignore
    public OrderMerchantModel( int merchantId){
        this.merchantId = merchantId;
    }

    public OrderMerchantModel(int id, int merchantId){
        this.id = id;
        this.merchantId = merchantId;

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
}
