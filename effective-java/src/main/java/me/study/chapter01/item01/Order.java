package me.study.chapter01.item01;

import java.util.Arrays;

public class Order {

    private boolean prime;

    private boolean urgent;

    private Product product;

    // enum을 사용함으로서 type safely
    private OrderStatus orderStatus;

//    public Order(boolean prime, Product product) {
//        this.prime = prime;
//        this.product = product;
//    }
//
//    public Order(boolean urgent, Product product) {
//        this.prime = urgent;
//        this.product = product;
//    }

    public static Order primeOrder(Product product) {
        Order order = new Order();
        order.prime = true;
        order.product = product;
        return order;
    }

    public static Order urgentOrder(Product product) {
        Order order = new Order();
        order.urgent = true;
        order.product = product;
        return order;
    }

    public static void main(String[] args) {
        Arrays.stream(OrderStatus.values()).forEach(System.out::println);
    }
}
