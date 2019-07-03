package com.example.ppeepfinal.data;

/*import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;*/

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDAO {

    @Query("SELECT * FROM `order`")
    List<OrderModel> loadOrder();

    @Insert
    void insertOrder(OrderModel orderModel);

    @Delete
    void  deleteOrder(OrderModel orderModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateOrder(OrderModel orderModel);

    @Query("SELECT * FROM `order` WHERE ItemId = :ItemId")
    OrderModel loadOrderById(int ItemId);
}
