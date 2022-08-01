package me.study.chapter01.item01;

import java.util.Arrays;

public enum OrderStatus {

    PREPARING, SHIPPED, DELIVERING, DELIVERED;

    public static boolean hasOrderStatus(String type) {
        return Arrays.stream(OrderStatus.values())
                .anyMatch(orderStatus -> orderStatus.name().equals(type));
    }

}
