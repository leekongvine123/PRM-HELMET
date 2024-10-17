package com.example.myapplication.model;

public class CartItem {
    private int cartID;
    private int helmetID;
    private String helmetName;
    private String size;
    private String color;
    private int quantity;
    private double price;
    private Helmet helmet;

    // Getters and setters
    public int getCartID() { return cartID; }
    public void setCartID(int cartID) { this.cartID = cartID; }

    public int getHelmetID() { return helmetID; }
    public void setHelmetID(int helmetID) { this.helmetID = helmetID; }

    public String getHelmetName() { return helmetName; }
    public void setHelmetName(String helmetName) { this.helmetName = helmetName; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Helmet getHelmet() { return helmet; }
    public void setHelmet(Helmet helmet) { this.helmet = helmet; }
}

