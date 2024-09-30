package com.example.myapplication.model;

public class OrderDetail {
    private int orderDetailID;
    private int orderID;
    private int helmetID;
    private int quantity;
    private double price;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailID, int orderID, int helmetID, int quantity, double price) {
        this.orderDetailID = orderDetailID;
        this.orderID = orderID;
        this.helmetID = helmetID;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(int orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getHelmetID() {
        return helmetID;
    }

    public void setHelmetID(int helmetID) {
        this.helmetID = helmetID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}