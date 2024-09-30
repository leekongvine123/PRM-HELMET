package com.example.myapplication.model;

public class Payment {
    private int paymentID;
    private int orderID;
    private String paymentDate;
    private double amount;
    private String paymentMethod;

    public Payment() {
    }

    public Payment(int paymentID, int orderID, String paymentDate, double amount, String paymentMethod) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // ... getter và setter
}