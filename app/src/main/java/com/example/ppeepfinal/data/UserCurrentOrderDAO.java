package com.example.ppeepfinal.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserCurrentOrderDAO {

    @Query("SELECT * FROM `userCurrentOrder`")
    List<UserCurrentOrder> loadCurrentOrder();

    @Insert
    void insertCurrentOrder(UserCurrentOrder userCurrentOrder);

    @Delete
    void  deleteCurrentOrder(UserCurrentOrder userCurrentOrder);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCurrentOrder(UserCurrentOrder userCurrentOrder);

    @Query("SELECT * FROM `userCurrentOrder` WHERE orderid = :orderid")
    UserCurrentOrder loadCurrentOrderById(int orderid);
}
