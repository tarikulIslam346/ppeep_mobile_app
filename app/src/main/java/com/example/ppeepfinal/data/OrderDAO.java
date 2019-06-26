package com.example.ppeepfinal.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrderDAO {

    @Query("SELECT * FROM `order`")
    List<OrderModel> loadOrder();

    @Insert
    void insertOrder(OrderModel orderModel);

    @Delete
    void  deleteOrder(OrderModel orderModel);
}
