package com.ddd.order.domain;

public class OrderLine {
    private Product product;
    private int price;
    private int quantity;
    private int amounts;

    public OrderLine(Product product, int price, int quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.amounts = calculateAmounts();
    }

    private int calculateAmounts() {
        return price * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmounts() {
        return amounts;
    }
}
