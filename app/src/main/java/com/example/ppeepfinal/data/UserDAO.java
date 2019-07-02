package com.example.ppeepfinal.data;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user")
    List<UserModel> loadPhone();

    @Insert
    void insertPhone(UserModel userModel);

    @Delete
    void  deletePhone(UserModel userModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(UserModel userModel);

    @Query("SELECT * FROM user WHERE id = :id")
    UserModel loadUserById(int id);

}
