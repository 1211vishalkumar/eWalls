package com.example.myapplication.Model;

public class AdminOrders  {
    private String address,city,date,time,name,phoneNo,status,totalAmount;

    public AdminOrders() {
    }

    public AdminOrders(String address, String city, String date, String time, String name, String phoneNo, String status, String totalAmount) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.time = time;
        this.name = name;
        this.phoneNo = phoneNo;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
