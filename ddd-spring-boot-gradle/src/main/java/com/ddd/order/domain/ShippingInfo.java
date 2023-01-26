package com.ddd.order.domain;

public class ShippingInfo {
    private String receiverName;
    private String receiverPhoneNumber;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingZipCode;

    public ShippingInfo() {}

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public String getShippingZipCode() {
        return shippingZipCode;
    }
}
