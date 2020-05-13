package com.example.myapplication.Model;

public class Cart {
    private String  pid, name, pname, price , quantity, time, date, discount;

    public Cart() {

    }

    public Cart(String pid, String name, String pname, String price, String quantity, String time, String date, String discount) {
        this.pid = pid;
        this.name = name;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        this.date = date;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
