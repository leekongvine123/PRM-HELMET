package com.example.myapplication.model;

public class Helmet {
    private int helmetID;
    private String name;
    private String brand;
    private String size;
    private String color;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private String createdAt;
    private String productCode;

    public Helmet() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Helmet(int helmetID, String productCode, String createdAt, String imageUrl, int stock, double price, String description, String color, String size, String brand, String name) {
        this.helmetID = helmetID;
        this.productCode = productCode;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.color = color;
        this.size = size;
        this.brand = brand;
        this.name = name;
    }

    public Helmet(int helmetID, String name, String brand, String size, String color, String description, double price, int stock, String imageUrl, String createdAt) {
        this.helmetID = helmetID;
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public int getHelmetID() {
        return helmetID;
    }

    public void setHelmetID(int helmetID) {
        this.helmetID = helmetID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    // ... getter và setter
}