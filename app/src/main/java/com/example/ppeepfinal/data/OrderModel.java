package com.example.ppeepfinal.data;

/*import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;*/

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "order")
public class OrderModel {

    @PrimaryKey(autoGenerate = true)
    private  int id;
    private int ItemId;
    private String ItemName;
    private int ItemPrice;
    private int ItemAmount;
    @ColumnInfo(name = "created_at")
    private Date createdAt;


    @Ignore
    public OrderModel( int ItemId,String ItemName,int ItemPrice,int ItemAmount, Date createdAt){
        this.ItemId = ItemId;
        this.ItemName = ItemName;
        this.ItemPrice = ItemPrice;
        this.createdAt = createdAt;
        this.ItemAmount = ItemAmount;
    }

    public OrderModel(int id, int ItemId,String ItemName,int ItemPrice,int ItemAmount,Date createdAt){
        this.id = id;
        this.ItemId = ItemId;
        this.ItemName = ItemName;
        this.ItemPrice = ItemPrice;
        this.createdAt = createdAt;
        this.ItemAmount = ItemAmount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemPrice(int itemPrice) {
        ItemPrice = itemPrice;
    }

    public int getItemPrice() {
        return ItemPrice;
    }

    public int getItemAmount() {
        return ItemAmount;
    }

    public void setItemAmount(int itemAmount) {
        ItemAmount = itemAmount;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
