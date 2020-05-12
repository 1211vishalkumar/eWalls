package com.example.myapplication.Model;

public class Users {
    private String name,phoneNo,password,image,address;


    // constructor
    public Users(){

    }
    // forming the constructor

    public Users(String name, String phoneNo, String password, String image, String address) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.password = password;
        this.image = image;
        this.address = address;
    }

    //getter and setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
