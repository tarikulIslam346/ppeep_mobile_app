package com.example.ppeepfinal.data;

/*import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;*/

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderMercahntDAO {
    @Query("SELECT * FROM `orderMerchant`")
    List<OrderMerchantModel> loadOrderMerchant();

    @Insert
    void insertOrderMerchant(OrderMerchantModel orderMerchantModel);

    @Delete
    void  deleteOrderMerchant(OrderMerchantModel orderMerchantModel);
}
