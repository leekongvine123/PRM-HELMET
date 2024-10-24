package com.example.myapplication.model;

import java.util.List;

public class ColorInfo {
    private String color;
    private String imageUrl;
    private List<SizeStock> sizeStocks;

    public ColorInfo(List<SizeStock> sizeStocks, String imageUrl) {
        this.imageUrl = imageUrl;
        this.sizeStocks = sizeStocks;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<SizeStock> getSizeStocks() {
        return sizeStocks;
    }

    public void setSizeStocks(List<SizeStock> sizeStocks) {
        this.sizeStocks = sizeStocks;
    }


    public String getImageUrl() {
        return imageUrl;
    }



    public static class SizeStock{
        private String size;
        private int stock;
        private double price;

        public SizeStock(String size, int stock, double price) {
            this.size = size;
            this.stock = stock;
            this.price = price;
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

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
