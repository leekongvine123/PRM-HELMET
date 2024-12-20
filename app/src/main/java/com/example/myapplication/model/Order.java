package com.example.myapplication.model;

import java.util.Date;

public class Order {
    private int orderID;
    private int customerID;
    private String orderDate;
    private double totalAmount;
    private String paymentStatus;

    public Order() {
    }

    public Order(int customerID, double totalAmount, String paymentStatus) {
        this.customerID = customerID;
        this.orderDate = new Date().toString();
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

// ... getter và setter
}