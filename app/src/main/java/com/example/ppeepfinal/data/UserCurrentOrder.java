package com.example.ppeepfinal.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "userCurrentOrder")
public class UserCurrentOrder {
    @PrimaryKey(autoGenerate = true)
    private  int id;
    private  int orderid;
    private int orderStatus ;


    @Ignore
    public UserCurrentOrder( int orderid,int orderStatus){
        //this.id = id;
        this.orderid = orderid;
        this.orderStatus = orderStatus;


    }

    public UserCurrentOrder(int id, int orderid,int orderStatus){
        this.id = id;
        this.orderid = orderid;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
