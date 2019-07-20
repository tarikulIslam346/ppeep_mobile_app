package com.example.ppeepfinal.data;

/*import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;*/

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class UserModel {

    @PrimaryKey(autoGenerate = true)
    private  int id;
    private String phone;
    private String name;
    private String address;
    private  double lat;
    private  double lng;


    @Ignore
    public UserModel( String phone,String name,String address,double lat,double lng){
        //this.id = id;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;

    }

    public UserModel(int id, String phone,String name,String address,double lat,double lng){
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
