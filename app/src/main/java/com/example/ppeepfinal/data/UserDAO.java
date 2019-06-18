package com.example.ppeepfinal.data;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user")
    List<UserModel> loadPhone();

    @Insert
    void insertPhone(UserModel userModel);

    @Delete
    void  deletePhone(UserModel userModel);

}
