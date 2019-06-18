package com.example.ppeepfinal.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class UserModel {

    @PrimaryKey(autoGenerate = true)
    private  int id;
    private String phone;
    private String name;


    @Ignore
    public UserModel( String phone,String name){
        //this.id = id;
        this.phone = phone;
        this.name = name;
    }

    public UserModel(int id, String phone,String name){
        this.id = id;
        this.phone = phone;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
