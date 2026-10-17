package com.homemess.tiffinservice.model;

/**
 * Order Entity Model
 * Statuses: PLACED, ACCEPTED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
 */
public class Order {
    private int id;
    private int customerId;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String itemsJson;
    private double totalPrice;
    private String status;
    private String paymentMode;
    private String orderDate;
    private int deliveryAgentId;
    private String otp;

    public Order() {}

    public Order(int id, int customerId, String customerName, String customerPhone, String deliveryAddress, 
                 String itemsJson, double totalPrice, String status, String paymentMode, String orderDate, 
                 int deliveryAgentId, String otp) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.itemsJson = itemsJson;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentMode = paymentMode;
        this.orderDate = orderDate;
        this.deliveryAgentId = deliveryAgentId;
        this.otp = otp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getItemsJson() { return itemsJson; }
    public void setItemsJson(String itemsJson) { this.itemsJson = itemsJson; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public int getDeliveryAgentId() { return deliveryAgentId; }
    public void setDeliveryAgentId(int deliveryAgentId) { this.deliveryAgentId = deliveryAgentId; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}
