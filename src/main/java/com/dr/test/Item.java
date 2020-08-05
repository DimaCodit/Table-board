package com.dr.test;

public class Item {
    public int id;
    public String product;
    public String quantity;
    public String price;
    public String discount;

    public Item(int id, String product, String quantity, String price, String discount) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }
}
